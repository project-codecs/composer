package com.codex.composer.mixin.impl.credits;

import com.codex.composer.internal.client.screen.ModifiedCreditsScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreditsAndAttributionScreen.class)
public class CreditsAndAttributionScreenMixin {
    @WrapOperation(method = "openCredits", at = @At(value = "NEW", target = "net/minecraft/client/gui/screen/CreditsScreen"))
    public CreditsScreen composer$replaceCredits(boolean endCredits, Runnable finishAction, Operation<CreditsScreen> original) {
        return new ModifiedCreditsScreen(true, endCredits, finishAction);
    }
}
