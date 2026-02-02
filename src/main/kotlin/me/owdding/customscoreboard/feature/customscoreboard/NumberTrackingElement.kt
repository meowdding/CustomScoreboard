package me.owdding.customscoreboard.feature.customscoreboard

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.owdding.customscoreboard.Main
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.elements.Element
import me.owdding.customscoreboard.utils.NumberUtils.format
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.profile.ProfileChangeEvent

abstract class NumberTrackingElement(val numberColor: String) : Element() {
    open var previousAmount: Long = -1
    open var temporaryChangeDisplay: String? = null
    open var temporaryChangeAmount: Long = 0
    open var currentJob: Job? = null


    open fun format(number: Number): String = number.format()

    open fun checkDifference(currentAmount: Long) {
        if (!LinesConfig.showCurrencyGain) return
        if (previousAmount == -1L) {
            previousAmount = currentAmount
            return
        }
        if (currentAmount != previousAmount) {
            val changeAmount = currentAmount - previousAmount
            showTemporaryChange(changeAmount)
            previousAmount = currentAmount
        }
    }

    protected open fun showTemporaryChange(changeAmount: Long, durationMillis: Long = 5000) {
        val changeAmount = temporaryChangeAmount + changeAmount
        temporaryChangeAmount = changeAmount

        temporaryChangeDisplay = when {
            changeAmount > 0 -> " §7($numberColor+${format(changeAmount)}§7)$numberColor"
            changeAmount < 0 -> " §7($numberColor${format(changeAmount)}§7)$numberColor"
            else -> null
        }


        this.currentJob?.cancel("New change detected")
        this.currentJob = Main.coroutineScope.launch {
            delay(durationMillis)
            temporaryChangeDisplay = null
            temporaryChangeAmount = 0
        }
    }

    @Subscription(event = [ProfileChangeEvent::class], inherited = true)
    fun onProfileSwitch() {
        previousAmount = -1
    }
}
