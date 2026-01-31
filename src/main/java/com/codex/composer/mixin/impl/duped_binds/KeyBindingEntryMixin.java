package com.codex.composer.mixin.impl.duped_binds;

import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.client.config.ComposerConfig;
import com.codex.composer.internal.client.duped_binds.BindTracker;
import com.codex.composer.internal.client.duped_binds.RainbowColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public abstract class KeyBindingEntryMixin {
    @Shadow
    @Final
    private KeyBinding binding;

    @Shadow
    protected abstract void update();

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"), index = 4)
    private int flowed_combat$recolorBinding(int value) {
        if (Composer.disableDupedBinds()) return value;
        if (BindTracker.bindAllowed(binding)) {
            RainbowColor.stepColor();
            this.update();
            return ComposerConfig.INSTANCE.rainbowEffectOnDuplicateKeybinds ?
                    RainbowColor.currentColor :
                    Formatting.AQUA.getColorValue() == null ? 0xFF000000 : Formatting.AQUA.getColorValue();
        } else {
            return value;
        }
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setMessage(Lnet/minecraft/text/Text;)V", ordinal = 1))
    private Text flowed_combat$updateBindingColor(Text value) {
        if (Composer.disableDupedBinds()) return value;
        if (BindTracker.bindAllowed(binding)) {
            return ComposerConfig.INSTANCE.rainbowEffectOnDuplicateKeybinds ?
                    binding.getBoundKeyLocalizedText().copy().setStyle(Style.EMPTY.withColor(RainbowColor.currentColor)) :
                    binding.getBoundKeyLocalizedText().copy().formatted(Formatting.AQUA);
        } else {
            return value;
        }
    }
}
