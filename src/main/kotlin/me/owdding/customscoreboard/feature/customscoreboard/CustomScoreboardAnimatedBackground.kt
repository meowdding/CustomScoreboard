package me.owdding.customscoreboard.feature.customscoreboard

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.metadata.IIOMetadataNode

private const val DEFAULT_FRAME_DELAY = 15L
private const val MAX_FRAMES = 100

object CustomScoreboardAnimatedBackground {

    private val maxTextureSize by lazy { RenderSystem.getDevice().maxTextureSize }
    private var index = -1
    private var frameStart = 0L
    private var _frames = mutableListOf<Frame>()

    val frames: List<Frame> get() = _frames
    val frame: Frame? get() {
        val progress = (System.currentTimeMillis()) - frameStart
        val duration = _frames.getOrNull(index)?.duration ?: return null
        if (progress >= duration) {
            frameStart = System.currentTimeMillis()
            index = (index + 1) % _frames.size
        }
        return _frames.getOrNull(index)
    }

    fun load(stream: InputStream) {
        index = 0
        frameStart = System.currentTimeMillis()
        _frames.clear()

        ImageIO.createImageInputStream(stream).use {
            val readers = ImageIO.getImageReaders(it)
            if (readers.hasNext()) {
                val reader = readers.next().apply { this.setInput(it, false) }
                if (reader.formatName.equals("gif", ignoreCase = true)) {
                    val frames = reader.getNumImages(true)

                    if (frames > MAX_FRAMES) {
                        println("Animated background has $frames frames, but only $frames can fit in a single texture. Some frames will be ignored.")
                    } else {
                        val background = reader.createBackground()

                        for (i in 0 until frames) {
                            val metadata = reader.getImageMetadata(i)

                            val node = metadata.getAsTree(metadata.nativeMetadataFormatName) as IIOMetadataNode
                            val descriptor = node.getElementsByTagName("ImageDescriptor").item(0) as IIOMetadataNode
                            val control = node.getElementsByTagName("GraphicControlExtension").item(0) as IIOMetadataNode

                            val width = descriptor.getAttribute("imageWidth").toInt()
                            val height = descriptor.getAttribute("imageHeight").toInt()
                            val x = descriptor.getAttribute("imageLeftPosition").toInt()
                            val y = descriptor.getAttribute("imageTopPosition").toInt()

                            reader.read(i).copyTo(background, x, y, width, height)

                            this._frames.add(Frame(
                                sprite = Identifier.fromNamespaceAndPath("customscoreboard", "dynamic/scoreboard/$i"),
                                image = background.copy(),
                                duration = control.getAttribute("delayTime").toLongOrNull()?.times(10) ?: DEFAULT_FRAME_DELAY
                            ))
                        }
                    }
                }
            }
        }
    }

    private fun ImageReader.createBackground(): NativeImage {
        val global = this.streamMetadata.getAsTree(this.streamMetadata.nativeMetadataFormatName) as IIOMetadataNode
        val screenDescriptor = global.getElementsByTagName("LogicalScreenDescriptor").item(0) as? IIOMetadataNode
        var width = screenDescriptor?.getAttribute("logicalScreenWidth")?.toIntOrNull()
        var height = screenDescriptor?.getAttribute("logicalScreenHeight")?.toIntOrNull()

        if (width == null || height == null) {
            width = this.getWidth(0)
            height = this.getHeight(0)
        }

        check(width <= maxTextureSize && height <= maxTextureSize) {
            "Animated background frame size $width x $height exceeds the maximum texture size of $maxTextureSize."
        }

        return NativeImage(width, height, false)
    }

    private fun NativeImage.copy(): NativeImage {
        val copy = NativeImage(this.format(), this.width, this.height, false)
        copy.copyFrom(this)
        return copy
    }

    private fun BufferedImage.copyTo(native: NativeImage, x: Int, y: Int, width: Int, height: Int) {
        for (j in 0 until height) {
            for (i in 0 until width) {
                val pixel = this.getRGB(i, j)
                if ((pixel ushr 24) == 0) continue

                native.setPixel(x + i, y + j, pixel)
            }
        }
    }

    data class Frame(val sprite: Identifier, val image: NativeImage, val duration: Long) {

        fun load(): DynamicTexture = DynamicTexture(
            { "Custom Scoreboard Animated Background Frame ${sprite.path}" },
            image
        )
    }
}
