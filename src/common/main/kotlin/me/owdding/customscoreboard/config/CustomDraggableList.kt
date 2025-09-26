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
import me.owdding.customscoreboard.utils.ElementGroup
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import tech.thatgravyboat.skyblockapi.utils.text.Text


val CUSTOM_DRAGGABLE_RENDERER = ResourceLocation.fromNamespaceAndPath("customscoreboard", "custom_draggable_list")

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
                    entry.string.toStupidInterfaceList()
                },
                {
                    entry.string = it.toConfigString()
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
        fun String.toStupidInterfaceList() = split(",").mapNotNull { id ->
            Main.allPossibleScoreboardElements.find { it.id == id }
        }

        fun List<BaseElement>.toConfigString() = joinToString(",") { it.id }

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
