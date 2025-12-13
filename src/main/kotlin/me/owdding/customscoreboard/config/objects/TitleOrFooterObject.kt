package me.owdding.customscoreboard.config.objects

import me.owdding.customscoreboard.feature.ShTransferableObject
import me.owdding.lib.displays.Alignment
import tech.thatgravyboat.skyblockapi.utils.extentions.valueOfOrNull

class TitleOrFooterObject(val type: String) : ShTransferableObject() {

    val alignment by enum(Alignment.CENTER) {
        this.name = Translated("customscoreboard.config.title_footer.alignment")
        this.description = Translated("customscoreboard.config.title_footer.alignment.desc")
        this.shPath = "display.titleAndFooter.align$type"
        this.shMapper = { valueOfOrNull<Alignment>(it.asString) ?: Alignment.CENTER }
    }

    val useCustomText by boolean("use_custom_text", false) {
        this.name = Translated("customscoreboard.config.title_footer.use_custom_text")
        this.description = Translated("customscoreboard.config.title_footer.use_custom_text.desc")
        this.shPath = "display.titleAndFooter.useCustom$type"
    }

    val text by string("custom_text", "") {
        this.name = Translated("customscoreboard.config.title_footer.custom_text")
        this.description = Translated("customscoreboard.config.title_footer.custom_text.desc")
        this.shPath = "display.titleAndFooter.custom$type"
    }
}
