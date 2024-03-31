package dev.aaronhowser.mods.geneticsresequenced.api.capability

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

object CapabilityHandler {

    val GENE_CAPABILITY_RL: ResourceLocation = ResourceLocation(GeneticsResequenced.ID, "genes")


}