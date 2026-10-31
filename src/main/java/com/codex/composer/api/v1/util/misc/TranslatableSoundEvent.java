package com.codex.composer.api.v1.util.misc;

//? if <=1.21 {
/*import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class TranslatableSoundEvent extends SoundEvent implements Translatable {
    private String translationKey;

    private TranslatableSoundEvent(Identifier id, float distanceToTravel, boolean useStaticDistance) {
        super(id, distanceToTravel, useStaticDistance);
        translationKey = id.toTranslationKey();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static TranslatableSoundEvent of(Identifier id, Optional<Float> distanceToTravel) {
        return distanceToTravel.map(float_ -> of(id, float_)).orElseGet(() -> of(id));
    }

    public static TranslatableSoundEvent of(Identifier id) {
        return new TranslatableSoundEvent(id, 16.0F, false);
    }

    public static TranslatableSoundEvent of(Identifier id, float distanceToTravel) {
        return new TranslatableSoundEvent(id, distanceToTravel, true);
    }

    public TranslatableSoundEvent translate(String key) {
        this.translationKey = key;
        return this;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }
}
*///?}