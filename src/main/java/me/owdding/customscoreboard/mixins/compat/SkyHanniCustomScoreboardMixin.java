package me.owdding.customscoreboard.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.owdding.customscoreboard.compat.ModCompat;
import me.owdding.customscoreboard.config.Config;
import me.owdding.customscoreboard.config.category.ModCompatibilityConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@IfModLoaded("skyhanni")
@Mixin(targets = "at.hannibal2.skyhanni.features.gui.customscoreboard.CustomScoreboard", remap = false)
public class SkyHanniCustomScoreboardMixin {

    @ModifyReturnValue(
        method = "isEnabled",
        at = @At(
            value = "RETURN"
        ),
        remap = false,
        require = 0
    )
    boolean customscoreboard$isEnabled(boolean original) {
        ModCompat.INSTANCE.setSkyhanniCustomScoreboardEnabled(original);
        if (ModCompatibilityConfig.INSTANCE.getOverrideSkyHanniScoreboard() && Config.INSTANCE.getEnabled()) {
            return false;
        }
        return original;
    }

}
