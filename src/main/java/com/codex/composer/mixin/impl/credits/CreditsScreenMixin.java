package com.codex.composer.mixin.impl.credits;

import com.codex.composer.internal.client.credits.CreditsInjectionHandler;
import com.codex.composer.internal.client.screen.ModifiedCreditsScreen;
import com.google.gson.JsonArray;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.CreditsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Reader;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin {
    @SuppressWarnings("ConstantConditions") // Need this because IDEA doesn't know about mixins and assumes CreditsScreenMixin will never be ModifiedCreditsScreen :/
    @Inject(method = "readCredits", at = @At("HEAD"), cancellable = true)
    public void composer$noLoadCredits(Reader reader, CallbackInfo ci) {
        if ((Object) this instanceof ModifiedCreditsScreen mcs && !mcs.showCredits())
            ci.cancel();
    }

    @ModifyExpressionValue(method = "readCredits", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;deserializeArray(Ljava/io/Reader;)Lcom/google/gson/JsonArray;"))
    public JsonArray composer$insertCustomCredits(JsonArray original) {
        return CreditsInjectionHandler.inject(original);
    }
}
