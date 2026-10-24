package com.codex.composer.internal.client.config;

//? if minecraft: <=1.21.6 {
import com.codex.composer.api.v1.util.data.JsonUtils;
import com.codex.composer.api.v1.util.misc.Translatable;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TranslatableOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

public class DupedBindsConfig {
    private DupedBindsConfig() {
        if (!Files.exists(CONFIG_FILE)) return;
        load();
    }

    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("composer-client.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static DupedBindsConfig INSTANCE;

    public SimpleOption<Boolean> rainbowEffectOnDuplicateKeybinds = SimpleOption.ofBoolean("option.composer.rainbow_dupes", SimpleOption.emptyTooltip(), false, b -> save());
    public SimpleOption<Integer> rainbowEffectSpeed = new SimpleOption<>("option.composer.rainbow_speed", SimpleOption.emptyTooltip(), GameOptions::getGenericValueText, new SimpleOption.ValidatingIntSliderCallbacks(1, 10), 3, ignored -> save());
    public SimpleOption<BindsMode> allowDuplicateKeybinds = new SimpleOption<>("option.composer.dupes_mode", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(), new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(BindsMode.values()), BindsMode.CODEC), BindsMode.MC_AND_CM, mode -> save());

    public static void initialize() {
        INSTANCE = new DupedBindsConfig();
    }

    public void load() {
        try {
            JsonObject json = GSON.fromJson(Files.readString(CONFIG_FILE), JsonObject.class);
            JsonUtils.ifBoolPresent(json, "rainbow_dupes", rainbowEffectOnDuplicateKeybinds::setValue);
            JsonUtils.ifIntPresent(json, "rainbow_speed", rainbowEffectSpeed::setValue);
            JsonUtils.ifStrPresent(json, "dupes_mode", str -> JsonUtils.ifEnum(new JsonPrimitive(str), BindsMode.ALL::fromString, allowDuplicateKeybinds::setValue));
        } catch (IOException ignored) {}
    }

    public void save() {
        JsonObject root = new JsonObject();
        root.addProperty("rainbow_dupes", rainbowEffectOnDuplicateKeybinds.getValue());
        root.addProperty("rainbow_speed", rainbowEffectSpeed.getValue());
        root.addProperty("dupes_mode", allowDuplicateKeybinds.getValue().asString());
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            Files.writeString(CONFIG_FILE, GSON.toJson(root));
        } catch (IOException ignored) {}
    }

    public SimpleOption<?>[] allOptions() {
        return new SimpleOption[]{ allowDuplicateKeybinds, rainbowEffectOnDuplicateKeybinds, rainbowEffectSpeed };
    }

    public enum BindsMode implements TranslatableOption, Translatable, StringIdentifiable, JsonUtils.EnumSerializable<BindsMode> {
        NONE, MC_AND_CM, ALL;

        public static final Codec<BindsMode> CODEC = StringIdentifiable.createCodec(BindsMode::values);

        BindsMode() {
        }

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public int getId() {
            return ordinal();
        }

        @Override
        public String getTranslationKey() {
            return "config.composer.binds_mode." + asString();
        }
    }
}
//? }
