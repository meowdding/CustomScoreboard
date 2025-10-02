package me.owdding.customscoreboard.config

import me.owdding.ktmodules.Module
import me.owdding.lib.utils.MeowddingKeybind
import net.minecraft.resources.ResourceLocation
import org.lwjgl.glfw.GLFW

@Module
object CustomScoreboardKeybinds {
    val TOGGLE_KEY = CustomScoreboardKeybind(
        "customscoreboard.keybinds.toggle",
        GLFW.GLFW_KEY_I,
    )
}

// shouldve been an open class instead of abstract
class CustomScoreboardKeybind(
    translationKey: String,
    keyCode: Int,
    allowMultipleExecutions: Boolean = false,
    runnable: (() -> Unit)? = null,
) : MeowddingKeybind(ResourceLocation.fromNamespaceAndPath("customscoreboard", "main"), translationKey, keyCode, allowMultipleExecutions, runnable)
