package me.owdding.customscoreboard.mixins.blur;

import me.owdding.customscoreboard.hooks.CommandEncoderHook;
import org.spongepowered.asm.mixin.Shadow;

//? 26.1
//@Mixin(targets = "com.mojang.blaze3d.opengl.GlCommandEncoder")
public class GlCommandEncoderMixin implements CommandEncoderHook {

    @Shadow private boolean inRenderPass;

    @Override
    public void cs$setInRenderPass(boolean inRenderPass) {
        this.inRenderPass = inRenderPass;
    }
}
