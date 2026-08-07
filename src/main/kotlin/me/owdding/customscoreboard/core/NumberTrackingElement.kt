package me.owdding.customscoreboard.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.config.category.LinesConfig
import me.owdding.customscoreboard.elements.Element
import me.owdding.customscoreboard.utils.NumberUtils.format
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.profile.ProfileChangeEvent
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextBuilder.append
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

abstract class NumberTrackingElement(val numberColor: Int) : Element() {
    open var previousAmount: Long = -1
    open var temporaryChangeDisplay: Component? = null
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
            changeAmount > 0 -> Text.of(" ") {
                color = TextColor.GRAY
                append("(")
                append("+${format(changeAmount)}", numberColor)
                append(")")
            }

            changeAmount < 0 -> Text.of(" ") {
                color = TextColor.GRAY
                append("(")
                append(format(changeAmount), numberColor)
                append(")")
            }

            else -> null
        }


        this.currentJob?.cancel("New change detected")
        this.currentJob = CustomScoreboardMod.coroutineScope.launch {
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
