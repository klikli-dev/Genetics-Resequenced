package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class GeneListItemComponent(
    val genes: Set<Holder<Gene>>
) {
    constructor(collection: Collection<Holder<Gene>>) : this(collection.toSet())

    companion object {

        val CODEC: Codec<GeneListItemComponent> = Gene.CODEC.listOf().xmap(
            { GeneListItemComponent(it) },
            { it.genes.toList() })

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneListItemComponent> = ByteBufCodecs.fromCodec(CODEC)
    }

}