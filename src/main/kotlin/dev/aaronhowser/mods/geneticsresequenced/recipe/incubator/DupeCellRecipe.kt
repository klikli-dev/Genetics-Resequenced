package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class DupeCellRecipe(
    val isGmoCell: Boolean = false
) : AbstractIncubatorRecipe() {

    private val potionIngredient = DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.SUBSTRATE))
    private val cellIngredient = Ingredient.of(if (isGmoCell) ModItems.GMO_CELL.get() else ModItems.CELL.get())

    override val ingredients: List<Ingredient> = listOf(potionIngredient, cellIngredient)

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topStack = input.getTopItem()
        val potionStack = input.getBottomItem()

        if (!potionIngredient.test(potionStack)) return false
        if (!cellIngredient.test(topStack)) return false

        return EntityDnaItem.hasEntity(topStack)
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        val topStack = input.getTopItem()

        val ingredientEntity = EntityDnaItem.getEntityType(topStack) ?: return ItemStack.EMPTY

        val outputCell: ItemStack

        if (isGmoCell) {
            val pIngredientGene = DnaHelixItem.getGeneHolder(topStack) ?: return ItemStack.EMPTY

            outputCell = ModItems.GMO_CELL.toStack()
            GmoCell.setDetails(outputCell, ingredientEntity, pIngredientGene)
        } else {
            outputCell = ModItems.CELL.toStack()
            EntityDnaItem.setEntityType(outputCell, ingredientEntity)
        }

        return outputCell
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return if (isGmoCell) ModItems.GMO_CELL.toStack() else ModItems.CELL.toStack()
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.DUPE_CELL.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.DUPE_CELL.get()
    }

    class Serializer : RecipeSerializer<DupeCellRecipe> {
        override fun codec(): MapCodec<DupeCellRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, DupeCellRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<DupeCellRecipe> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        Codec.BOOL
                            .optionalFieldOf("is_gmo_cell", false)
                            .forGetter(DupeCellRecipe::isGmoCell)
                    ).apply(instance, ::DupeCellRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, DupeCellRecipe> =
                StreamCodec.composite(
                    ByteBufCodecs.BOOL, DupeCellRecipe::isGmoCell,
                    ::DupeCellRecipe
                )
        }
    }

}
