package me.owdding.customscoreboard.feature.customscoreboard.elements

import me.owdding.customscoreboard.AutoElement
import me.owdding.customscoreboard.config.categories.LinesConfig
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.api.profile.PetsAPI
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

@AutoElement
object ElementPet : Element() {
    override fun getDisplay() = buildList {
        val pet = PetsAPI.pet ?: return@buildList

        val petColor = PetsAPI.rarity?.color ?: 0xFFFFFF
        val petLine = Text.of(pet) { this.color = petColor }
        if (LinesConfig.petPrefix) {
            add(CustomScoreboardRenderer.formatNumberDisplayDisplay(Text.of("Pet"), petLine, petColor))
        } else {
            add(petLine)
        }

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
