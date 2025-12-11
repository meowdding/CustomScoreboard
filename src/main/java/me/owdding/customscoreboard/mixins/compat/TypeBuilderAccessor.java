package me.owdding.customscoreboard.mixins.compat;

import com.teamresourceful.resourcefulconfigkt.api.builders.TypeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TypeBuilder.class)
public interface TypeBuilderAccessor {

    @Accessor("id")
    String customscoreboard$getId();

}
