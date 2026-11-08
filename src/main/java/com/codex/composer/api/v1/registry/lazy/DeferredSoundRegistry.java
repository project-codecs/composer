package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

//? if <=1.21
//import com.codex.composer.api.v1.util.misc.TranslatableSoundEvent;

public class DeferredSoundRegistry extends EmptyDeferredRegistry {
    public DeferredSoundRegistry(String modId) {
        super(modId);
    }

    public SoundEvent register(String name) {
        SoundEvent soundEvent = SoundEvent.of(id(name));
        return Registry.register(Registries.SOUND_EVENT, /*? if <=1.21 { */ /*soundEvent.getId() *//*? } else {*/soundEvent.id()/*?}*/, soundEvent);
    }

    public RegistryEntry.Reference<SoundEvent> registerReference(String name) {
        SoundEvent soundEvent = SoundEvent.of(id(name));
        return Registry.registerReference(Registries.SOUND_EVENT, /*? if <=1.21 { */ /*soundEvent.getId() *//*? } else { */soundEvent.id()/*?}*/, soundEvent);
    }

    //? if <=1.21 {
    /*public TranslatableSoundEvent register(String name, String translation) {
        return register(name, modId, translation);
    }

    public TranslatableSoundEvent register(String name, String translationPrefix, String translationKey) {
        TranslatableSoundEvent soundEvent = TranslatableSoundEvent.of(id(name));
        return Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent).translate(translationPrefix + "." + translationKey);
    }

    public RegistryEntry.Reference<SoundEvent> registerReference(String name, String translation) {
        return registerReference(name, modId, translation);
    }

    public RegistryEntry.Reference<SoundEvent> registerReference(String name, String translationPrefix, String translationKey) {
        TranslatableSoundEvent soundEvent = TranslatableSoundEvent.of(id(name));
        return Registry.registerReference(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent.translate(translationPrefix + "." + translationKey));
    }
    *///?}
}
