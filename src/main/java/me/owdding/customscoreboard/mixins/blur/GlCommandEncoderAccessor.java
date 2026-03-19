package me.owdding.customscoreboard.mixins.blur;

import com.mojang.blaze3d.opengl.GlCommandEncoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlCommandEncoder.class)
public interface GlCommandEncoderAccessor {

    @Accessor("inRenderPass")
    void cs$setInRenderPass(boolean inRenderPass);
}
