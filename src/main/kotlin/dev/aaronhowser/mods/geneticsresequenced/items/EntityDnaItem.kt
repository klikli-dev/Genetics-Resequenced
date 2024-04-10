package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class EntityDnaItem() : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
) {

    companion object {
        private const val MOB_ID_NBT = "MobId"

        fun setMob(itemStack: ItemStack, entityType: EntityType<*>): Boolean {
            val mobId = ForgeRegistries.ENTITY_TYPES.getKey(entityType)
            if (mobId == null) {
                GeneticsResequenced.LOGGER.error("Failed to get mob id for $entityType")
                return false
            }

            val tag = itemStack.orCreateTag
            tag.putString(MOB_ID_NBT, mobId.toString())

            return true
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            val string = itemStack.tag?.getString(MOB_ID_NBT) ?: return null
            val resourceLocation = ResourceLocation.tryParse(string) ?: return null

            return ForgeRegistries.ENTITY_TYPES.getValue(resourceLocation)
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {

        val entityType = getEntityType(pStack)
        if (entityType != null) {
            val component =
                Component
                    .translatable("tooltip.geneticsresequenced.organic_matter")
                    .append(entityType.getDescription())
                    .withStyle { it.withColor(ChatFormatting.GRAY) }

            pTooltipComponents.add(component)
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

}