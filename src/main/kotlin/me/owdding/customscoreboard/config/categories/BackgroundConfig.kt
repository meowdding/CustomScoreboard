package me.owdding.customscoreboard.config.categories

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigUI
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils
import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.dialog.OlympusDialogs
import earth.terrarium.olympus.client.layouts.Layouts
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardBackground
import me.owdding.customscoreboard.utils.rendering.RenderUtils.drawTexture
import net.minecraft.Util
import net.minecraft.client.gui.layouts.LayoutElement
import tech.thatgravyboat.skyblockapi.helpers.McFont
import tech.thatgravyboat.skyblockapi.utils.text.CommonText
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.underlined
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull

object BackgroundConfig : CategoryKt("Background") {

    val enabled by boolean(true) {
        this.translation = "config.cs.background.enabled"
    }

    val backgroundColor by color("color", 0xA0000000.toInt()) {
        this.translation = "config.cs.background.color"
        this.allowAlpha = true
    }

    val padding by int(5) {
        this.translation = "config.cs.background.padding"
        this.range = 0..20
        this.slider = true
    }

    val margin by int(0) {
        this.translation = "config.cs.background.margin"
        this.range = 0..20
        this.slider = true
    }

    val radius by int(5) {
        this.translation = "config.cs.background.radius"
        this.range = 0..20
        this.slider = true
    }

    init {
        separator {
            this.title = "config.cs.background.separator"
            this.description = "config.cs.background.separator.desc"
        }
    }

    val imageBackground by boolean(false) {
        this.translation = "config.cs.background.image"
    }

    init {
        button {
            this.title = "config.cs.background.website"
            this.description = "config.cs.background.website.desc"
            this.text = "Open Website"

            onClick {
                Util.getPlatform().openUri("https://j10a1n15.github.io/j10a1n15/pages/background.html")
            }
        }

        button {
            this.title = "config.cs.background.editor"
            this.description = "config.cs.background.editor.desc"
            this.text = "Open Editor"

            onClick {
                CustomBackgroundModal.open()
            }
        }
    }

    val imageBackgroundTransparency by int(90) {
        this.translation = "config.cs.background.transparency"
        this.range = 5..100
        this.slider = true
    }

    var customImageFile by string("") {
        this.condition = { false }
    }
}

object CustomBackgroundModal {

    private const val PADDING = 10

    private fun getDisplay(width: Int, height: Int): LayoutElement {
        return Widgets.button { it ->
            it.withTexture(null)
            it.withRenderer { graphics, context, _ ->
                val realWidth = (width * 1/3f).toInt()
                graphics.drawTexture(
                    context.x + (context.width - realWidth) / 2, context.y,
                    realWidth, context.height,
                    CustomScoreboardBackground.getTexture(),
                    radius = BackgroundConfig.radius,
                    alpha = BackgroundConfig.imageBackgroundTransparency / 100f,
                )
            }
            it.withSize(width, height - 20 - PADDING * 3 - McFont.height)
            it.active = false
        }
    }

    private fun getFileDisplay(width: Int): LayoutElement {
        return Widgets.button {
            it.withTexture(null)
            it.withRenderer { graphics, context, _ ->
                val hovered = context.mouseX in context.x..context.x + context.width && context.mouseY in context.y..context.y + context.height
                val centerX = context.x + context.width / 2
                val text = when {
                    BackgroundConfig.customImageFile.isEmpty() -> Text.of("No file selected") {
                        this.color = TextColor.RED
                    }
                    hovered -> {
                        ScreenUtils.setTooltip(listOf(
                            Text.of(BackgroundConfig.customImageFile) {
                                this.color = TextColor.GRAY
                                this.underlined = true
                            },
                            CommonText.EMPTY,
                            Text.of("Click to open file location") {
                                this.color = TextColor.YELLOW
                            }
                        ))

                        Text.of("Hover to view file") {
                            this.color = TextColor.WHITE
                        }
                    }
                    else -> Text.of("Hover to view file") {
                        this.color = TextColor.WHITE
                    }
                }

                graphics.drawCenteredString(McFont.self, text, centerX, context.y, -1)
            }
            it.withCallback {
                if (BackgroundConfig.customImageFile.isNotEmpty()) {
                    val path = Path(BackgroundConfig.customImageFile).parent
                    Util.getPlatform().openPath(path)
                }
            }
            it.withSize(width, McFont.height)
        }
    }

    fun open() {
        ResourcefulConfigUI.openModal(Text.of("Custom Background")) { x, y, width, height ->
            val buttonWidth = (width - PADDING * 3) / 2
            val layout = Layouts.column()
                .withGap(PADDING)
                .withChild(getDisplay(width, height))
                .withChild(getFileDisplay(width))
                .withChild(Layouts.row()
                    .withGap(PADDING)
                    .withChild(ResourcefulConfigUI.button(0, 0, buttonWidth, 20, Text.of("Select Image")) {
                        OlympusDialogs.openFileSystemDialog(OlympusDialogs.FileSystemDialogType.OPEN_FILE, null, "*.png").thenAccept { files ->
                            val file = files.getOrNull()?.firstOrNull() ?: return@thenAccept
                            val path = file.toAbsolutePath().toString()
                            BackgroundConfig.customImageFile = path

                            CustomScoreboardBackground.load()
                        }
                    })
                    .withChild(ResourcefulConfigUI.button(0, 0, buttonWidth, 20, Text.of("Remove Image")) {
                        BackgroundConfig.customImageFile = ""
                        CustomScoreboardBackground.load()
                    })
                )

            ResourcefulConfigUI.container(x, y, width, height, layout)
        }
    }
}
