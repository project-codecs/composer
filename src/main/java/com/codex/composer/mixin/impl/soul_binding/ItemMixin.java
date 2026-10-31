package com.codex.composer.mixin.impl.soul_binding;

import net.minecraft.item.Item;
import com.codex.composer.mixin.accessor.ItemMethodAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if <=1.20.4 {
/*import com.codex.composer.api.v1.item.settings.ComposerItemSettings;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }

@SuppressWarnings("FieldCanBeLocal")
@Mixin(Item.class)
public class ItemMixin implements ItemMethodAccessor {
    @Unique private boolean soulbound = false;
    @Unique private boolean soulboundCanDrop = false;

    //? if <=1.20.4 {
    /*@Inject(method = "<init>", at = @At("TAIL"))
    private void composer$settingsImpl(Item.Settings settings, CallbackInfo ci) {
        if (settings instanceof ComposerItemSettings c) {
            soulbound = c.soulbound;
            soulboundCanDrop = c.soulboundCanDrop;
        }
    }
    *///? }

    @Override
    public boolean composer$soulbound() {
        return soulbound;
    }

    @Override
    public boolean composer$soulboundCanDrop() {
        return soulboundCanDrop;
    }
}
