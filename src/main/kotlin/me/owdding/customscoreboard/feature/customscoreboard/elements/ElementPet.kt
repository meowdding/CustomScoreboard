package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.config.categories.LinesConfig
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.PetsAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

object ElementPet : Element() {
    override fun getDisplay() = buildList {
        val pet = PetsAPI.pet ?: return@buildList

        add(Text.of(pet) { this.color = PetsAPI.rarity?.color ?: 0x00000 })

        if (PetsAPI.isMaxLevel && LinesConfig.showPetMax) {
            add(Text.of(" MAX") { this.color = TextColor.GREEN })
        }
        if (!PetsAPI.isMaxLevel) {
            add(
                Text.of(" Lvl. ${PetsAPI.level} (${String.format("%.1f", ((PetsAPI.xp / PetsAPI.xpToNextLevel) * 100))}%)") {
                    this.color = TextColor.YELLOW
                },
            )
        }
    }

    override fun showIsland() = !SkyBlockIsland.inAnyIsland(SkyBlockIsland.THE_RIFT)

    override val configLine = "Pet"
}
