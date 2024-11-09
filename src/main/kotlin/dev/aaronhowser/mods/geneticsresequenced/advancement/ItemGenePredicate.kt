package dev.aaronhowser.mods.geneticsresequenced.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.advancements.critereon.ItemSubPredicate
import net.minecraft.advancements.critereon.SingleComponentItemPredicate
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import java.util.*

/**
 * Predicate for checking if an item has a specific gene.
 *
 * An empty optional means that it will return true if the stack has any gene.
 */
class ItemGenePredicate(
    val gene: Optional<Holder<Gene>>
) : SingleComponentItemPredicate<Holder<Gene>> {

    override fun matches(stack: ItemStack, value: Holder<Gene>): Boolean {
        if (gene.isEmpty) return DnaHelixItem.hasGene(stack)

        val stackGene = DnaHelixItem.getGeneHolder(stack) ?: return false
        return stackGene.isGene(value)
    }

    override fun componentType(): DataComponentType<Holder<Gene>> {
        return ModDataComponents.GENE_COMPONENT.get()
    }

    companion object {
        fun any() = ItemGenePredicate(Optional.empty())

        val CODEC: Codec<ItemGenePredicate> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Gene.CODEC
                        .optionalFieldOf("gene")
                        .forGetter(ItemGenePredicate::gene),
                ).apply(instance, ::ItemGenePredicate)
            }

        val TYPE: ItemSubPredicate.Type<ItemGenePredicate> = ItemSubPredicate.Type(CODEC)
    }

}