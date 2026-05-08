package com.codex.composer.api.v1.item.settings;

import net.minecraft.item.Item;

//? if minecraft: >=1.20.6
import com.codex.composer.internal.registry.ModDataComponentTypes;

public class ComposerItemSettings extends Item.Settings {
    //? if minecraft: <=1.20.4 {
    /*public boolean soulbound = false;
    public boolean soulboundCanDrop = false;
    *///? }

    public ComposerItemSettings soulbound(boolean canDrop) {
        //? if minecraft: >=1.20.6 {
        /*? if legacy {*/component/*? } else {*//*withComponent*//*? }*/(ModDataComponentTypes.SOULBOUND, true);
        /*? if legacy {*/component/*? } else {*//*withComponent*//*? }*/(ModDataComponentTypes.SOULBOUND_CAN_DROP, canDrop);
        //? } else {
        /*soulbound = true;
        soulboundCanDrop = canDrop;
        *///? }
        return this;
    }

    public ComposerItemSettings soulbound() {
        return soulbound(true);
    }
}
