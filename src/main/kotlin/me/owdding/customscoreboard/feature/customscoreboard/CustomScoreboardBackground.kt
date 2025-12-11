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

    fun load() {
        runCatching {
            val file = File(BackgroundConfig.customImageFile)
            if (file.exists()) {
                file.inputStream().use { stream ->
                    val image = NativeImage.read(NativeImage.Format.RGBA, stream)
                    McClient.runNextTick {
                        val texture = DynamicTexture({ "Custom Scoreboard Background" }, image)
                        McClient.self.textureManager.register(dynamicTexture, texture)
                    }
                    dynamic = true
                }
            } else {
                dynamic = false
            }
        }.onFailure(Throwable::printStackTrace)
    }

    fun getTexture(): Identifier = dynamicTexture.takeIf { dynamic } ?: texturepackTexture.takeIf { doesTextureExist(it) } ?: skyhanniTexture

    fun doesTextureExist(texture: Identifier): Boolean {
        return McClient.self.resourceManager.getResource(texture).getOrNull()?.let { true } == true
    }
}
