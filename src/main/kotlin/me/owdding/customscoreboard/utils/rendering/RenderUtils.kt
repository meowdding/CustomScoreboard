package me.owdding.customscoreboard.utils.rendering

import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB

object RenderUtils {
    fun GuiGraphics.drawRec(
        x: Int, y: Int, width: Int, height: Int,
        backgroundColor: Int, borderColor: Int = backgroundColor,
        borderSize: Int = 0, radius: Int = 0,
    ) {
        RoundedRectangle.draw(this, x, y, width, height, backgroundColor, borderColor, radius.toFloat(), borderSize)
    }

    fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: Identifier, radius: Int = 0,
        alpha: Float = 1f,
    ) {
        RoundedTexture.draw(
            this@drawTexture, x, y, width, height,
            texture, 0f, 0f, 1f, 1f, radius.toFloat(),
            ARGB.color((alpha * 255).toInt(), 255, 255, 255),
        )
    }
}
