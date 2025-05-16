package gay.j10a1n15.customscoreboard.feature.customscoreboard.elements

import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Module
object ElementLobby : Element() {
    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    private var lobbyCode: String? = null
    private var roomId: String? = null

    override fun getDisplay() = "§7" + LocalDate.now().format(formatter) + " §8$lobbyCode" + if (roomId != null) " §8$roomId" else ""

    override fun showWhen() = lobbyCode != null

    override val configLine = "Lobby"


    private val roomIdRegex = "\\d+\\/\\d+\\/\\d+ \\w+ (?<roomId>[\\w,-]+)".toRegex()

    @Subscription
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        for (line in event.new) {
            roomId = roomIdRegex.find(line)?.groups["roomId"]?.value ?: continue
            return
        }
        roomId = null
    }

    @Subscription
    fun onServerChange(event: ServerChangeEvent) {
        lobbyCode = event.name.replace("mini", "m").replace("mega", "M")
    }
}
