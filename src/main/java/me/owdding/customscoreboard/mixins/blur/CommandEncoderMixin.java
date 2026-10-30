package me.owdding.customscoreboard.mixins.blur;

import com.mojang.renderpearl.backend.api.CommandEncoderBackend;
import me.owdding.customscoreboard.hooks.CommandEncoderHook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//~ if >= 26.3 'com.mojang.blaze3d.systems.CommandEncoder' -> 'com.mojang.renderpearl.frontend.FrontendCommandEncoder'
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;

//~ if >= 26.3 'CommandEncoder' -> 'FrontendCommandEncoder'
@Mixin(FrontendCommandEncoder.class)
public class CommandEncoderMixin implements CommandEncoderHook {

    @Final
    @Shadow
    private CommandEncoderBackend backend;

    @Shadow
    private boolean isInRenderPass;

    @Override
    public void cs$setInRenderPass(boolean inRenderPass) {
        if (this.backend instanceof CommandEncoderHook hook) {
            hook.cs$setInRenderPass(inRenderPass);
        }
        this.isInRenderPass = inRenderPass;
    }
}
