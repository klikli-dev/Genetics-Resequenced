package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.recipes.DnaExtractorRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class DnaExtractorRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<DnaExtractorRecipe> {

    companion object {

        val recipeType: RecipeType<DnaExtractorRecipe> =
            RecipeType(
                OtherUtil.modResource(DnaExtractorRecipe.RECIPE_TYPE_NAME),
                DnaExtractorRecipe::class.java
            )

        val UID = OtherUtil.modResource(DnaExtractorRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/dna_extractor.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 57, 30, 75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModBlocks.DNA_EXTRACTOR.get()))

    override fun getRecipeType(): RecipeType<DnaExtractorRecipe> = GeneticsResequencedJeiPlugin.DNA_EXTRACTOR_TYPE

    override fun getTitle(): Component = Component.translatable("block.geneticsresequenced.dna_extractor")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DnaExtractorRecipe, focuses: IFocusGroup) {
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