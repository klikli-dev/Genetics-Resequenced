package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class AdvancedIncubatorScreen(
    pMenu: AdvancedIncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<AdvancedIncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        private const val FAST_BUBBLE_SPEED = 12
        private const val SLOW_BUBBLE_SPEED = 12 * 3
    }

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.INCUBATOR

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY)

        renderHeat(pGuiGraphics, leftPos, topPos)
        renderBubble(pGuiGraphics, leftPos, topPos)
    }

    override fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int): Boolean {
        if (isMouseOverTemperature(pMouseX.toInt(), pMouseY.toInt(), leftPos, topPos)) {
            this.minecraft?.gameMode?.handleInventoryButtonClick(this.menu.containerId, 1)
            bubblePosProgress = 0
            bubblePos = 0
            return true
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton)
    }

    private fun isMouseOverTemperature(mouseX: Int, mouseY: Int, x: Int, y: Int): Boolean {
        return isMouseOver(
            mouseX, mouseY,
            x, y,
            ScreenTextures.Elements.Heat.Position.X,
            ScreenTextures.Elements.Heat.Position.Y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Incubator.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Incubator.Y

    //FIXME: Make it fill up instead of emptying
    override val arrowTexture: ResourceLocation = ScreenTextures.Elements.ArrowDown.TEXTURE
    override val arrowTextureSize: Int = ScreenTextures.Elements.ArrowDown.TEXTURE_SIZE
    override val arrowPosLeft: Int = ScreenTextures.Elements.ArrowDown.Position.X
    override val arrowPosTop: Int = ScreenTextures.Elements.ArrowDown.Position.Y
    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
    override fun progressArrowWidth(): Int = ScreenTextures.Elements.ArrowDown.Dimensions.WIDTH
    override fun progressArrowHeight(): Int = menu.getScaledProgress()

    private var bubblePosProgress = 0
    private var bubblePos = 0
        set(value) {
            field = value

            val amountOverMax = bubblePos - ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT
            if (amountOverMax > 0) {
                field = amountOverMax
            }
        }

    //FIXME: Make the bubbles go up instead of down
    private fun renderBubble(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isCrafting) return

        val speed = if (menu.isHighTemperature) FAST_BUBBLE_SPEED else SLOW_BUBBLE_SPEED

        bubblePosProgress++
        if (bubblePosProgress % speed == 0) {
            bubblePos++
            bubblePosProgress = 0
        }

        val amountBubbleToRender = ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT - bubblePos

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Bubbles.TEXTURE,
            ScreenTextures.Elements.Bubbles.TEXTURE_SIZE,
            ScreenTextures.Elements.Bubbles.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.Bubbles.Position.X,
            y + ScreenTextures.Elements.Bubbles.Position.Y,
            ScreenTextures.Elements.Bubbles.Dimensions.WIDTH,
            amountBubbleToRender
        )
    }

    private fun renderHeat(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val hasEnergy = menu.blockEntity.energyStorage.energyStored != 0
        if (!hasEnergy) return

        val texture =
            if (menu.isHighTemperature) ScreenTextures.Elements.Heat.Texture.HIGH else ScreenTextures.Elements.Heat.Texture.LOW

        pGuiGraphics.blitSprite(
            texture,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            0, 0,
            x + ScreenTextures.Elements.Heat.Position.X,
            y + ScreenTextures.Elements.Heat.Position.Y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )
    }
}