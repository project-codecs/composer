package com.codex.composer.api.v1.registry.lazy;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import com.codex.composer.internal.Composer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.registry.Registries.ROOT;

//? if minecraft: >=1.20.6 {
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.Bootstrap;
import com.mojang.serialization.Codec;
//? }

@SuppressWarnings("unchecked")
public class DeferredRegistryRegistry {
    private final Map<Identifier, Supplier<?>> defaultEntries = new LinkedHashMap<>();

    //? if minecraft: <=1.20.4 {
    /*public <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Initializer<T> initializer) {
        return create(key, Lifecycle.stable(), initializer);
    }

    public <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, Initializer<T> initializer) {
        return create(key, defaultId, Lifecycle.stable(), initializer);
    }

    public <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, String defaultId, Initializer<T> initializer) {
        return createIntrusive(key, defaultId, Lifecycle.stable(), initializer);
    }

    public <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, Initializer<T> initializer) {
        return create(key, (MutableRegistry<T>) (new SimpleRegistry<>(key, lifecycle, false)), initializer, lifecycle);
    }

    public <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, Lifecycle lifecycle, Initializer<T> initializer) {
        return (DefaultedRegistry<T>) create(key, (MutableRegistry<T>) (new SimpleDefaultedRegistry<>(defaultId, key, lifecycle, false)), initializer, lifecycle);
    }

    public <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, String defaultId, Lifecycle lifecycle, Initializer<T> initializer) {
        return (DefaultedRegistry<T>) create(key, (MutableRegistry<T>) (new SimpleDefaultedRegistry<>(defaultId, key, lifecycle, true)), initializer, lifecycle);
    }

    public <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Initializer<T> initializer, Lifecycle lifecycle) {
        Identifier identifier = key.getValue();
        defaultEntries.put(identifier, (Supplier<?>) () -> initializer.run(registry));
        //noinspection rawtypes
        ROOT.add((RegistryKey) key, registry, lifecycle);
        return registry;
    }
    *///? } else {
    public  <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Initializer<T> initializer) {
        return create(key, (MutableRegistry<T>) (new SimpleRegistry<>(key, Lifecycle.stable(), false)), initializer);
    }

    public <T> Registry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, Initializer<T> initializer) {
        return create(key, (MutableRegistry<T>) (new SimpleRegistry<>(key, Lifecycle.stable(), true)), initializer);
    }

    public <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, Initializer<T> initializer) {
        return (DefaultedRegistry<T>) create(key, (MutableRegistry<T>) (new SimpleDefaultedRegistry<>(defaultId, key, Lifecycle.stable(), false)), initializer);
    }

    public <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, String defaultId, Initializer<T> initializer) {
        return (DefaultedRegistry<T>) create(key, (MutableRegistry<T>) (new SimpleDefaultedRegistry<>(defaultId, key, Lifecycle.stable(), true)), initializer);
    }

    public <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Initializer<T> initializer) {
        Bootstrap.ensureBootstrapped(() -> "registry " + key);
        Identifier identifier = key.getValue();
        defaultEntries.put(identifier, (Supplier<?>) () -> initializer.run(registry));
        //noinspection rawtypes
        ROOT.add((RegistryKey) key, registry, RegistryEntryInfo.DEFAULT);
        return registry;
    }

    public <T> RegistryKey<Registry<T>> createDynamic(Identifier id, Codec<T> codec) {
        RegistryKey<Registry<T>> key = RegistryKey.ofRegistry(id);
        DynamicRegistries.register(key, codec);
        return key;
    }

    public <T> RegistryKey<Registry<T>> createDynamicSynced(Identifier id, Codec<T> codec, DynamicRegistries.SyncOption... options) {
        RegistryKey<Registry<T>> key = RegistryKey.ofRegistry(id);
        DynamicRegistries.registerSynced(key, codec, options);
        return key;
    }
    //? }

    public void finalizeRegistries() {
        defaultEntries.forEach((id, initializer) -> {
            if (initializer.get() == null) {
                Composer.LOGGER.error("Unable to bootstrap registry '{}'", id);
            }
        });
    }

    @FunctionalInterface
    public interface Initializer<T> {
        T run(Registry<T> registry);
    }
}
