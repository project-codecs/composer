package com.codex.composer.api.v1.datagen.lang;

import com.codex.composer.api.v1.datagen.ores.OreSet;
import com.codex.composer.api.v1.datagen.ores.OreTranslations;
import com.codex.composer.api.v1.registry.util.ComposedBlockFamily;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.util.misc.Translatable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

//? if <=1.20.4
//import com.codex.composer.api.v1.util.misc.TranslatableSoundEvent;

//? if >=1.20.6
import net.minecraft.registry.entry.RegistryEntry;

public abstract class SemiLanguageProvider {
    protected final Set<String> set = new HashSet<>();
    protected FabricLanguageProvider.TranslationBuilder builder;
    protected String prefix = "";
    private boolean prefixInlined = false;
    protected String suffix = "";
    private boolean suffixInlined = false;

    public void setBuilder(FabricLanguageProvider.TranslationBuilder builder) {
        this.builder = builder;
    }
    public abstract void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup);

    public String prefix() { return prefix; }
    public String suffix() { return suffix; }

    public void prefix(String prefix) { this.prefix = prefix; prefixInlined = prefix != null && !prefix.isBlank(); }
    public void suffix(String suffix) { this.suffix = suffix; suffixInlined = suffix != null && !suffix.isBlank(); }

    public void stat(Identifier identifier, String value) {
        add("stat.%s.%s".formatted(identifier.getNamespace(), identifier.getPath()), value, false);
    }

    public void group(RegistryKey<ItemGroup> key, String value) {
        add(String.format("itemGroup.%s", key.getValue().getPath()), value, false);
    }

    public <T extends Enum<T> & Translatable> void translatableEnum(Class<T> tClass, String... values) {
        if (tClass.getEnumConstants() == null) throw new IllegalArgumentException("translatableEnum used on a non-enum class!");
        if (tClass.getEnumConstants().length != values.length) throw new IllegalArgumentException("translatableEnum requires the same amount of values as the given enum has!");

        for (int i = 0; i < tClass.getEnumConstants().length; i++) {
            add(tClass.getEnumConstants()[i], values[i]);
        }
    }

    public FamilyTranslationBuilder family(ComposedBlockFamily family) {
        return new FamilyTranslationBuilder(family, (block, name, hasItem) -> {
            if (hasItem) block(block, name);
            else builder.add(block, name);
        });
    }

    public FamilyTranslationBuilder baseEnglishFamily(ComposedBlockFamily family, String baseNamePlural, String baseNameSingular) {
        return FamilyTranslationBuilder.baseEnglish(family, (block, name, hasItem) -> {
            if (hasItem) block(block, name);
            else builder.add(block, name);
        }, baseNamePlural, baseNameSingular);
    }

    public FamilyTranslationBuilder baseEnglishFamily(ComposedBlockFamily family, String baseName) {
        return baseEnglishFamily(family, baseName, baseName);
    }

    public OreTranslations ore(OreSet set) {
        return new OreTranslations(set, (variant, name) -> {
            if (variant.isBlock()) block(set.tBlock(variant), name);
            else item(set.tItem(variant), name);
        });
    }

    public OreTranslations baseEnglishOre(OreSet set, String baseName) {
        return OreTranslations.baseEnglish(set, ((variant, name) -> {
            if (variant.isBlock()) block(set.tBlock(variant), name);
            else item(set.tItem(variant), name);
        }), baseName);
    }

    //? if <=1.20.4 {
    /*public void sound(TranslatableSoundEvent sound, String value) {
        add(sound, value);
    }
    *///?}

    public void sound(SoundEvent sound, String value) {
        add(sound./*? if >=1.21.3 { */id/*?} else {*//*getId*//*? }*/(), value);
    }

    public void add(Translatable translatable, String value) {
        add(translatable, "", value);
    }

    public void add(Translatable translatable, String prefix, String value) {
        add(translatable, prefix, "", value);
    }

    public void add(Translatable translatable, String prefix, String suffix, String value) {
        add(translatable.getTranslationKey(prefix, suffix), value, false);
    }

    public void bind(KeyBinding bind, String value) {
        //? if >=1.21.9 {
        /*add(bind.getId(), value);
        *///? } else {
        add(bind.getTranslationKey(), value, false);
        //? }
    }

    //? if >=1.21.9 {
    /*public void bind_category(KeyBinding.Category category, String value) {
        add(String.format("key.category.%s.%s", category.id().getNamespace(), category.id().getPath()), value, false);
    }
    *///? } else {
    public void bind(KeyBinding bind, String keyTranslation, String categoryTranslation) {
        bind(bind, keyTranslation);
        bind_category(bind, categoryTranslation);
    }

    public void bind_category(KeyBinding bind, String value) {
        add(bind.getCategory(), value);
    }
    //? }

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

    public void item(ItemConvertible item, String value) {
        builder.add(item.asItem(), value);
    }

    public void block(Block block, String value) {
        builder.add(block, value);
        //? if >=1.21.3
        item(block, value);
    }

    public void registryKey(RegistryKey<ItemGroup> registryKey, String value) {
        builder.add(registryKey, value);
    }

    public void entity(EntityType<?> entityType, String value) {
        builder.add(entityType, value);
    }

    //? if <=1.20.4 {
    /*public void enchantment(Enchantment enchantment, String value) {
        builder.add(enchantment, value);
    }

    public void attribute(EntityAttribute entityAttribute, String value) {
        builder.add(entityAttribute, value);
    }
    *///?} else if <=1.20.6 {
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

    //? if >=1.20.6 {
    public void tag(TagKey<?> key, String value) {
        builder.add(key, value);
    }
    //? }

    public void add(Identifier identifier, String value) {
        builder.add(identifier, value);
    }

    public void append(Path existingLanguageFile) throws IOException {
        builder.add(existingLanguageFile);
    }
}
