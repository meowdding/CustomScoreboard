package me.owdding.customscoreboard.feature.customscoreboard

import com.teamresourceful.resourcefulconfig.client.components.options.misc.draggable.DraggableList
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.categories.CustomizationConfig
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import net.minecraft.client.gui.screens.Screen
import tech.thatgravyboat.skyblockapi.helpers.McFont
import tech.thatgravyboat.skyblockapi.utils.text.Text

class CustomScoreboardEditor : Screen(Text.of("Custom Scoreboard Editor")) {

    override fun init() {
        val maxWidth = CustomizationConfig.appearance.maxOf { McFont.width(it.toString()) }
        val list = DraggableList<Element>(0, 0, maxWidth, 100).apply {
            addAll(CustomizationConfig.appearance.toList())
        }
        list.setOnUpdate {
            CustomizationConfig.appearance = it.toList()
            Main.config.save()
        }
        list.setPosition(10, 10)
        this.addRenderableWidget(list)
    }

    override fun onClose() {
        super.onClose()
        Main.config.save()
    }


}
