package me.owdding.customscoreboard.config

import me.owdding.ktmodules.Module
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

@Module
object CustomScoreboardKeybinds {
    val TOGGLE_KEY: KeyMapping = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "customscoreboard.keybinds.toggle",
            GLFW.GLFW_KEY_I,
            "customscoreboard",
        ),
    )
}
