package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jfenn.scoreboardoverhaul.impl.DrawService;
import me.owdding.customscoreboard.config.categories.ModCompatibilityConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI;

import java.awt.Color;

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = DrawService.class, remap = false)
public class ScoreboardOverhaulDrawServiceMixin {

    @Unique
    private Integer getOverhaulColor() {
        if (!ModCompatibilityConfig.INSTANCE.getSkyblockLevelColor() || !LocationAPI.INSTANCE.isOnSkyBlock()) return null;
        return ProfileAPI.INSTANCE.getLevelColor();
    }

    @Inject(method = "setColorTint", at = @At(value = "HEAD"))
    public void setColorTint(CallbackInfo ci, @Local(argsOnly = true) LocalRef<Color> color) {
        Integer overhaulColor = this.getOverhaulColor();
        if (overhaulColor != null) {
            color.set(new Color(overhaulColor));
        }
    }
}
