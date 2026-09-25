package me.owdding.customscoreboard.mixins.compat;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@IfModLoaded("skyhanni")
@Mixin(targets = "at.hannibal2.skyhanni.features.misc.compacttablist.TabListRenderer", remap = false)
public interface SkyHanniTabListRendererMixin {

    // https://github.com/hannibal002/SkyHanni/blob/0913ae5c96fc52d03aa41ad1a8369c8643e6dc63/src/main/java/at/hannibal2/skyhanni/features/misc/compacttablist/TabListRenderer.kt#L45
    @Accessor("isTabToggled")
    static boolean customscoreboard$isTabToggled() {
        return false;
    }

    @Accessor("isPressed")
    static boolean customscoreboard$isPressed() {
        return false;
    }
}
