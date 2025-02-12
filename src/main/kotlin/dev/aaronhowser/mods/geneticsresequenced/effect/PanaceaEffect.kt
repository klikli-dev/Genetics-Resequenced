package dev.aaronhowser.mods.geneticsresequenced.effect

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class PanaceaEffect : MobEffect(
    MobEffectCategory.BENEFICIAL,
    0xa83283
) {

    override fun isInstantenous(): Boolean = true

    override fun applyInstantenousEffect(
        pSource: Entity?,
        pIndirectSource: Entity?,
        pLivingEntity: LivingEntity,
        pAmplifier: Int,
        pHealth: Double
    ) {
        removeAllNegativeGenes(pLivingEntity)
        removeAllNegativeEffects(pLivingEntity)

        pLivingEntity.removeEffect(ModEffects.PANACEA)
    }

    private fun removeAllNegativeGenes(pLivingEntity: LivingEntity) {
        val genes = pLivingEntity.geneHolders.filter { it.isNegative }.iterator()

        while (genes.hasNext()) {
            val gene = genes.next()
            pLivingEntity.removeGene(gene)
        }
    }

    private fun removeAllNegativeEffects(pLivingEntity: LivingEntity) {
        val harmfulEffects = pLivingEntity.activeEffects
            .filter { it.effect.value().category == MobEffectCategory.HARMFUL }
            .iterator()

        while (harmfulEffects.hasNext()) {
            val effect = harmfulEffects.next()
            pLivingEntity.removeEffect(effect.effect)
        }
    }

}