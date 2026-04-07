package me.owdding.customscoreboard.mixins.blur;

//? if >= 26.1 {
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.CommandEncoderBackend;
import me.owdding.customscoreboard.hooks.CommandEncoderHook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandEncoder.class)
public class CommandEncoderMixin implements CommandEncoderHook {

    @Final
    @Shadow
    private CommandEncoderBackend backend;

    @Override
    public void cs$setInRenderPass(boolean inRenderPass) {
        if (this.backend instanceof CommandEncoderHook hook) {
            hook.cs$setInRenderPass(inRenderPass);
        }
    }
}
//? }
