package me.owdding.customscoreboard.utils.rendering

import com.mojang.blaze3d.systems.RenderSystem
import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import tech.thatgravyboat.skyblockapi.utils.extentions.translated

actual object RenderUtils {
    actual fun GuiGraphics.drawRec(
        x: Int, y: Int, width: Int, height: Int,
        backgroundColor: Int, borderColor: Int,
        borderSize: Int, radius: Int,
    ) {
        RoundedRectangle.drawRelative(this, x, y, width, height, backgroundColor, borderColor, radius.toFloat(), borderSize)
    }

    actual fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: ResourceLocation, radius: Int,
        alpha: Float,
    ) {
        val xOffset = this.pose().last().pose().m30()
        val yOffset = this.pose().last().pose().m31()
        translated(-xOffset, -yOffset) {
            RenderSystem.setShaderColor(1f, 1f, 1f, alpha)
            RoundedTexture.draw(
                this@drawTexture, (x + xOffset).toInt(), (y + yOffset).toInt(), width, height,
                texture, 0f, 0f, 1f, 1f, radius.toFloat(),
            )
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        }
    }
}
