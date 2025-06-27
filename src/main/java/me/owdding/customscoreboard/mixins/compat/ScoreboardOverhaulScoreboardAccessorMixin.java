package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jfenn.scoreboardoverhaul.common.data.ObjectiveInfo;
import me.jfenn.scoreboardoverhaul.common.data.ScoreInfo;
import me.jfenn.scoreboardoverhaul.impl.ScoreboardAccessor;
import me.owdding.customscoreboard.feature.customscoreboard.CustomScoreboardRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

import static tech.thatgravyboat.skyblockapi.utils.time.InstantExtensionsKt.currentInstant;

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = {ScoreboardAccessor.class}, remap = false)
public class ScoreboardOverhaulScoreboardAccessorMixin {

    @Unique
    private boolean shouldModify() {
        return CustomScoreboardRenderer.INSTANCE.renderScoreboardOverhaul()
            && CustomScoreboardRenderer.INSTANCE.shouldUseCustomLines()
            && !CustomScoreboardRenderer.INSTANCE.getLines().isEmpty();
    }

    @ModifyReturnValue(
        method = "getSidebarObjective",
        at = @At("RETURN")
    )
    ObjectiveInfo customscoreboard$getSidebarObjective(ObjectiveInfo original) {
        if (original == null || !shouldModify()) {
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
        at = @At("RETURN")
    )
    List<ScoreInfo> customscoreboard$getScoreList(List<ScoreInfo> original) {
        if (original == null || !shouldModify()) {
            return original;
        }

        var lines = CustomScoreboardRenderer.INSTANCE.getLines();

        ArrayList<ScoreInfo> scores = new ArrayList<>();

        for (int i = lines.size() - 2; i >= 0; i--) {
            scores.add(
                new ScoreInfo(
                    "Line " + i,
                    lines.get(lines.size() - i - 1).getComponent(),
                    i,
                    currentInstant()
                )
            );
        }

        return scores;
    }
}
