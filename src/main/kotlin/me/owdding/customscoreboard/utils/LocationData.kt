package me.owdding.customscoreboard.utils

import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockBiome
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

// TODO: this could be nice in sbapi
data class LocationData(
    val areas: Set<SkyBlockArea> = emptySet(),
    val islands: Set<SkyBlockIsland> = emptySet(),
    val biomes: Set<SkyBlockBiome> = emptySet(),
) {
    fun inArea() = SkyBlockArea.inAnyArea(areas)
    fun inIsland() = SkyBlockIsland.inAnyIsland(islands)
    fun inBiome() = SkyBlockBiome.inAnyBiome(biomes)

    fun inLocation() = inArea() || inIsland() || inBiome()
}
