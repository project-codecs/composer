package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

//? minecraft: >=1.20.6
import net.minecraft.registry.entry.RegistryEntry;

public class DeferredPotionRegistry extends EmptyDeferredRegistry {
    public DeferredPotionRegistry(String modId) {
        super(modId);
    }

    public <T extends Potion> T register(String name, T potion) {
        return Registry.register(Registries.POTION, id(name), potion);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Potion register(String name, StatusEffectInstance... effects) {
        return register(name, new Potion(/*? if >=1.21 { */name, /*?}*/ effects));
    }

    public static StatusEffectInstance effect(/*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ type, int duration) {
        return new StatusEffectInstance(type, duration);
    }

    public static StatusEffectInstance effect(/*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ type, int duration, int amplifier) {
        return new StatusEffectInstance(type, duration, amplifier);
    }

    public Potion registerLong(String baseName, StatusEffectInstance... effects) {
        return register("long_" + baseName, new Potion(baseName, effects));
    }

    public Potion registerStrong(String baseName, StatusEffectInstance... effects) {
        return register("strong_" + baseName, new Potion(baseName, effects));
    }

    public void registerVariants(String baseName, StatusEffectInstance normal, StatusEffectInstance longVariant, StatusEffectInstance strongVariant) {
        register(baseName, normal);
        register("long_" + baseName, new Potion(baseName, longVariant));
        register("strong_" + baseName, new Potion(baseName, strongVariant));
    }

    public RegistryKey<Potion> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.POTION, id(name));
    }

    public Potion register(String name, RegistryKey<Potion> key, Potion potion) {
        return Registry.register(Registries.POTION, key, potion);
    }

    public Potion registerEmpty(String name) {
        return register(name, new Potion(/*? if >=1.21.3 { */name/*?}*/));
    }

    public Potion registerWater(String name) {
        return register(name, new Potion(/*? if >=1.21.3 { */name/*?}*/));
    }

    public static Potion of(/*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ type, int duration) {
        return new Potion(/*? if >=1.21.3 { */"", /*?}*/new StatusEffectInstance(type, duration));
    }

    public static Potion of(/*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ type, int duration, int amplifier) {
        return new Potion(/*? if >=1.21.3 { */"", /*?}*/new StatusEffectInstance(type, duration, amplifier));
    }

    public Potion multi(String name, StatusEffectInstance... effects) {
        return register(name, new Potion(/*? if >=1.21.3 { */name, /*?}*/ effects));
    }
}

