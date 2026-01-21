package me.owdding.customscoreboard.utils.rendering

import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB

object RenderUtils {
    fun GuiGraphics.drawRec(x: Int, y: Int, width: Int, height: Int) {
        RoundedRectangle.draw(
            this, x, y, width, height,
            BackgroundConfig.backgroundColor, BackgroundConfig.backgroundColor,
            BackgroundConfig.radius.toFloat(), 0,
        )
    }

    fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: Identifier, alpha: Float = 1f,
    ) {
        RoundedTexture.draw(
            this@drawTexture, x, y, width, height,
            texture, 0f, 0f, 1f, 1f, BackgroundConfig.radius.toFloat(),
            ARGB.color((alpha * 255).toInt(), 255, 255, 255),
        )
    }
}
