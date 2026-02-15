package me.owdding.customscoreboard.feature.customscoreboard

import com.mojang.blaze3d.platform.NativeImage
import me.owdding.customscoreboard.config.categories.BackgroundConfig
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import tech.thatgravyboat.skyblockapi.helpers.McClient
import java.io.File
import kotlin.jvm.optionals.getOrNull

object CustomScoreboardBackground {

    private val texturepackTexture = Identifier.fromNamespaceAndPath("customscoreboard", "scoreboard.png")
    private val skyhanniTexture = Identifier.fromNamespaceAndPath("skyhanni", "scoreboard.png")
    private val dynamicTexture = Identifier.fromNamespaceAndPath("customscoreboard", "dynamic/scoreboard")

    private var dynamic = false
    private var animated = false

    fun load() {
        runCatching {
            val path = BackgroundConfig.customImageFile
            val file = File(path)
            if (path.isNotBlank() && file.exists() && file.isFile) {
                val isGif = file.extension.equals("gif", ignoreCase = true)
                file.inputStream().use { stream ->
                    if (isGif) {
                        this.animated = true
                        this.dynamic = false
                        CustomScoreboardAnimatedBackground.load(stream)

                        McClient.runNextTick {
                            for (frame in CustomScoreboardAnimatedBackground.frames) {
                                McClient.self.textureManager.register(frame.sprite, frame.load())
                            }
                        }
                    } else {
                        this.animated = false
                        this.dynamic = true

                        val image = NativeImage.read(NativeImage.Format.RGBA, stream)
                        McClient.runNextTick {
                            val texture = DynamicTexture({ "Custom Scoreboard Background" }, image)
                            McClient.self.textureManager.register(dynamicTexture, texture)
                        }
                    }
                }
            } else {
                this.dynamic = false
                this.animated = false
            }
        }.onFailure(Throwable::printStackTrace)
    }

    fun getTexture(): Identifier {
        if (animated) {
            val frame = CustomScoreboardAnimatedBackground.frame
            if (frame != null) {
                return frame.sprite
            }
        }

        return when {
            dynamic -> dynamicTexture
            doesTextureExist(texturepackTexture) -> texturepackTexture
            else -> skyhanniTexture
        }
    }

    fun doesTextureExist(texture: Identifier): Boolean = McClient.self.resourceManager.getResource(texture).getOrNull() != null
}
