package me.owdding.customscoreboard.compat

import me.owdding.customscoreboard.mixins.compat.SkyHanniTabListRendererMixin
import me.owdding.lib.utils.KnownMods
import net.minecraft.client.Minecraft
import tech.thatgravyboat.skyblockapi.helpers.McClient

object TablistCompat {

    private val isVanillaTabRendering get() = McClient.hud.tabList.visible

    private val isSkyHanniTabRendering get() = KnownMods.SKYHANNI.installed && SkyHanniTabListRendererMixin.`customscoreboard$isTabToggled`()
    private val isSkyHanniTogglePressed get() = KnownMods.SKYHANNI.installed && SkyHanniTabListRendererMixin.`customscoreboard$isPressed`()

    @JvmStatic
    var skyblockerTabRendering: Boolean = false
    private val isSkyblockerTabRendering get() = KnownMods.SKYBLOCKER.installed && skyblockerTabRendering && McClient.options.keyPlayerList.isDown;

    val isAnyTabRendering: Boolean
        get() = (isVanillaTabRendering && !isSkyHanniTogglePressed) || isSkyHanniTabRendering || isSkyblockerTabRendering
}
