package me.owdding.customscoreboard.elements

import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.utils.ElementGroup
import me.owdding.customscoreboard.utils.RemoteStrings
import me.owdding.customscoreboard.utils.ScoreboardElement
import me.owdding.customscoreboard.utils.StringGroup.Companion.resolve
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.anyFound
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@ScoreboardElement
object LobbyElement : Element() {
    private var lobbyCode: String? = null
    private var roomId: String? = null

    override fun getDisplay() = Text.of {
        color = TextColor.DARK_GRAY
        append(if (LinesConfig.dateInLobbyCode) "${LinesConfig.dateFormat} " else "", TextColor.GRAY)
        append(lobbyCode ?: "")
        roomId?.let {
            append(" $it")
        }
    }

    override fun showWhen() = lobbyCode != null

    override val configLine = "Lobby"
    override val id = "LOBBY"
    override val group = ElementGroup.HEADER


    private val roomIdRegex by RemoteStrings.resolve().regex("\\d+/\\d+/\\d+ \\w+ (?<roomId>[\\w,-]+)")

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        val found = roomIdRegex.anyFound(event.new, "roomId") { (roomId) ->
            this.roomId = roomId
        }
        if (!found) roomId = null
    }

    override fun onServerChange(event: ServerChangeEvent) {
        lobbyCode = event.name.replace("mini", "m").replace("mega", "M")
    }
}
