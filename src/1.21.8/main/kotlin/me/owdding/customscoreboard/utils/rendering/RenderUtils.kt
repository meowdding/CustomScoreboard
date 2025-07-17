package me.owdding.customscoreboard.utils.rendering

import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ARGB

actual object RenderUtils {
    actual fun GuiGraphics.drawRec(
        x: Int, y: Int, width: Int, height: Int,
        backgroundColor: Int, borderColor: Int,
        borderSize: Int, radius: Int,
    ) {
        RoundedRectangle.draw(this, x, y, width, height, backgroundColor, borderColor, radius.toFloat(), borderSize)
    }

    actual fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: ResourceLocation, radius: Int,
        alpha: Float,
    ) {
        RoundedTexture.draw(
            this@drawTexture, x, y, width, height,
            texture, 0f, 0f, 1f, 1f, radius.toFloat(),
            ARGB.color((alpha * 255).toInt(), 255, 255, 255),
        )
    }
}
