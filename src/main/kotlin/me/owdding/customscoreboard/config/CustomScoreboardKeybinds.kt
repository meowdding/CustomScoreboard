package me.owdding.customscoreboard.config

import com.mojang.blaze3d.platform.InputConstants
import me.owdding.ktmodules.Module
import me.owdding.lib.utils.MeowddingKeybind
import net.minecraft.resources.Identifier

@Module
object CustomScoreboardKeybinds {
    val TOGGLE_KEY = CustomScoreboardKeybind(
        "customscoreboard.keybinds.toggle",
        InputConstants.UNKNOWN.value,
    )
}

// shouldve been an open class instead of abstract
class CustomScoreboardKeybind(
    translationKey: String,
    keyCode: Int,
    allowMultipleExecutions: Boolean = false,
    runnable: (() -> Unit)? = null,
) : MeowddingKeybind(Identifier.fromNamespaceAndPath("customscoreboard", "main"), translationKey, keyCode, allowMultipleExecutions, runnable)
