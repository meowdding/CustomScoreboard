package me.owdding.customscoreboard.mixins.blur;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "com.mojang.blaze3d.opengl.GlCommandEncoder")
public interface GlCommandEncoderAccessor {

    @Accessor("inRenderPass")
    void cs$setInRenderPass(boolean inRenderPass);
}
