package me.owdding.customscoreboard.mixins.compat;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.owdding.customscoreboard.compat.TablistCompat;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@IfModLoaded("skyblocker")
@Mixin(targets = "de.hysky.skyblocker.skyblock.tabhud.screenbuilder.WidgetManager", remap = false)
public class SkyblockerWidgetManagerMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Z)V", at = @At("HEAD"), require = 0)
    private static void customscoreboard$onExtractRenderStateHead(GuiGraphicsExtractor graphics, boolean hud, CallbackInfo ci) {
        if (!hud) {
            TablistCompat.setSkyblockerTabRendering(false);
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIZ)V", at = @At("TAIL"), require = 0)
    private static void customscoreboard$onExtractRenderStateTail(GuiGraphicsExtractor graphics, int w, int h, boolean hud, CallbackInfo ci) {
        if (!hud) {
            TablistCompat.setSkyblockerTabRendering(true);
        }
    }
}
