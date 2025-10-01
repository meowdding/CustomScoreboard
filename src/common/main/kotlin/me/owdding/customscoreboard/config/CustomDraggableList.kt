package me.owdding.customscoreboard.config

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigElementRenderer
import com.teamresourceful.resourcefulconfig.api.client.options.ResourcefulConfigOptionUI
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry
import com.teamresourceful.resourcefulconfig.api.types.info.TooltipProvider
import com.teamresourceful.resourcefulconfig.api.types.options.data.DraggableOptionEntry
import com.teamresourceful.resourcefulconfig.client.UIConstants
import com.teamresourceful.resourcefulconfig.client.components.ModSprites
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.customscoreboard.utils.ElementGroup
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import tech.thatgravyboat.skyblockapi.utils.text.Text


val CUSTOM_DRAGGABLE_RENDERER = Main.id("custom_draggable_list")

class CustomDraggableList(val element: ResourcefulConfigElement) : ResourcefulConfigElementRenderer {
    val entry get() = (element as ResourcefulConfigEntryElement).entry() as ResourcefulConfigValueEntry

    override fun title(): MutableComponent = entry.options().title().toComponent()
    override fun description(): MutableComponent = entry.options().comment().toComponent()

    override fun widgets(): List<AbstractWidget> {
        return listOf(
            ResourcefulConfigOptionUI.draggable(
                entry.options().title().toComponent(),
                Main.allPossibleScoreboardElements.map { it.toDraggableOptionEntry() }.sortedBy { it.value().group.ordinal },
                {
                    (entry.array as Array<String>).asList().toBaseElements()
                },
                {
                    entry.array = it.toConfigStrings()
                },
            ),
            SpriteButton.builder(12, 12)
                .padding(2)
                .sprite(ModSprites.RESET)
                .tooltip(UIConstants.RESET)
                .onPress { entry.reset() }
                .build(),
        )
    }

    companion object {
        fun List<String>.toBaseElements(): List<Element> = mapNotNull { id ->
            Main.allPossibleScoreboardElements.find { it.id == id }
        }

        fun List<BaseElement>.toConfigStrings(): Array<String> = map { it.id }.toTypedArray()

        fun BaseElement.toDraggableOptionEntry() = DraggableOptionEntry(this, this.canDuplicate)
    }
}

interface BaseElement : TooltipProvider {
    val id: String
    val configLineHover get() = listOf<String>()
    val canDuplicate: Boolean get() = false
    val group: ElementGroup get() = ElementGroup.MIDDLE

    override fun getTooltip(): Component = Text.multiline(this.configLineHover)
}
