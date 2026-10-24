package com.codex.composer.mixin.impl.duped_binds;

//? if minecraft: <=1.21.6 {
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.client.screen.DupedBindsConfigScreen;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? }

import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin extends GameOptionsScreen {
    public ControlsOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    //? if minecraft: <=1.21.6 {
    @Inject(method = /*~ if <=1.20.4 'addOptions' -> 'init' {*/"addOptions"/*~}*/, at = @At("TAIL"))
    public void composer$addConfigButton(CallbackInfo ci) {
        if (Composer.disableDupedBinds()) return;
        //? if minecraft: <=1.20.4 {
        /*DupedBindsConfigScreen.modify(this, this::addDrawableChild);
        *///? } else {
        DupedBindsConfigScreen.modify((ControlsOptionsScreen) (Object) this);
        //? }
    }
    //? }
}
