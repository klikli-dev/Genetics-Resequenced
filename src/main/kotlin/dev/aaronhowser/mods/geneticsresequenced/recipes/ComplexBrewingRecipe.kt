package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraftforge.common.brewing.IBrewingRecipe

class ComplexBrewingRecipe : IBrewingRecipe {

    private val inputPotion: Potion
    private val ingredient: Item
    private val output: ItemStack

    private var inputGene: Gene? = null
    private var inputCellEntity: EntityType<*>? = null

    constructor(
        inputPotion: Potion,
        ingredientItem: Item,
        gene: Gene?,
        outputStack: ItemStack
    ) {
        this.inputPotion = inputPotion
        this.ingredient = ingredientItem
        this.inputGene = gene
        this.output = outputStack
    }

    constructor(
        inputPotion: Potion,
        ingredientItem: Item,
        cellEntity: EntityType<*>,
        geneOutput: Gene
    ) {
        this.inputPotion = inputPotion
        this.ingredient = ingredientItem
        this.inputCellEntity = cellEntity
        this.inputGene = geneOutput

        val outputCell = ItemStack(ModItems.CELL.get())
        EntityDnaItem.setMob(outputCell, cellEntity)
        this.output = outputCell
    }

    override fun isInput(pInput: ItemStack): Boolean {
        val pInputPotion = PotionUtils.getPotion(pInput)

        if (inputPotion == Potions.EMPTY || pInputPotion != inputPotion) return false

        if (pInputPotion == ModPotions.SUBSTRATE) return true

        when (pInput.item) {
            ModItems.CELL.get() -> return handleCellInput(pInput, pInputPotion)

            ModItems.DNA_HELIX.get() -> return handleDnaHelix(pInput, pInputPotion)
        }

        return pInputPotion == inputPotion
    }

    private fun handleCellInput(pInput: ItemStack): Boolean {
        if (inputCellEntity == null) return false
        val pInputEntity = EntityDnaItem.getEntityType(pInput) ?: return false



    }

    private fun handleDnaHelix(pInput: ItemStack, pInputPotion: Potion): Boolean {
        val pInputGene = pInput.getGene()

        if (inputGene != null) {
            val pInputIsViral = pInputPotion == ModPotions.VIRAL_AGENTS
            if (pInputIsViral) return true
        }

        return inputGene == pInputGene
    }

    private val requiredGenes = setOf(
        DefaultGenes.CURSED, DefaultGenes.POISON_4, DefaultGenes.WITHER, DefaultGenes.WEAKNESS,
        DefaultGenes.BLINDNESS, DefaultGenes.SLOWNESS_6, DefaultGenes.NAUSEA, DefaultGenes.HUNGER,
        DefaultGenes.FLAMBE, DefaultGenes.MINING_WEAKNESS, DefaultGenes.LEVITATION,
        DefaultGenes.DEAD_CREEPERS, DefaultGenes.DEAD_UNDEAD, DefaultGenes.DEAD_HOSTILE, DefaultGenes.DEAD_OLD_AGE
    )

    override fun isIngredient(pIngredient: ItemStack): Boolean {
        return pIngredient.item == ingredient
    }

    override fun getOutput(pInput: ItemStack, pIngredient: ItemStack): ItemStack {
        return output
    }
}