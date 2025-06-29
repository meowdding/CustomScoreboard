package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jfenn.scoreboardoverhaul.common.config.ConfigManager;
import me.jfenn.scoreboardoverhaul.common.config.ScoreboardConfig;
import me.owdding.customscoreboard.feature.ModCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = {ConfigManager.class}, remap = false)
public class ScoreboardOverhaulConfigManagerMixin {

    @ModifyReturnValue(
        method = "access$getInstance$cp",
        at = @At("RETURN")
    )
    private static ScoreboardConfig customscoreboard$access$getInstance(ScoreboardConfig original) {
        ModCompat.INSTANCE.setScoreboardOverhaulEnabled(original.isEnabled());
        return original;
    }

}
