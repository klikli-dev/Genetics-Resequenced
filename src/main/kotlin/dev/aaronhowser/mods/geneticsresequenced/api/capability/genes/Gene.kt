package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

@Suppress("unused")
class Gene(
    val id: String
) {

    var isNegative: Boolean = false
        private set

    private fun setNegative(): Gene {
        this.isNegative = true
        return this
    }

    private var mutatesInto: Gene? = null
    private fun setMutatesInto(mutatesInto: Gene): Gene {
        this.mutatesInto = mutatesInto
        return this
    }

    private val isMutation: Boolean
        get() = REGISTRY.any { it.mutatesInto == this }

    var translationKey: String = ""
        private set

    private fun setTranslationKey(translationKey: String): Gene {
        this.translationKey = translationKey
        return this
    }

    var amountNeeded: Int = -1
        private set

    fun setAmountNeeded(amountNeeded: Int): Gene {
        this.amountNeeded = amountNeeded
        return this
    }

    val nameComponent: Component
        get() = Component
            .translatable(translationKey)
            .withStyle {
                it
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("tooltip.geneticsresequenced.copy_gene_id", id)
                        )
                    )
                    .withClickEvent(
                        ClickEvent(
                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                            id
                        )
                    )
            }

    private fun build(): Gene {

        if (amountNeeded == -1 || translationKey.isBlank()) {
            throw IllegalStateException("Gene $id is missing required fields")
        }

        REGISTRY.add(this)
        return this
    }

    companion object {

        private val REGISTRY: MutableSet<Gene> = mutableSetOf()
        fun getRegistry(): List<Gene> = REGISTRY.sortedBy { it.id }

        fun valueOf(searchedId: String): Gene {
            return REGISTRY.first { it.id == searchedId }
        }

        val HASTE_2 = Gene("HASTE_2")
            .setTranslationKey("gene.geneticsresequenced.haste_2")
            .setAmountNeeded(50)
            .build()
        val EFFICIENCY_4 = Gene("EFFICIENCY_4")
            .setTranslationKey("gene.geneticsresequenced.efficiency_4")
            .setAmountNeeded(50)
            .build()
        val REGENERATION_4 = Gene("REGENERATION_4")
            .setTranslationKey("gene.geneticsresequenced.regeneration_4")
            .setAmountNeeded(50)
            .build()
        val SPEED_4 = Gene("SPEED_4")
            .setTranslationKey("gene.geneticsresequenced.speed_4")
            .setAmountNeeded(50)
            .build()
        val SPEED_2 = Gene("SPEED_2")
            .setTranslationKey("gene.geneticsresequenced.speed_2")
            .setAmountNeeded(50)
            .setMutatesInto(SPEED_4)
            .build()
        val RESISTANCE_2 = Gene("RESISTANCE_2")
            .setTranslationKey("gene.geneticsresequenced.resistance_2")
            .setAmountNeeded(50)
            .build()
        val STRENGTH_2 = Gene("STRENGTH_2")
            .setTranslationKey("gene.geneticsresequenced.strength_2")
            .setAmountNeeded(50)
            .build()
        val MEATY_2 = Gene("MEATY_2")
            .setTranslationKey("gene.geneticsresequenced.meaty_2")
            .setAmountNeeded(50)
            .build()
        val MORE_HEARTS_2 = Gene("MORE_HEARTS_2")
            .setTranslationKey("gene.geneticsresequenced.more_hearts_2")
            .setAmountNeeded(50)
            .build()
        val INVISIBLE = Gene("INVISIBLE")
            .setTranslationKey("gene.geneticsresequenced.invisibility")
            .setAmountNeeded(50)
            .build()
        val FLIGHT = Gene("FLIGHT")
            .setTranslationKey("gene.geneticsresequenced.flight")
            .setAmountNeeded(50)
            .build()
        val LUCK = Gene("LUCK")
            .setTranslationKey("gene.geneticsresequenced.luck")
            .setAmountNeeded(50)
            .build()
        val SCARE_ZOMBIES = Gene("SCARE_ZOMBIES")
            .setTranslationKey("gene.geneticsresequenced.scare_zombies")
            .setAmountNeeded(50)
            .build()
        val SCARE_SPIDERS = Gene("SCARE_SPIDERS")
            .setTranslationKey("gene.geneticsresequenced.scare_spiders")
            .setAmountNeeded(50)
            .build()
        val THORNS = Gene("THORNS")
            .setTranslationKey("gene.geneticsresequenced.thorns")
            .setAmountNeeded(12)
            .build()
        val CLAWS_2 = Gene("CLAWS_2")
            .setTranslationKey("gene.geneticsresequenced.claws_2")
            .setAmountNeeded(50)
            .build()

        //Standard list
        val DRAGONS_BREATH = Gene("DRAGONS_BREATH")
            .setTranslationKey("gene.geneticsresequenced.dragons_breath")
            .setAmountNeeded(20)
            .build()
        val EAT_GRASS = Gene("EAT_GRASS")
            .setTranslationKey("gene.geneticsresequenced.eat_grass")
            .setAmountNeeded(16)
            .build()
        val EMERALD_HEART = Gene("EMERALD_HEART")
            .setTranslationKey("gene.geneticsresequenced.emerald_heart")
            .setAmountNeeded(30)
            .build()
        val ENDER_DRAGON_HEALTH = Gene("ENDER_DRAGON_HEALTH")
            .setTranslationKey("gene.geneticsresequenced.ender_dragon_health")
            .setAmountNeeded(60)
            .build()
        val EXPLOSIVE_EXIT = Gene("EXPLOSIVE_EXIT")
            .setTranslationKey("gene.geneticsresequenced.explosive_exit")
            .setAmountNeeded(20)
            .build()
        val FIRE_PROOF = Gene("FIRE_PROOF")
            .setTranslationKey("gene.geneticsresequenced.fire_proof")
            .setAmountNeeded(24)
            .build()
        val ITEM_MAGNET = Gene("ITEM_MAGNET")
            .setTranslationKey("gene.geneticsresequenced.item_magnet")
            .setAmountNeeded(30)
            .build()
        val JUMP_BOOST = Gene("JUMP_BOOST")
            .setTranslationKey("gene.geneticsresequenced.jump_boost")
            .setMutatesInto(FLIGHT)
            .setAmountNeeded(10)
            .build()
        val MILKY = Gene("MILKY")
            .setTranslationKey("gene.geneticsresequenced.milky")
            .setAmountNeeded(12)
            .build()
        val MORE_HEARTS = Gene("MORE_HEARTS")
            .setTranslationKey("gene.geneticsresequenced.more_hearts")
            .setAmountNeeded(40)
            .setMutatesInto(MORE_HEARTS_2)
            .build()
        val NIGHT_VISION = Gene("NIGHT_VISION")
            .setTranslationKey("gene.geneticsresequenced.night_vision")
            .setAmountNeeded(16)
            .build()
        val NO_FALL_DAMAGE = Gene("NO_FALL_DAMAGE")
            .setTranslationKey("gene.geneticsresequenced.no_fall_damage")
            .setAmountNeeded(30)
            .build()
        val PHOTOSYNTHESIS = Gene("PHOTOSYNTHESIS")
            .setTranslationKey("gene.geneticsresequenced.photosynthesis")
            .setAmountNeeded(40)
            .setMutatesInto(THORNS)
            .build()
        val POISON_IMMUNITY = Gene("POISON_IMMUNITY")
            .setTranslationKey("gene.geneticsresequenced.poison_immunity")
            .setAmountNeeded(24)
            .build()
        val RESISTANCE = Gene("RESISTANCE")
            .setTranslationKey("gene.geneticsresequenced.resistance")
            .setAmountNeeded(30)
            .setMutatesInto(RESISTANCE_2)
            .build()
        val KEEP_INVENTORY = Gene("KEEP_INVENTORY")
            .setTranslationKey("gene.geneticsresequenced.keep_inventory")
            .setAmountNeeded(40)
            .build()
        val SCARE_CREEPERS = Gene("SCARE_CREEPERS")
            .setTranslationKey("gene.geneticsresequenced.scare_creepers")
            .setAmountNeeded(20)
            .setMutatesInto(SCARE_ZOMBIES)
            .build()
        val SCARE_SKELETONS = Gene("SCARE_SKELETONS")
            .setTranslationKey("gene.geneticsresequenced.scare_skeletons")
            .setAmountNeeded(20)
            .setMutatesInto(SCARE_SPIDERS)
            .build()
        val SHOOT_FIREBALLS = Gene("SHOOT_FIREBALLS")
            .setTranslationKey("gene.geneticsresequenced.shoot_fireballs")
            .setAmountNeeded(24)
            .build()
        val SLIMY_DEATH = Gene("SLIMY_DEATH")
            .setTranslationKey("gene.geneticsresequenced.slimy_death")
            .setAmountNeeded(60)
            .build()
        val SPEED = Gene("SPEED")
            .setTranslationKey("gene.geneticsresequenced.speed")
            .setAmountNeeded(20)
            .setMutatesInto(SPEED_2)
            .build()
        val STRENGTH = Gene("STRENGTH")
            .setTranslationKey("gene.geneticsresequenced.strength")
            .setAmountNeeded(20)
            .setMutatesInto(STRENGTH_2)
            .build()
        val TELEPORT = Gene("TELEPORT")
            .setTranslationKey("gene.geneticsresequenced.teleport")
            .setAmountNeeded(24)
            .setMutatesInto(FLIGHT)
            .build()
        val WATER_BREATHING = Gene("WATER_BREATHING")
            .setTranslationKey("gene.geneticsresequenced.water_breathing")
            .setAmountNeeded(16)
            .build()
        val WOOLY = Gene("WOOLY")
            .setTranslationKey("gene.geneticsresequenced.wooly")
            .setAmountNeeded(12)
            .build()
        val WITHER_HIT = Gene("WITHER_HIT")
            .setTranslationKey("gene.geneticsresequenced.wither_hit")
            .setAmountNeeded(20)
            .build()
        val WITHER_PROOF = Gene("WITHER_PROOF")
            .setTranslationKey("gene.geneticsresequenced.wither_proof")
            .setAmountNeeded(40)
            .build()
        val XP_MAGNET = Gene("XP_MAGNET")
            .setTranslationKey("gene.geneticsresequenced.xp_magnet")
            .setAmountNeeded(30)
            .build()
        val STEP_ASSIST = Gene("STEP_ASSIST")
            .setTranslationKey("gene.geneticsresequenced.step_assist")
            .setAmountNeeded(10)
            .build()
        val INFINITY = Gene("INFINITY")
            .setTranslationKey("gene.geneticsresequenced.infinity")
            .setAmountNeeded(30)
            .build()
        val BIOLUMINESCENCE = Gene("BIOLUMINESCENCE")
            .setTranslationKey("gene.geneticsresequenced.bioluminescence")
            .setAmountNeeded(16)
            .build()
        val CYBERNETIC = Gene("CYBERNETIC")
            .setTranslationKey("gene.geneticsresequenced.cybernetics")
            .setAmountNeeded(30)
            .build()
        val LAY_EGG = Gene("LAY_EGG")
            .setTranslationKey("gene.geneticsresequenced.lay_eggs")
            .setAmountNeeded(12)
            .build()
        val MEATY = Gene("MEATY")
            .setTranslationKey("gene.geneticsresequenced.meaty")
            .setAmountNeeded(12)
            .setMutatesInto(MEATY_2)
            .build()
        val NO_HUNGER = Gene("NO_HUNGER")
            .setTranslationKey("gene.geneticsresequenced.no_hunger")
            .setAmountNeeded(30)
            .build()
        val CLAWS = Gene("CLAWS")
            .setTranslationKey("gene.geneticsresequenced.claws")
            .setAmountNeeded(20)
            .setMutatesInto(CLAWS_2)
            .build()
        val HASTE = Gene("HASTE")
            .setTranslationKey("gene.geneticsresequenced.haste")
            .setAmountNeeded(30)
            .setMutatesInto(HASTE_2)
            .build()
        val EFFICIENCY = Gene("EFFICIENCY")
            .setTranslationKey("gene.geneticsresequenced.efficiency")
            .setAmountNeeded(30)
            .setMutatesInto(EFFICIENCY_4)
            .build()
        val WALL_CLIMBING = Gene("WALL_CLIMBING")
            .setTranslationKey("gene.geneticsresequenced.climb_walls")
            .setAmountNeeded(40)
            .build()
        val MOB_SIGHT = Gene("MOB_SIGHT")
            .setTranslationKey("gene.geneticsresequenced.mob_sight")
            .setAmountNeeded(16)
            .build()
        val REGENERATION = Gene("REGENERATION")
            .setTranslationKey("gene.geneticsresequenced.regeneration")
            .setAmountNeeded(60)
            .setMutatesInto(REGENERATION_4)
            .build()

        //Negative effects
        val POISON = Gene("POISON")
            .setTranslationKey("gene.geneticsresequenced.poison_2")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val POISON_4 = Gene("POISON_4")
            .setTranslationKey("gene.geneticsresequenced.poison_4")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val WITHER = Gene("WITHER")
            .setTranslationKey("gene.geneticsresequenced.wither_2")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val WEAKNESS = Gene("WEAKNESS")
            .setTranslationKey("gene.geneticsresequenced.weakness")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val BLINDNESS = Gene("BLINDNESS")
            .setTranslationKey("gene.geneticsresequenced.blindness")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val SLOWNESS = Gene("SLOWNESS")
            .setTranslationKey("gene.geneticsresequenced.slowness")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val SLOWNESS_4 = Gene("SLOWNESS_4")
            .setTranslationKey("gene.geneticsresequenced.slowness_4")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val SLOWNESS_6 = Gene("SLOWNESS_6")
            .setTranslationKey("gene.geneticsresequenced.slowness_6")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val NAUSEA = Gene("NAUSEA")
            .setTranslationKey("gene.geneticsresequenced.nausea")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val HUNGER = Gene("HUNGER")
            .setTranslationKey("gene.geneticsresequenced.hunger")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val FLAME = Gene("FLAME")
            .setTranslationKey("gene.geneticsresequenced.flambe")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val CURSED = Gene("CURSED")
            .setTranslationKey("gene.geneticsresequenced.cursed")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val LEVITATION = Gene("LEVITATION")
            .setTranslationKey("gene.geneticsresequenced.levitation")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val MINING_WEAKNESS = Gene("MINING_WEAKNESS")
            .setTranslationKey("gene.geneticsresequenced.mining_weakness")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val DEAD_CREEPERS = Gene("DEAD_CREEPERS")
            .setTranslationKey("gene.geneticsresequenced.green_death")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val DEAD_UNDEAD = Gene("DEAD_UNDEAD")
            .setTranslationKey("gene.geneticsresequenced.undeath")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val DEAD_OLD_AGE = Gene("DEAD_OLD_AGE")
            .setTranslationKey("gene.geneticsresequenced.gray_death")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val DEAD_HOSTILE = Gene("DEAD_HOSTILE")
            .setTranslationKey("gene.geneticsresequenced.white_death")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val DEAD_ALL = Gene("DEAD_ALL")
            .setTranslationKey("gene.geneticsresequenced.black_death")
            .setAmountNeeded(1)
            .setNegative()
            .build()
        val REALLY_DEAD_ALL = Gene("REALLY_DEAD_ALL")
            .setTranslationKey("gene.geneticsresequenced.void_death")
            .setAmountNeeded(1)
            .setNegative()
            .build()
    }

    var isActive: Boolean = true

    fun getPotion(): MobEffectInstance? {

        val effect = when (this) {
            HASTE, HASTE_2 -> MobEffects.DIG_SPEED
            REGENERATION, REGENERATION_4 -> MobEffects.REGENERATION
            SPEED, SPEED_2, SPEED_4 -> MobEffects.MOVEMENT_SPEED
            RESISTANCE, RESISTANCE_2 -> MobEffects.DAMAGE_RESISTANCE
            STRENGTH, STRENGTH_2 -> MobEffects.DAMAGE_BOOST
            INVISIBLE -> MobEffects.INVISIBILITY
            LUCK -> MobEffects.LUCK
            NIGHT_VISION -> MobEffects.NIGHT_VISION
            JUMP_BOOST -> MobEffects.JUMP
            POISON, POISON_4 -> MobEffects.POISON
            WEAKNESS -> MobEffects.WEAKNESS
            BLINDNESS -> MobEffects.BLINDNESS
            SLOWNESS, SLOWNESS_4, SLOWNESS_6 -> MobEffects.MOVEMENT_SLOWDOWN
            NAUSEA -> MobEffects.CONFUSION
            HUNGER -> MobEffects.HUNGER
            CURSED -> MobEffects.UNLUCK
            LEVITATION -> MobEffects.LEVITATION
            MINING_WEAKNESS -> MobEffects.DIG_SLOWDOWN
            WITHER -> MobEffects.WITHER
            else -> return null
        }

        val level = when (this) {
            HASTE_2, REGENERATION_4, SPEED_2, RESISTANCE_2, STRENGTH_2 -> 1
            SPEED_4, POISON_4, SLOWNESS_4 -> 3
            SLOWNESS_6 -> 5
            else -> 0
        }

        return MobEffectInstance(effect, 300, level, true, false, ServerConfig.showEffectIcons.get())
    }

    fun canAddMutation(genes: GenesCapability, syringeGenes: GenesCapability): Boolean {
        return when (this) {
            HASTE_2 -> genes.hasGene(HASTE) || syringeGenes.hasGene(HASTE)
            EFFICIENCY_4 -> genes.hasGene(EFFICIENCY) || syringeGenes.hasGene(EFFICIENCY)
            REGENERATION_4 -> genes.hasGene(REGENERATION) || syringeGenes.hasGene(REGENERATION)
            SPEED_4 -> genes.hasGene(SPEED_2) || syringeGenes.hasGene(SPEED_2)
            SPEED_2 -> genes.hasGene(SPEED) || syringeGenes.hasGene(SPEED)
            RESISTANCE_2 -> genes.hasGene(RESISTANCE) || syringeGenes.hasGene(RESISTANCE)
            STRENGTH_2 -> genes.hasGene(STRENGTH) || syringeGenes.hasGene(STRENGTH)
            MEATY_2 -> genes.hasGene(MEATY) || syringeGenes.hasGene(MEATY)
            MORE_HEARTS_2 -> genes.hasGene(MORE_HEARTS) || syringeGenes.hasGene(MORE_HEARTS)
            INVISIBLE -> true
            FLIGHT -> genes.hasGene(JUMP_BOOST) || genes.hasGene(TELEPORT) || genes.hasGene(NO_FALL_DAMAGE) ||
                    syringeGenes.hasGene(JUMP_BOOST) || syringeGenes.hasGene(TELEPORT) || syringeGenes.hasGene(
                NO_FALL_DAMAGE
            )

            LUCK -> true
            SCARE_ZOMBIES -> genes.hasGene(SCARE_CREEPERS) || syringeGenes.hasGene(SCARE_CREEPERS)
            SCARE_SPIDERS -> genes.hasGene(SCARE_SKELETONS) || syringeGenes.hasGene(SCARE_SKELETONS)
            THORNS -> genes.hasGene(PHOTOSYNTHESIS) || syringeGenes.hasGene(PHOTOSYNTHESIS)
            CLAWS_2 -> genes.hasGene(CLAWS) || syringeGenes.hasGene(CLAWS)
            else -> true
        }
    }
}