package gay.j10a1n15.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import net.minecraft.Util

object BackgroundConfig : CategoryKt("Background") {

    val enabled by boolean(true) {
        this.name = Translated("config.cs.background.enabled")
        this.description = Translated("config.cs.background.enabled.desc")
    }

    val backgroundColor by color("color", 0xA0000000.toInt()) {
        this.name = Translated("config.cs.background.color")
        this.description = Translated("config.cs.background.color.desc")
        this.allowAlpha = true
    }

    val padding by int(5) {
        this.name = Translated("config.cs.background.padding")
        this.description = Translated("config.cs.background.padding.desc")
        this.range = 0..20
        this.slider = true
    }

    val margin by int(0) {
        this.name = Translated("config.cs.background.margin")
        this.description = Translated("config.cs.background.margin.desc")
        this.range = 0..20
        this.slider = true
    }

    val radius by int(5) {
        this.name = Translated("config.cs.background.radius")
        this.description = Translated("config.cs.background.radius.desc")
        this.range = 0..20
        this.slider = true
    }

    @ConfigOption.Separator("Custom Image Background")
    private val separator = Unit

    init {
        button {
            this.title = "Image Creator Website"
            this.description = "Click to open a website that helps you generate your own background texturepack. " +
                "Make sure to enable the 'Modern CustomScoreboard' option."
            this.text = "Open Website"

            onClick {
                Util.getPlatform().openUri("https://j10a1n15.github.io/j10a1n15/pages/background.html")
            }
        }
    }

    val imageBackground by boolean(false) {
        this.name = Translated("config.cs.background.image")
        this.description = Translated("config.cs.background.image.desc")
    }

    val imageBackgroundTransparency by int(90) {
        this.range = 0..100
        this.slider = true
        this.name = Translated("config.cs.background.transparency")
        this.description = Translated("config.cs.background.transparency.desc")
    }

}
