package com.codex.composer.api.v1.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class ComposerMultiLanguageProvider implements DataProvider {
    private final FabricDataOutput dataOutput;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
    private final LanguagePack languagePack = new LanguagePack();

    public ComposerMultiLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
        init(languagePack);
    }

    protected abstract void init(LanguagePack pack);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<String, Map<String, String>> allLanguageKeys = new HashMap<>();

        for (Map.Entry<String, List<ComposerLanguageProvider>> entry : languagePack.languageMap.entrySet()) {
            String langCode = entry.getKey();
            Map<String, String> translations = new HashMap<>();
            for (ComposerLanguageProvider provider : entry.getValue()) {
                provider.generateTranslations(registryLookup.join(), (key, value) -> {
                    Objects.requireNonNull(key);
                    Objects.requireNonNull(value);

                    if (translations.containsKey(key)) {
                        throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
                    }

                    translations.put(key, value);
                });
            }
            allLanguageKeys.put(langCode, translations);
        }

        if (languagePack.baseLanguage != null) {
            Map<String, String> baseKeys = allLanguageKeys.get(languagePack.baseLanguage);
            if (baseKeys == null) {
                throw new IllegalStateException("Base language " + languagePack.baseLanguage + " has no providers registered!");
            }

            for (Map.Entry<String, Map<String, String>> entry : allLanguageKeys.entrySet()) {
                String langCode = entry.getKey();
                if (langCode.equals(languagePack.baseLanguage)) continue;

                Map<String, String> translations = entry.getValue();

                Set<String> missing = new HashSet<>(baseKeys.keySet());
                missing.removeAll(translations.keySet());
                if (!missing.isEmpty()) {
                    System.err.println("Language " + langCode + " is missing keys: " + missing);
                }

                Set<String> extra = new HashSet<>(translations.keySet());
                extra.removeAll(baseKeys.keySet());
                if (!extra.isEmpty()) {
                    System.err.println("Language " + langCode + " has extra keys not in base: " + extra);
                }
            }
        }

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : allLanguageKeys.entrySet()) {
            String langCode = entry.getKey();
            Map<String, String> translations = entry.getValue();
            futures.add(writeLanguageFile(writer, langCode, translations));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<?> writeLanguageFile(DataWriter writer, String langCode, Map<String, String> translations) {
        com.google.gson.JsonObject langEntryJson = new com.google.gson.JsonObject();
        translations.forEach(langEntryJson::addProperty);

        return net.minecraft.data.DataProvider.writeToPath(
                writer,
                langEntryJson,
                getLangFilePath(langCode)
        );
    }

    private java.nio.file.Path getLangFilePath(String code) {
        return dataOutput
                .getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang")
                .resolveJson(Identifier.of(dataOutput.getModId(), code));
    }

    public static class LanguagePack {
        private final Map<String, List<ComposerSemiLanguageProvider>> languageMap = new HashMap<>();
        @Nullable private String baseLanguage;

        public <T extends ComposerSemiLanguageProvider> T addProvider(String languageCode, Supplier<T> provider) {
            languageMap.computeIfAbsent(languageCode, k -> new ArrayList<>()).add(provider.get());
            return provider.get();
        }

        public void setBaseLanguage(@Nullable String languageCode) {
            this.baseLanguage = languageCode;
        }
    }
}