package me.owdding.customscoreboard.config.objects

import com.teamresourceful.resourcefulconfigkt.api.ObjectKt
import me.owdding.lib.displays.Alignment

class TitleOrFooterObject : ObjectKt() {
    val alignment by enum(Alignment.CENTER) {
        this.name = Translated("customscoreboard.config.title_footer.alignment")
        this.description = Translated("customscoreboard.config.title_footer.alignment.desc")
    }

    val useCustomText by boolean("use_custom_text", false) {
        this.name = Translated("customscoreboard.config.title_footer.use_custom_text")
        this.description = Translated("customscoreboard.config.title_footer.use_custom_text.desc")
    }

    val text by string("custom_text", "") {
        this.name = Translated("customscoreboard.config.title_footer.custom_text")
        this.description = Translated("customscoreboard.config.title_footer.custom_text.desc")
    }
}
