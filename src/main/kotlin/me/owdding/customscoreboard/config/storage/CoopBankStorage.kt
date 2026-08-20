package me.owdding.customscoreboard.config.storage

import com.mojang.serialization.Codec
import me.owdding.customscoreboard.CustomScoreboardMod
import me.owdding.customscoreboard.generated.CodecUtils
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI

object CoopBankStorage {

    private val storage = CustomScoreboardMod.storage<MutableMap<String, Boolean>>(
        "coop_bank",
        { mutableMapOf() },
        CodecUtils.map(Codec.STRING, Codec.BOOL),
    )

    fun setCurrentProfile(isActualCoop: Boolean) {
        if (getCurrentProfile() == isActualCoop) return
        val profile = ProfileAPI.profileName ?: return
        storage.get()[profile] = isActualCoop
        storage.save()
    }

    fun getCurrentProfile(): Boolean = storage.get()[ProfileAPI.profileName] == true

}
