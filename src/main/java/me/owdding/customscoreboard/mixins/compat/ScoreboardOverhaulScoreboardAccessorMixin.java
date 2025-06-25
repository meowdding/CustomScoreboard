package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import kotlinx.datetime.Instant;
import me.jfenn.scoreboardoverhaul.common.data.ObjectiveInfo;
import me.jfenn.scoreboardoverhaul.common.data.ScoreInfo;
import me.jfenn.scoreboardoverhaul.impl.ScoreboardAccessor;
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = {ScoreboardAccessor.class}, remap = false)
public class ScoreboardOverhaulScoreboardAccessorMixin {

    @ModifyReturnValue(
        method = "getSidebarObjective",
        at = @At("RETURN"),
        remap = false
    )
    ObjectiveInfo customscoreboard$getSidebarObjective(ObjectiveInfo original) {
        if (original == null) {
            return null;
        }
        if (!CustomScoreboardRenderer.INSTANCE.renderScoreboardOverhaul()) {
            return original;
        }
        if (!LocationAPI.INSTANCE.isOnSkyBlock()) {
            return original;
        }
        if (CustomScoreboardRenderer.INSTANCE.getLines().isEmpty()) {
            return original;
        }

        return new ObjectiveInfo(
            original.getId(),
            CustomScoreboardRenderer.INSTANCE.getLines().getFirst().getComponent(),
            original.getRenderType()
        );
    }

    @ModifyReturnValue(
        method = "getScoreList",
        at = @At("RETURN"),
        remap = false
    )
    List<ScoreInfo> customscoreboard$getScoreList(List<ScoreInfo> original) {
        if (original == null) {
            return null;
        }
        if (!CustomScoreboardRenderer.INSTANCE.renderScoreboardOverhaul()) {
            return original;
        }
        if (!LocationAPI.INSTANCE.isOnSkyBlock()) {
            return original;
        }

        var lines = CustomScoreboardRenderer.INSTANCE.getLines();

        if (lines.isEmpty()) {
            return original;
        }

        ArrayList<ScoreInfo> scores = new ArrayList<>();

        for (int i = lines.size() - 2; i >= 0; i--) {
            scores.add(
                new ScoreInfo(
                    "Line " + i,
                    lines.get(lines.size() - i - 1).getComponent(),
                    i,
                    Instant.Companion.now()
                )
            );
        }

        return scores;
    }
}
