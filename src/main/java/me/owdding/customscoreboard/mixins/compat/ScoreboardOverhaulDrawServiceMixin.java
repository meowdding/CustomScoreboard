package me.owdding.customscoreboard.mixins.compat;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jfenn.scoreboardoverhaul.impl.DrawService;
import me.owdding.customscoreboard.config.categories.ModCompatibilityConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;
import tech.thatgravyboat.skyblockapi.api.profile.profile.ProfileAPI;

//? > 1.21.5 {
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;
//?} else {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.ARGB;
*///?}

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = DrawService.class, remap = false)
public class ScoreboardOverhaulDrawServiceMixin {

    @Unique
    private Integer getOverhaulColor() {
        if (!ModCompatibilityConfig.INSTANCE.getSkyblockLevelColor() || !LocationAPI.INSTANCE.isOnSkyBlock()) return null;
        return ProfileAPI.INSTANCE.getLevelColor();
    }

    //? > 1.21.5 {
    @Inject(method = "setColorTint", at = @At(value = "HEAD"))
    public void setColorTint(CallbackInfo ci, @Local(argsOnly = true) LocalRef<Color> color) {
        Integer overhaulColor = this.getOverhaulColor();
        if (overhaulColor != null) {
            color.set(new Color(overhaulColor));
        }
    }
    //?} else {
    /*@WrapOperation(
        method = "setColorTint",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V")
    )
    private void customscoreboard$setColorTint(float f, float g, float h, float i, Operation<Void> original) {
        Integer overhaulColor = this.getOverhaulColor();
        if (overhaulColor != null && (f != 1.0f || g != 1.0f || h != 1.0f)) {
            original.call(
                ARGB.redFloat(overhaulColor),
                ARGB.greenFloat(overhaulColor),
                ARGB.blueFloat(overhaulColor),
                i
            );
        } else {
            original.call(f, g, h, i);
        }
    }*///?}
}
