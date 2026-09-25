package me.owdding.customscoreboard.compat

import me.owdding.customscoreboard.mixins.compat.SkyHanniTabListRendererMixin
import tech.thatgravyboat.skyblockapi.helpers.McClient

object TablistCompat {

    //~ if >= 26.2 'gui' -> 'hud'
    private val isVanillaTabRendering get() = McClient.hud.tabList.visible

    private val isSkyHanniTabRendering get() = SkyHanniTabListRendererMixin.`customscoreboard$isTabToggled`()
    private val isSkyHanniTogglePressed get() = SkyHanniTabListRendererMixin.`customscoreboard$isPressed`()

    val isAnyTabRendering: Boolean
        get() = (isVanillaTabRendering && !isSkyHanniTogglePressed) || isSkyHanniTabRendering
}
