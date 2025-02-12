package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.clearSavedInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.getSavedInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.saveInventory
import dev.aaronhowser.mods.geneticsresequenced.compatibility.curios.KeepCurioInventory
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.neoforged.fml.ModList
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.level.ExplosionEvent
import java.util.*
import kotlin.random.Random

object DeathGenes {

    //TODO: Test with grave mods
    fun saveInventory(player: Player) {
        val keepInventory = ModGenes.KEEP_INVENTORY.getHolderOrThrow(player.registryAccess())
        if (keepInventory.isDisabled) return

        player.level().apply {
            if (isClientSide) return
            if (gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)) return
            if (levelData.isHardcore) return
        }

        if (!player.hasGene(ModGenes.KEEP_INVENTORY)) return

        val playerItems =
            (player.inventory.items + player.inventory.armor + player.inventory.offhand).filter { !it.isEmpty }

        player.saveInventory(playerItems)

        val curiosIsLoaded = ModList.get().isLoaded("curios")
        if (curiosIsLoaded) {
            KeepCurioInventory.saveCurios(player)
        }

        player.inventory.clearContent()
    }

    fun returnInventory(player: Player) {
        val items = player.getSavedInventory()
        if (items.isEmpty()) return

        items.forEach { itemStack: ItemStack ->
            if (!player.inventory.add(itemStack)) {
                val itemEntity = ItemEntity(player.level(), player.x, player.y, player.z, itemStack)
                player.level().addFreshEntity(itemEntity)
            }
        }

        player.clearSavedInventory()
    }

    private val emeraldHeartCooldown = GeneCooldown(
        ModGenes.EMERALD_HEART,
        ServerConfig.emeraldHeartCooldown.get()
    )

    fun handleEmeraldHeart(event: LivingDeathEvent) {
        val emeraldHeart = ModGenes.EMERALD_HEART.getHolderOrThrow(event.entity.registryAccess())
        if (emeraldHeart.isDisabled) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.EMERALD_HEART)) return

        if (entity !is Player) {
            val itemEntity = ItemEntity(entity.level(), entity.x, entity.y, entity.z, ItemStack(Items.EMERALD, 1))
            entity.level().addFreshEntity(itemEntity)
            return
        }

        val wasNotOnCooldown = emeraldHeartCooldown.add(entity)

        if (!wasNotOnCooldown) return

        entity.inventory.add(ItemStack(Items.EMERALD, 1))
    }

    private val recentlyExplodedEntities: MutableSet<UUID> = mutableSetOf()

    private const val GUNPOWDER_REQUIRED = 5
    private const val EXPLOSION_STRENGTH = 3f
    fun handleExplosiveExit(event: LivingDeathEvent) {
        val explosiveExit = ModGenes.EXPLOSIVE_EXIT.getHolderOrThrow(event.entity.registryAccess())
        if (explosiveExit.isDisabled) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.EXPLOSIVE_EXIT)) return

        val shouldExplode = if (entity !is Player) {
            true
        } else {
            val amountGunpowder = entity.inventory.items.sumOf { if (it.item == Items.GUNPOWDER) it.count else 0 }
            amountGunpowder >= GUNPOWDER_REQUIRED
        }

        if (!shouldExplode) return

        recentlyExplodedEntities.add(entity.uuid)

        entity.level().explode(
            entity,
            entity.x,
            entity.y,
            entity.z,
            EXPLOSION_STRENGTH,
            Level.ExplosionInteraction.NONE // What the heck does this do
        )

        recentlyExplodedEntities.remove(entity.uuid)

        if (entity is Player) {
            var amountGunpowderRemoved = 0
            for (stack in entity.inventory.items) {
                if (stack.item != Items.GUNPOWDER) continue

                while (stack.count > 0 && amountGunpowderRemoved < GUNPOWDER_REQUIRED) {
                    stack.shrink(1)
                    amountGunpowderRemoved++
                }

                if (amountGunpowderRemoved >= GUNPOWDER_REQUIRED) break
            }
        }

    }

    fun explosiveExitDetonation(event: ExplosionEvent.Detonate) {
        val explosiveExit = ModGenes.EXPLOSIVE_EXIT.getHolderOrThrow(event.level.registryAccess())
        if (explosiveExit.isDisabled) return

        val exploderUuid = event.explosion.directSourceEntity?.uuid
        if (exploderUuid !in recentlyExplodedEntities) return

        event.affectedEntities.removeAll { it !is LivingEntity }
        event.affectedBlocks.clear()
    }

    private val slimyDeathCooldown = GeneCooldown(
        ModGenes.SLIMY_DEATH,
        ServerConfig.slimyDeathCooldown.get()
    )

    fun handleSlimyDeath(event: LivingDeathEvent) {
        val slimyDeath = ModGenes.SLIMY_DEATH.getHolderOrThrow(event.entity.registryAccess())
        if (slimyDeath.isDisabled) return
        if (event.isCanceled) return

        val entity: LivingEntity = event.entity
        if (!entity.hasGene(ModGenes.SLIMY_DEATH)) return

        val newlyUsed = slimyDeathCooldown.add(entity)
        if (!newlyUsed) return

        val amount = Random.nextInt(3, 6)

        repeat(amount) {
            val supportSlime = SupportSlime(entity.level(), entity.uuid)

            val randomNearbyPosition = entity.position().add(
                Random.nextDouble(-1.0, 1.0),
                0.0,
                Random.nextDouble(-1.0, 1.0)
            )

            supportSlime.moveTo(randomNearbyPosition.x, randomNearbyPosition.y, randomNearbyPosition.z)
            entity.level().addFreshEntity(supportSlime)
        }

        event.isCanceled = true
        entity.health = entity.maxHealth * ServerConfig.slimyDeathHealthMultiplier.get().toFloat()

        if (entity is ServerPlayer) {
            AdvancementTriggers.slimyDeathAdvancement(entity)
        }

    }

}