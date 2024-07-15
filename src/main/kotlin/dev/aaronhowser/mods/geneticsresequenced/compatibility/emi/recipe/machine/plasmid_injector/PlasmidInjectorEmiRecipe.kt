package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.crafting.Ingredient

class PlasmidInjectorEmiRecipe(
    val gene: Gene,
    val isMetal: Boolean,
    val isAntiPlasmid: Boolean
) : EmiRecipe {

    private val plasmid: EmiIngredient
    private val syringeBefore: EmiIngredient
    private val syringeAfter: EmiStack

    init {
        val plasmidStack = if (isAntiPlasmid) ModItems.ANTI_PLASMID.toStack() else ModItems.PLASMID.toStack()
        PlasmidItem.setGene(plasmidStack, gene, gene.dnaPointsRequired)
        plasmid = EmiIngredient.of(Ingredient.of(plasmidStack))

        val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

        val localPlayer = ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")

        val entity = if (isMetal) {
            Cow(EntityType.COW, localPlayer.level())
        } else {
            localPlayer
        }

        SyringeItem.setEntity(syringeStack, entity, setContaminated = false)
        syringeBefore = EmiIngredient.of(Ingredient.of(syringeStack))

        if (isAntiPlasmid) {
            SyringeItem.addAntigene(syringeStack, gene)
        } else {
            SyringeItem.addGene(syringeStack, gene)
        }

        syringeAfter = EmiStack.of(syringeStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.PLASMID_INJECTOR_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val geneString = gene.id.toString().replace(':', '/')
        val syringeString = if (isMetal) "/metal" else ""
        val plasmidString = if (isAntiPlasmid) "/anti" else ""

        return OtherUtil.modResource("/plasmid_injector/$geneString$syringeString$plasmidString")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(plasmid, syringeBefore)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(syringeAfter)
    }

    override fun getDisplayWidth(): Int {
        return 102
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW,  48, 1)
        widgets.addSlot(plasmid, 0, 0)
        widgets.addSlot(syringeBefore, 24, 0)
        widgets.addSlot(syringeAfter, 78, 0).recipeContext(this);
    }
}