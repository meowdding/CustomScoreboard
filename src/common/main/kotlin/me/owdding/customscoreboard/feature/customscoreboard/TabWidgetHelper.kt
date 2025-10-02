package me.owdding.customscoreboard.feature.customscoreboard

import me.owdding.customscoreboard.config.MainConfig
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.utils.extentions.toTitleCase

@Module
object TabWidgetHelper {

    var tablistLineCache: List<Element> = listOf()
        private set
    val tabWidgetCache = mutableMapOf<TabWidget, List<Component>>()

    fun updateTablistLineCache() {
        tablistLineCache = MainConfig.tablistLines.map {
            object : Element() {
                override fun getDisplay(): List<Component>? = tabWidgetCache[it]

                override val configLine: String get() = "Tablist - ${it.name.toTitleCase()}"
                override val id: String = "TABLIST_${it.name}"
            }
        }
    }

    @Subscription
    fun onTabWidget(event: TabWidgetChangeEvent) {
        tabWidgetCache[event.widget] = event.newComponents
    }

    @Subscription
    fun onWorldSwitch(event: ServerChangeEvent) = tabWidgetCache.clear()


}
