package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jfenn.scoreboardoverhaul.impl.DrawService;
import me.owdding.customscoreboard.config.MainConfig;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@IfModLoaded("scoreboard-overhaul")
@Mixin(value = DrawService.class, remap = false)
public class ScoreboardOverhaulDrawService {

    @WrapOperation(
        method = "setColorTint",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V")
    )
    private void customscoreboard$setColorTint(float f, float g, float h, float i, Operation<Void> original) {
        if (f == 1f && g == 1f && h == 1f && i == 1f) {
            original.call(f, g, h, i);
            return;
        }

        int color = MainConfig.INSTANCE.getOverhaulColor();
        original.call(
            ARGB.redFloat(color),
            ARGB.greenFloat(color),
            ARGB.blueFloat(color),
            ARGB.alphaFloat(color)
        );
    }

}
