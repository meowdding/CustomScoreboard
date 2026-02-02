package me.owdding.customscoreboard.utils

import tech.thatgravyboat.skyblockapi.api.events.misc.AbstractModRegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent

class RegisterCustomScoreboardCommandEvent(event: RegisterCommandsEvent) : AbstractModRegisterCommandsEvent(event, "customscoreboard", "csb", "cs")
