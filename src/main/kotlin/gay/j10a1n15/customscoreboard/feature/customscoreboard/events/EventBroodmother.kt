package gay.j10a1n15.customscoreboard.feature.customscoreboard.events

import gay.j10a1n15.customscoreboard.utils.TextUtils.trim
import me.owdding.ktmodules.Module
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch

@Module
object EventBroodmother : Event() {
    override fun getDisplay() = stateString

    override fun showIsland() = SkyBlockIsland.inAnyIsland(SkyBlockIsland.SPIDERS_DEN)

    override val configLine = "Broodmother"


    private var stateString: Component? = null

    private val broodmotherRegex = ComponentRegex(" Broodmother: (?<state>.*)")

    @Subscription
    @OnlyWidget(TabWidget.AREA)
    fun onTabWidgetUpdate(event: TabWidgetChangeEvent) {
        broodmotherRegex.anyMatch(event.newComponents) {
            stateString = it.component.trim()
        }
    }
}
