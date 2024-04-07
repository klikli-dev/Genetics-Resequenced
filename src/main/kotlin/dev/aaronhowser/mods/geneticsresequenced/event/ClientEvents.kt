package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.controls.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.packet.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.TeleportPlayerPacket
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    value = [Dist.CLIENT]
)
object ClientEvents {

    @SubscribeEvent
    fun onKeyInputEvent(event: InputEvent.Key) {
        if (ModKeyMappings.TELEPORT.consumeClick()) {
            ModPacketHandler.messageServer(TeleportPlayerPacket())
        }

        if (ModKeyMappings.DRAGONS_BREATH.consumeClick()) {
            ModPacketHandler.messageServer(FireballPacket())
        }
    }
}