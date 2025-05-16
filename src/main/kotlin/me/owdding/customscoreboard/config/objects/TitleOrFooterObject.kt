package me.owdding.customscoreboard.config.objects

import com.teamresourceful.resourcefulconfigkt.api.ObjectKt
import me.owdding.customscoreboard.utils.rendering.alignment.TextAlignment

class TitleOrFooterObject : ObjectKt() {
    val alignment by enum(TextAlignment.CENTER) {
        this.name = Translated("config.cs.title_footer.alignment")
        this.description = Translated("config.cs.title_footer.alignment.desc")
    }

    val useCustomText by boolean("use_custom_text", false) {
        this.name = Translated("config.cs.title_footer.use_custom_text")
        this.description = Translated("config.cs.title_footer.use_custom_text.desc")
    }

    val text by string("custom_text", "") {
        this.name = Translated("config.cs.title_footer.custom_text")
        this.description = Translated("config.cs.title_footer.custom_text.desc")
    }
}
