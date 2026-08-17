package me.owdding.customscoreboard.utils

import net.minecraft.world.phys.AABB
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockArea
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockBiome
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.helpers.McPlayer
import tech.thatgravyboat.skyblockapi.helpers.McPlayer.contains

// TODO: this could be nice in sbapi
data class LocationData(
    val areas: Set<SkyBlockArea> = emptySet(),
    val islands: Set<SkyBlockIsland> = emptySet(),
    val biomes: Set<SkyBlockBiome> = emptySet(),
    val islandBasedAABBs: Map<SkyBlockIsland, Set<AABB>> = emptyMap(),
) {
    fun inArea() = SkyBlockArea.inAnyArea(areas)
    fun inIsland() = SkyBlockIsland.inAnyIsland(islands)
    fun inBiome() = SkyBlockBiome.inAnyBiome(biomes)
    fun inAABB() = islandBasedAABBs[LocationAPI.island]?.any { McPlayer in it } == true

    fun inLocation() = inArea() || inIsland() || inBiome() || inAABB()
}
