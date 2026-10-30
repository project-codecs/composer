package com.codex.composer.api.v1.registry.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DynamicRegistryUtils {
    @Environment(EnvType.CLIENT)
    public static <T> Optional<Registry<T>> getRegistryClient(RegistryKey<Registry<T>> key) {
        return ClientAccess.getRegistry(key);
    }

    public static <T> Optional<Registry<T>> getRegistry(@Nullable World world, RegistryKey<Registry<T>> key) {
        if (world == null) return Optional.empty();
        return getRegistry(world.getRegistryManager(), key);
    }

    public static <T> Optional<Registry<T>> getRegistry(@Nullable MinecraftServer server, RegistryKey<Registry<T>> key) {
        if (server == null) return Optional.empty();
        return getRegistry(server.getRegistryManager(), key);
    }

    public static <T> Optional<Registry<T>> getRegistry(@Nullable DynamicRegistryManager manager, RegistryKey<Registry<T>> key) {
        if (manager == null) return Optional.empty();
        return manager.getOptional(key);
    }

    public static <T> Registry<T> getRegistryOrThrow(World world, RegistryKey<Registry<T>> key) {
        return getRegistry(world, key).orElseThrow(() -> new IllegalStateException("Registry not found: " + key.getValue()));
    }

    public static <T> Registry<T> getRegistryOrThrow(DynamicRegistryManager manager, RegistryKey<Registry<T>> key) {
        return getRegistry(manager, key).orElseThrow(() -> new IllegalStateException("Registry not found: " + key.getValue()));
    }

    //~ if minecraft: <=1.21 'getOptional' -> 'getOrEmpty' {
    public static <T> Optional<T> getValue(World world, RegistryKey<Registry<T>> key, Identifier id) {
        return getRegistry(world, key).flatMap(reg -> reg.getOptional(RegistryKey.of(key, id)))/*? if minecraft: >=1.21.3 {*/.map(RegistryEntry.Reference::value)/*?}*/;
    }

    public static <T> Optional<T> getValue(DynamicRegistryManager manager, RegistryKey<Registry<T>> key, Identifier id) {
        return getRegistry(manager, key).flatMap(reg -> reg.getOptional(RegistryKey.of(key, id)))/*? if minecraft: >=1.21.3 {*/.map(RegistryEntry.Reference::value)/*?}*/;
    }

    public static <T> T getValueOrThrow(World world, RegistryKey<Registry<T>> key, Identifier id) {
        return getValue(world, key, id).orElseThrow(() -> new IllegalStateException("Missing registry entry: " + id + " in " + key.getValue()));
    }
    //~}

    //? if minecraft: >= 1.21.3 {
    public static <T> Optional<RegistryEntry.Reference<T>> getEntry(World world, RegistryKey<Registry<T>> key, Identifier id) {
        return getRegistry(world, key).flatMap(reg -> reg.getOptional(RegistryKey.of(key, id)));
    }

    public static <T> Optional<RegistryEntry.Reference<T>> getEntry(DynamicRegistryManager manager, RegistryKey<Registry<T>> key, Identifier id) {
        return getRegistry(manager, key).flatMap(reg -> reg.getOptional(RegistryKey.of(key, id)));
    }

    public static <T> RegistryEntry.Reference<T> getEntryOrThrow(World world, RegistryKey<Registry<T>> key, Identifier id) {
        return getEntry(world, key, id).orElseThrow(() -> new IllegalStateException("Missing registry entry: " + id + " in " + key.getValue()));
    }
    //? }

    public static <T> boolean contains(World world, RegistryKey<Registry<T>> key, Identifier id) {
        return getRegistry(world, key).map(reg -> reg.containsId(id)).orElse(false);
    }

    public static <T> Optional<Identifier> getId(World world, RegistryKey<Registry<T>> key, T value) {
        return getRegistry(world, key).map(reg -> reg.getId(value));
    }

    public static <T> Optional<RegistryKey<T>> getKey(World world, RegistryKey<Registry<T>> key, T value) {
        return getRegistry(world, key).flatMap(reg -> reg.getKey(value));
    }

    public static <T> List<Identifier> getIds(World world, RegistryKey<Registry<T>> key) {
        return getRegistry(world, key).map(reg -> List.copyOf(reg.getIds())).orElseGet(List::of);
    }

    //~ if minecraft: <=1.21 'RegistryWrapper::streamEntries' -> 'Registry::streamEntries' {
    public static <T> Stream<RegistryEntry.Reference<T>> streamEntries(World world, RegistryKey<Registry<T>> key) {
        return getRegistry(world, key).map(RegistryWrapper::streamEntries).orElseGet(Stream::empty);
    }
    //~}

    //~ if minecraft: <=1.21 'getOptional' -> 'getEntryList' {
    public static <T> List<RegistryEntry<T>> getTagEntries(World world, RegistryKey<Registry<T>> key, TagKey<T> tag) {
        return getRegistry(world, key).flatMap(reg -> reg.getOptional(tag)).map(named -> named.stream().toList()).orElseGet(List::of);
    }
    //~}

    public static <T> boolean isInTag(World world, RegistryKey<Registry<T>> key, T value, TagKey<T> tag) {
        return getRegistry(world, key).map(reg -> reg.getEntry(value).isIn(tag)).orElse(false);
    }

    //? if minecraft: >=1.20.6 {
    //~ if minecraft: <=1.21 'access.getOptional' -> 'access.getOptionalWrapper' {
    public static <T> Optional<? extends RegistryWrapper.Impl<T>> getCommandRegistry(CommandRegistryAccess access, RegistryKey<Registry<T>> key) {
        return access.getOptional(key);
    }

    public static <T> Optional<RegistryEntry.Reference<T>> getEntryFromCommandRegistry(CommandRegistryAccess access, RegistryKey<Registry<T>> key, Identifier id) {
        return access.getOptional(key).flatMap(reg -> reg.getOptional(RegistryKey.of(key, id)));
    }
    //~}

    public static <T> Optional<T> getFromCommandRegistry(CommandRegistryAccess access, RegistryKey<Registry<T>> key, Identifier id) {
        return getEntryFromCommandRegistry(access, key, id).map(RegistryEntry.Reference::value);
    }
    //?}

    @Environment(EnvType.CLIENT)
    private static final class ClientAccess {
        static <T> Optional<Registry<T>> getRegistry(RegistryKey<Registry<T>> key) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.world == null) return Optional.empty();
            return DynamicRegistryUtils.getRegistry(client.world, key);
        }
    }
}
