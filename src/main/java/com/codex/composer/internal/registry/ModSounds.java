package com.codex.composer.internal.registry;

import com.codex.composer.api.v1.registry.lazy.DeferredSoundRegistry;
import com.codex.composer.internal.Composer;

//? if minecraft: <=1.20.1 {
/*import com.codex.composer.api.v1.util.misc.TranslatableSoundEvent;
*///?} else {
import net.minecraft.sound.SoundEvent;
//?}

public class ModSounds {
    private static final DeferredSoundRegistry REGISTRY = new DeferredSoundRegistry(Composer.MOD_ID);

    //? if minecraft: <=1.20.1 {
    /*public static final TranslatableSoundEvent LILBRO_SQUISH = REGISTRY.register("lilbro_squish", "lilbro_squish");
    public static final TranslatableSoundEvent VINE_BOOM = REGISTRY.register("vine_boom", "vine_boom");
    *///?} else {
    public static final SoundEvent LILBRO_SQUISH = REGISTRY.register("lilbro_squish");
    public static final SoundEvent VINE_BOOM = REGISTRY.register("vine_boom");
     //?}

    public static void initialize() {

    }
}
