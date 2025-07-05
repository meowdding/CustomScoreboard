package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jfenn.scoreboardoverhaul.common.config.ScoreboardConfig;
import me.owdding.customscoreboard.feature.ModCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = ScoreboardConfig.class, remap = false)
public class ScoreboardConfigMixin {

    @ModifyReturnValue(method = "isEnabled", at = @At("RETURN"))
    private static boolean customscoreboard$isEnabled(boolean original) {
        ModCompat.INSTANCE.setScoreboardOverhaulEnabled(original);
        return original;
    }

}
