package com.codex.composer.api.v1.datagen;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.util.misc.Translatable;

import java.util.HashSet;
import java.util.Set;

//? if minecraft: <=1.20.4 {
/*import com.codex.composer.api.v1.util.misc.TranslatableSoundEvent;
*///? } else {
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import net.minecraft.registry.entry.RegistryEntry;
//?}

//? legacy {
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//? } else
//import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import java.io.IOException;
import java.nio.file.Path;

public abstract class ComposerLanguageProvider extends FabricLanguageProvider {
    protected final Set<String> set = new HashSet<>();
    protected FabricLanguageProvider.TranslationBuilder builder;
    protected String prefix = "";
    private boolean prefixInlined = false;
    protected String suffix = "";
    private boolean suffixInlined = false;

    //? if minecraft: <=1.20.4 {
    /*public ComposerLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    *///? } else {
    public ComposerLanguageProvider(/*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    //? }

    //? if minecraft: <=1.20.4
    //@Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        this.builder = translationBuilder;
        generate();
    }

    //? if minecraft: >=1.20.6 {
    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        generateTranslations(translationBuilder);
    }
    //?}

    public abstract void generate();

    public String prefix() { return prefix; }
    public String suffix() { return suffix; }

    public void prefix(String prefix) { this.prefix = prefix; prefixInlined = prefix != null && !prefix.isBlank(); }
    public void suffix(String suffix) { this.suffix = suffix; suffixInlined = suffix != null && !suffix.isBlank(); }

    // Custom

    public void stat(Identifier identifier, String value) {
        add("stat.%s.%s".formatted(identifier.getNamespace(), identifier.getPath()), value);
    }

    public void group(RegistryKey<ItemGroup> key, String value) {
        add(String.format("itemGroup.%s", key.getValue().getPath()), value);
    }

    public <T extends Enum<T> & EnumTranslatable> void enumTranslatable(Class<T> enumClass, String... values) {
        T[] constants = enumClass.getEnumConstants();
        if (constants.length != values.length) {
            throw new IllegalArgumentException(
                    "Expected " + constants.length + " values, but got " + values.length
            );
        }

        String prefix = constants[0].prefix();
        for (int i = 0; i < constants.length; i++) add("%s.%s".formatted(prefix, constants[i].name()), values[i]);
    }

    //? if minecraft: <=1.20.4 {
    /*public void sound(TranslatableSoundEvent sound, String value) {
        add(sound, value);
    }
    *///?}

    public void sound(SoundEvent sound, String value) {
        add(sound./*? if minecraft: >=1.21.3 { */id/*?} else {*//*getId*//*? }*/(), value);
    }

    public void add(Translatable translatable, String value) {
        add(translatable, "", value);
    }

    public void add(Translatable translatable, String prefix, String value) {
        add(translatable, prefix, "", value);
    }

    public void add(Translatable translatable, String prefix, String suffix, String value) {
        add(translatable.getTranslationKey(prefix, suffix), value);
    }

    @Deprecated(since = "4.2", forRemoval = true)
    public void prefix(String key, String value) {
        add(prefix() + (prefix().isBlank() ? "" : ".") + key, value);
    }

    @Deprecated(since = "4.2", forRemoval = true)
    public void suffix(String key, String value) {
        add(key + (suffix().isBlank() ? "" : ".") + suffix(), value);
    }

    @Deprecated(since = "4.2", forRemoval = true)
    public void surround(String key, String value) {
        add("%s%s%s%s%s".formatted(prefix(), prefix().isBlank() ? "" : ".", key, suffix().isBlank() ? "" : ".", suffix()), value);
    }

    public void pre(String key, String value) {
        add(prefix() + (prefix().isBlank() ? "" : ".") + key, value, false);
    }

    public void suf(String key, String value) {
        add(key + (suffix().isBlank() ? "" : ".") + suffix(), value, false);
    }

    public void sur(String key, String value) {
        add("%s%s%s%s%s".formatted(prefix(), prefix().isBlank() ? "" : ".", key, suffix().isBlank() ? "" : ".", suffix()), value, false);
    }

    public void add(String key, String value) {
        add(key, value, true);
    }

    public void add(String key, String value, boolean append) {
        if (set.contains(key) && (!append && (prefixInlined || suffixInlined))) return;
        set.add(key);

        if (append && prefixInlined && suffixInlined) sur(key, value);
        else if (append && prefixInlined) pre(key, value);
        else if (append && suffixInlined) suf(key, value);
        else builder.add(key, value);
    }

    // Vanilla redirect

    public void item(ItemConvertible item, String value) {
        builder.add(item.asItem(), value);
    }

    public void block(Block block, String value) {
        builder.add(block, value);
        //? if minecraft: >=1.21.3
        item(block, value);
    }

    public void registryKey(RegistryKey<ItemGroup> registryKey, String value) {
        builder.add(registryKey, value);
    }

    public void entity(EntityType<?> entityType, String value) {
        builder.add(entityType, value);
    }

    //? if minecraft: <=1.20.4 {
    /*public void enchantment(Enchantment enchantment, String value) {
        builder.add(enchantment, value);
    }

    public void attribute(EntityAttribute entityAttribute, String value) {
        builder.add(entityAttribute, value);
    }
    *///?} else if minecraft: <=1.20.6 {
    /*public void enchantment(Enchantment enchantment, String value) {
        builder.add(enchantment, value);
    }

    public void attribute(RegistryEntry<EntityAttribute> entityAttribute, String value) {
        builder.add(entityAttribute, value);
    }
    *///?} else {
    public void enchantment(RegistryKey<Enchantment> enchantment, String value) {
        builder.addEnchantment(enchantment, value);
    }

    public void attribute(RegistryEntry<EntityAttribute> entityAttribute, String value) {
        builder.add(entityAttribute, value);
    }
    //?}

    public void stat(StatType<?> statType, String value) {
        builder.add(statType, value);
    }

    public void effect(StatusEffect statusEffect, String value) {
        builder.add(statusEffect, value);
    }

    public void add(Identifier identifier, String value) {
        builder.add(identifier, value);
    }

    public void append(Path existingLanguageFile) throws IOException {
        builder.add(existingLanguageFile);
    }
}
