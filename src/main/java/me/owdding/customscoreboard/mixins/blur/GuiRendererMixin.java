package me.owdding.customscoreboard.mixins.blur;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform;
import me.owdding.customscoreboard.feature.customscoreboard.BlurredBackground;
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

    @Inject(
        method = "executeDraw(Lnet/minecraft/client/gui/render/GuiRenderer$Draw;Lcom/mojang/blaze3d/systems/RenderPass;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setIndexBuffer(Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;)V")
    )
    private void copyBackground(GuiRenderer.Draw draw, RenderPass pass, GpuBuffer buffer, VertexFormat.IndexType indexType, CallbackInfo ci) {
        if (draw.textureSetup() != BlurredBackground.getSetup()) return;

        var encoder = RenderSystem.getDevice().createCommandEncoder();
        var target = Minecraft.getInstance().getMainRenderTarget();
        var uniform = BlurredBackground.getUniform();

        if (!(encoder instanceof GlCommandEncoderAccessor accessor)) return;

        accessor.cs$setInRenderPass(false);

        encoder.copyTextureToTexture(
            target.getColorTexture(),
            BlurredBackground.getTexture(),
            0,
            0, 0,
            0, 0,
            target.width, target.height
        );

        var uniformBuffer = uniform != null ? RoundedTextureUniform.STORAGE.get().writeUniform(uniform) : null;
        accessor.cs$setInRenderPass(true);

        if (uniformBuffer != null) {
            pass.setUniform(RoundedTextureUniform.NAME, uniformBuffer);
        }
    }

    @WrapOperation(method = "addElementToMesh", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;scissorChanged(Lnet/minecraft/client/gui/navigation/ScreenRectangle;Lnet/minecraft/client/gui/navigation/ScreenRectangle;)Z"))
    private boolean forceMeshRecording(GuiRenderer instance, ScreenRectangle scissorArea, ScreenRectangle oldScissorArea, Operation<Boolean> original, @Local TextureSetup setup) {
        if (this.previousTextureSetup == BlurredBackground.getSetup()) return true;
        if (setup == BlurredBackground.getSetup()) return true;
        return original.call(instance, scissorArea, oldScissorArea);
    }
}
