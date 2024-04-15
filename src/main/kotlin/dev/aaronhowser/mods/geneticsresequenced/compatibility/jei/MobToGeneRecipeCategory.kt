package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.MobToGeneRecipe
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.drawable.IDrawableStatic
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class MobToGeneRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<MobToGeneRecipe> {

    companion object {
        val recipeType: RecipeType<MobToGeneRecipe> =
            RecipeType(
                ResourceLocation(GeneticsResequenced.ID, MobToGeneRecipe.RECIPE_TYPE_NAME),
                MobToGeneRecipe::class.java
            )

        val UID = ResourceLocation(GeneticsResequenced.ID, MobToGeneRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = ResourceLocation(GeneticsResequenced.ID, "textures/gui/dna_decryptor.png")
    }

    private val background: IDrawableStatic = helper.createDrawable(TEXTURE, 57, 30, 75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModItems.DNA_HELIX))

    override fun getRecipeType(): RecipeType<MobToGeneRecipe> = GeneticsResequencedJeiPlugin.MOB_TO_GENE_TYPE

    override fun getTitle(): Component = Component.translatable("block.geneticsresequenced.dna_decryptor")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: MobToGeneRecipe, focuses: IFocusGroup) {
        builder.addSlot(
            RecipeIngredientRole.INPUT,
            6,
            6
        ).addIngredients(recipe.ingredients.first())

        builder.addSlot(
            RecipeIngredientRole.OUTPUT,
            53,
            6
        ).addItemStack(recipe.resultItem)
    }
}