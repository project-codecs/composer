package com.codex.composer.api.v1.feature.state;

import com.google.gson.*;
import net.minecraft.server.MinecraftServer;
import com.codex.composer.api.v1.data.GsonSerializer;
import com.codex.composer.mixin.accessor.MinecraftServerMethodAccessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("CallToPrintStackTrace")
public class FeatureState {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "composer_features_v2.json";

    private final Map<String, Boolean> enabled = new LinkedHashMap<>();
    private final Map<String, JsonObject> configs = new LinkedHashMap<>();

    private final File file;

    private FeatureState(File file) {
        this.file = file;
        load();
    }

    private void load() {
        if (!file.exists()) return;

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (root.has("enabled")) {
                root.getAsJsonObject("enabled").entrySet()
                        .forEach(e -> enabled.put(e.getKey(), e.getValue().getAsBoolean()));
            }

            if (root.has("configs")) {
                root.getAsJsonObject("configs").entrySet()
                        .forEach(e -> configs.put(e.getKey(), e.getValue().getAsJsonObject()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JsonObject root = new JsonObject();

        JsonObject en = new JsonObject();
        enabled.forEach(en::addProperty);
        root.add("enabled", en);

        JsonObject confs = new JsonObject();
        configs.forEach(confs::add);
        root.add("configs", confs);

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FeatureState get(MinecraftServer server) {
        File worldFolder = ((MinecraftServerMethodAccessor) server).composer$getWorldDirectory().resolve("data").toFile();
        File file = new File(worldFolder, FILE_NAME);
        return new FeatureState(file);
    }

    public Boolean getEnabled(String id) {
        return enabled.getOrDefault(id, null);
    }

    public void setEnabled(String id, boolean value) {
        enabled.put(id, value);
        save();
    }

    public void ensureEnabledDefault(String id, boolean def) {
        enabled.putIfAbsent(id, def);
        save();
    }

    public void pruneUnknown(Set<String> registeredIds) {
        enabled.keySet().removeIf(k -> !registeredIds.contains(k));
        configs.keySet().removeIf(k -> !registeredIds.contains(k));
        save();
    }

    public JsonObject getConfig(String id) {
        return configs.computeIfAbsent(id, k -> new JsonObject());
    }

    public void setConfigValue(String id, String key, JsonElement value) {
        JsonObject obj = configs.computeIfAbsent(id, k -> new JsonObject());
        obj.add(key, value);
        save();
    }

    public JsonElement getConfigValue(String id, String key, JsonElement fallback) {
        JsonObject obj = configs.get(id);
        if (obj == null || !obj.has(key)) return fallback;
        return obj.get(key);
    }

    public <T> void ensureDefaultConfig(String id, String key, GsonSerializer<T> serializer, Object defaultValue) {
        JsonObject obj = configs.computeIfAbsent(id, k -> new JsonObject());
        if (!obj.has(key)) {
            @SuppressWarnings("unchecked")
            T value = (T) defaultValue;
            obj.add(key, serializer.writeToJson(value));
            save();
        }
    }

}
