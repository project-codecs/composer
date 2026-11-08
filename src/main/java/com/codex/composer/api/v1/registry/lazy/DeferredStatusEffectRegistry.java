package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.entity.attribute.EntityAttribute;

//? if <=1.20.4
//import java.util.UUID;

public class DeferredStatusEffectRegistry extends EmptyDeferredRegistry {
    public DeferredStatusEffectRegistry(String modId) {
        super(modId);
    }

    public /*? if <=1.20.4 { *//*<T extends StatusEffect> T*//*? } else {*/<T extends StatusEffect> RegistryEntry<T>/*?}*/ register(String name, T effect) {
        //? if <=1.20.4 {
        /*return Registry.register(Registries.STATUS_EFFECT, id(name), effect);
        *///?} else {
        return (RegistryEntry<T>) Registry.registerReference(Registries.STATUS_EFFECT, id(name), effect);
        //?}
    }

    public /*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ simple(String name, StatusEffectCategory category, int color) {
        return register(name, new SimpleEffect(category, color));
    }

    public /*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ beneficial(String name, int color) {
        return simple(name, StatusEffectCategory.BENEFICIAL, color);
    }

    public /*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ harmful(String name, int color) {
        return simple(name, StatusEffectCategory.HARMFUL, color);
    }

    public /*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ neutral(String name, int color) {
        return simple(name, StatusEffectCategory.NEUTRAL, color);
    }

    public StatusEffectBuilder attribute(String name, StatusEffectCategory category, int color) {
        return new StatusEffectBuilder(this, name, category, color);
    }

    public RegistryKey<StatusEffect> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.STATUS_EFFECT, id(name));
    }

    public static class StatusEffectBuilder {
        private final DeferredStatusEffectRegistry registry;
        private final String name;
        private final StatusEffect delegate;

        private StatusEffectBuilder(DeferredStatusEffectRegistry registry, String name, StatusEffectCategory category, int color) {
            this.registry = registry;
            this.name = name;
            this.delegate = new SimpleEffect(category, color);
        }

        public StatusEffectBuilder withAttribute(String name, /*? if <=1.20.4 { *//*EntityAttribute*//*? } else {*/RegistryEntry<EntityAttribute>/*?}*/ attribute, double amount, EntityAttributeModifier.Operation operation) {
            //? if <=1.20.4 {
            /*delegate.addAttributeModifier(attribute, UUID.nameUUIDFromBytes(registry.id(name).toString().getBytes()).toString(), amount, operation);
             *///?} else if <=1.20.6 {
            /*delegate.addAttributeModifier(attribute, registry.modId + ".effect." + name, amount, operation);
             *///?} else {
            delegate.addAttributeModifier(attribute, registry.id("effect." + name), amount, operation);
            //? }

            return this;
        }

        public /*? if <=1.20.4 { *//*StatusEffect*//*? } else {*/RegistryEntry<StatusEffect>/*?}*/ register() {
            return registry.register(name, delegate);
        }
    }

    // This exists because StatusEffect::new is protected
    public static class SimpleEffect extends StatusEffect {
        public SimpleEffect(StatusEffectCategory category, int color) {
            super(category, color);
        }
    }
}