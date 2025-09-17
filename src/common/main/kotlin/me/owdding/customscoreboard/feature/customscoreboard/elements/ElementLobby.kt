package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.ElementGroup
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.utils.ScoreboardElement
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent

@AutoElement(ElementGroup.HEADER)
@ScoreboardElement
object ElementLobby : Element() {
    private var lobbyCode: String? = null
    private var roomId: String? = null

    override fun getDisplay() = "§7" + LinesConfig.dateFormat + " §8$lobbyCode" + if (roomId != null) " §8$roomId" else ""

    override fun showWhen() = lobbyCode != null

    override val configLine = "Lobby"
    override val id = "LOBBY"


    private val roomIdRegex = "\\d+/\\d+/\\d+ \\w+ (?<roomId>[\\w,-]+)".toRegex()

    override fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        for (line in event.new) {
            roomId = roomIdRegex.find(line)?.groups["roomId"]?.value ?: continue
            return
        }
        roomId = null
    }

    override fun onServerChange(event: ServerChangeEvent) {
        lobbyCode = event.name.replace("mini", "m").replace("mega", "M")
    }
}
