package me.owdding.customscoreboard.mixins.blur;

import com.mojang.blaze3d.platform.Window;
import me.owdding.customscoreboard.feature.customscoreboard.BlurredBackground;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;reload()V"))
    private void onInit(CallbackInfo ci) {
        BlurredBackground.init(this.window.getWidth(), this.window.getHeight());
    }

    @Inject(method = "resizeDisplay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;resize(II)V"))
    private void onResize(CallbackInfo ci) {
        BlurredBackground.init(this.window.getWidth(), this.window.getHeight());
    }
}
