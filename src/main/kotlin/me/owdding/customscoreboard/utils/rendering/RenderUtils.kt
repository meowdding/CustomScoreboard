package me.owdding.customscoreboard.utils.rendering

//? 1.21.5
/*import com.mojang.blaze3d.systems.RenderSystem*/
import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
//? 1.21.5
/*import tech.thatgravyboat.skyblockapi.utils.extentions.translated*/

object RenderUtils {
    fun GuiGraphics.drawRec(
        x: Int, y: Int, width: Int, height: Int,
        backgroundColor: Int, borderColor: Int = backgroundColor,
        borderSize: Int = 0, radius: Int = 0,
    ) {
        //? > 1.21.5 {
        RoundedRectangle.draw(this, x, y, width, height, backgroundColor, borderColor, radius.toFloat(), borderSize)
        //?} else
        /*RoundedRectangle.drawRelative(this, x, y, width, height, backgroundColor, borderColor, radius.toFloat(), borderSize)*/
    }

    fun GuiGraphics.drawTexture(
        x: Int, y: Int, width: Int, height: Int,
        texture: Identifier, radius: Int = 0,
        alpha: Float = 1f,
    ) {
        //? > 1.21.5 {
        RoundedTexture.draw(
            this@drawTexture, x, y, width, height,
            texture, 0f, 0f, 1f, 1f, radius.toFloat(),
            ARGB.color((alpha * 255).toInt(), 255, 255, 255),
        )
        //?} else {
        /*val xOffset = this.pose().last().pose().m30()
        val yOffset = this.pose().last().pose().m31()
        translated(-xOffset, -yOffset) {
            RenderSystem.setShaderColor(1f, 1f, 1f, alpha)
            RoundedTexture.draw(
                this@drawTexture, (x + xOffset).toInt(), (y + yOffset).toInt(), width, height,
                texture, 0f, 0f, 1f, 1f, radius.toFloat(),
            )
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        }
        *///?}
    }
}
