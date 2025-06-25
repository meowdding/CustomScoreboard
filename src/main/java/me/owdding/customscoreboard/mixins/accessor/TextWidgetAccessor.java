package me.owdding.customscoreboard.mixins.accessor;

import earth.terrarium.olympus.client.components.string.TextWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextWidget.class)
public interface TextWidgetAccessor {

    @Accessor("text")
    Component getText();
}
