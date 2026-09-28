package me.owdding.customscoreboard.core

import com.mojang.blaze3d.platform.NativeImage
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.config.category.BackgroundConfig
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import tech.thatgravyboat.skyblockapi.helpers.McClient
import java.io.File
import kotlin.io.path.relativeTo
import kotlin.jvm.optionals.getOrNull

object CustomScoreboardBackground {

    private val texturepackTexture = Identifier.fromNamespaceAndPath("customscoreboard", "scoreboard.png")
    private val skyhanniTexture = Identifier.fromNamespaceAndPath("skyhanni", "scoreboard.png")
    private val dynamicTexture = Identifier.fromNamespaceAndPath("customscoreboard", "dynamic/scoreboard")

    private val configFolderFilePng = McClient.config.resolve("customscoreboard/scoreboard.png").toFile()
    private val configFolderFileGif = McClient.config.resolve("customscoreboard/scoreboard.gif").toFile()

    private var dynamic = false
    private var animated = false

    fun load() {
        runCatching {
            CustomScoreboardMod.info("Loading CustomScoreboard background...")

            val file = BackgroundConfig.customImageFile.takeUnless(String::isEmpty)?.let(::File)?.takeIf(File::exists)
                ?: configFolderFileGif.takeIf(File::exists)
                ?: configFolderFilePng.takeIf(File::exists)

            if (file != null && file.isFile) {
                val isGif = file.extension.equals("gif", ignoreCase = true)
                CustomScoreboardMod.info("Found background file: ${file.toPath().normalize().relativeTo(McClient.self.gameDirectory.toPath())} (GIF: $isGif)")

                file.inputStream().use { stream ->
                    if (isGif) {
                        this.animated = true
                        this.dynamic = false
                        CustomScoreboardAnimatedBackground.load(stream)

                        McClient.runNextTick {
                            CustomScoreboardMod.info("Registering ${CustomScoreboardAnimatedBackground.frames.size} animated frames for background.")
                            for (frame in CustomScoreboardAnimatedBackground.frames) {
                                McClient.self.textureManager.register(frame.sprite, frame.load())
                            }
                        }
                    } else {
                        this.animated = false
                        this.dynamic = true

                        val image = NativeImage.read(NativeImage.Format.RGBA, stream)
                        McClient.runNextTick {
                            CustomScoreboardMod.info("Registering dynamic texture for background.")
                            val texture = DynamicTexture({ "Custom Scoreboard Background" }, image)
                            McClient.self.textureManager.register(dynamicTexture, texture)
                        }
                    }
                }
            } else {
                CustomScoreboardMod.info("No custom background file found.")
                this.dynamic = false
                this.animated = false
            }
        }.onFailure {
            CustomScoreboardMod.error("Failed to load CustomScoreboard background", it)
            it.printStackTrace()
        }
    }

    fun getTexture(): Identifier {
        if (animated) {
            val frame = CustomScoreboardAnimatedBackground.frame
            if (frame != null) return frame.sprite
        }

        return when {
            dynamic -> dynamicTexture
            doesTextureExist(texturepackTexture) -> texturepackTexture
            else -> skyhanniTexture
        }
    }

    fun doesTextureExist(texture: Identifier): Boolean = McClient.self.resourceManager.getResource(texture).getOrNull() != null
}
