package me.owdding.customscoreboard.utils.rendering

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

expect object RenderUtils {
    fun GuiGraphics.drawRec(
        x: Int, y: Int, width: Int, height: Int,
        backgroundColor: Int, borderColor: Int = backgroundColor,
        borderSize: Int = 0, radius: Int = 0,
    )

    fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: ResourceLocation, radius: Int = 0,
        alpha: Float = 1f,
    )
}
