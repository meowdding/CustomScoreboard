package me.owdding.customscoreboard.mixins.blur;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.RenderPass;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform;
import me.owdding.customscoreboard.core.BlurredBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// adapted from: https://github.com/wisp-forest/owo-lib/blob/braid-ui/src/main/java/io/wispforest/owo/mixin/ui/GuiRendererMixin.java#L52
// licensed under MIT
@Mixin(GuiRenderer.class)
public class GuiRendererMixin {

    @Shadow
    @Nullable
    private TextureSetup previousTextureSetup;

    @Inject(method = "draw", at = @At("HEAD"))
    private void copyBackgroundBeforePass(CallbackInfo ci) {
        if (BlurredBackground.getNeedsCopy()) {
            var encoder = RenderSystem.getDevice().createCommandEncoder();
            var target = Minecraft.getInstance().gameRenderer.mainRenderTarget();

            encoder.copyTextureToTexture(
                target.getColorTexture(),
                BlurredBackground.INSTANCE.getSetup().texure0().texture(),
                0, 0, 0, 0, 0, target.width, target.height
            );

            BlurredBackground.setNeedsCopy(false);
        }
    }

    @Inject(
        method = "executeDraw",
        at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setIndexBuffer(Lcom/mojang/renderpearl/api/buffers/GpuBuffer;Lcom/mojang/renderpearl/api/pipeline/IndexType;)V")
    )
    private void copyBackground(GuiRenderer.Draw draw, RenderPass renderPass, CallbackInfo ci) {
        if (draw.textureSetup() != BlurredBackground.getSetup()) return;

        var uniform = BlurredBackground.getUniform();
        if (uniform != null) {
            //~ if >= 26.3 'writeUniform' -> 'writeData'
            var uniformBuffer = RoundedTextureUniform.STORAGE.get().writeData(uniform);
            if (uniformBuffer != null) {
                renderPass.setUniform(RoundedTextureUniform.NAME, uniformBuffer);
            }
        }
    }

    @WrapOperation(method = "addElementToMesh", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;scissorChanged(Lnet/minecraft/client/gui/navigation/ScreenRectangle;Lnet/minecraft/client/gui/navigation/ScreenRectangle;)Z"))
    private boolean forceMeshRecording(GuiRenderer instance, ScreenRectangle scissorArea, ScreenRectangle oldScissorArea, Operation<Boolean> original, @Local TextureSetup setup) {
        if (this.previousTextureSetup == BlurredBackground.getSetup()) return true;
        if (setup == BlurredBackground.getSetup()) return true;
        return original.call(instance, scissorArea, oldScissorArea);
    }
}
