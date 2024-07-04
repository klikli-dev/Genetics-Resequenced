package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.dna_extractor

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class CellToHelixEmiRecipe(
    val entityType: EntityType<*>
) : EmiRecipe {

    private val cell: EmiIngredient
    private val helix: EmiStack

    init {
        val cellStack = ModItems.CELL.toStack()
        EntityDnaItem.setEntityType(cellStack, entityType)
        cell = EmiIngredient.of(DataComponentIngredient.of(false, cellStack))

        val helixStack = ModItems.DNA_HELIX.toStack()
        EntityDnaItem.setEntityType(helixStack, entityType)
        helix = EmiStack.of(helixStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.DNA_EXTRACTOR_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val entityTypeRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        val entityString = entityTypeRl.toString().replace(':', '/')

        return OtherUtil.modResource("/dna_extractor/$entityString")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(cell)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(helix)
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(cell, 0, 0)
        widgets.addSlot(helix, 58, 0).recipeContext(this);
    }
}