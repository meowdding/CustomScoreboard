package me.owdding.customscoreboard.utils.rendering

import com.mojang.blaze3d.systems.RenderSystem
import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import me.owdding.customscoreboard.utils.rendering.alignment.TextAlignment
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import tech.thatgravyboat.skyblockapi.helpers.McFont
import tech.thatgravyboat.skyblockapi.utils.extentions.translated

typealias AlignedText = Pair<Component, TextAlignment>

object RenderUtils {

    fun GuiGraphics.drawAlignedText(text: Component, x: Int, y: Int, width: Int, alignment: TextAlignment, shadow: Boolean = true) {
        val textWidth = McFont.width(text)
        drawString(McFont.self, text, x + alignment.align(textWidth, width), y, -1, shadow)
    }

    fun GuiGraphics.drawAlignedTexts(texts: List<AlignedText>, x: Int, y: Int, shadow: Boolean = true) {
        var currentY = y
        val maxWidth = texts.maxOf { McFont.width(it.first) }
        texts.forEach { text ->
            drawAlignedText(text.first, x, currentY, maxWidth, text.second, shadow)
            currentY += McFont.self.lineHeight
        }
    }

    fun GuiGraphics.drawRec(
        x: Int, y: Int, width: Int, height: Int,
        backgroundColor: Int, borderColor: Int = backgroundColor,
        borderSize: Int = 0, radius: Int = 0,
    ) {
        RoundedRectangle.drawRelative(this, x, y, width, height, backgroundColor, borderColor, borderSize.toFloat(), radius)
    }

    fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: ResourceLocation, radius: Int = 0,
        alpha: Float = 1f,
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
