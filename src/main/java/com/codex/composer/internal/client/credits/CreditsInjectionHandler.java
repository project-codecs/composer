package com.codex.composer.internal.client.credits;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;

import java.util.*;

public class CreditsInjectionHandler {
    private static final String KEY = "composer-credits";

    public static JsonArray inject(JsonArray original) {
        List<JsonObject> base = new ArrayList<>();
        original.forEach(e -> base.add(e.getAsJsonObject()));

        List<Injection> injections = new ArrayList<>();

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            CustomValue value = mod.getMetadata().getCustomValue(KEY);
            if (value == null || value.getType() != CustomValue.CvType.ARRAY) continue;

            for (CustomValue entry : value.getAsArray()) {
                JsonObject obj = convert(entry).getAsJsonObject();

                Injection injection = new Injection(
                        obj.has("after") ? obj.get("after").getAsString() : null,
                        obj.has("before") ? obj.get("before").getAsString() : null,
                        obj
                );

                injections.add(injection);
            }
        }

        for (Injection injection : injections) {
            int index = findIndex(base, injection);

            if (index != -1) {
                base.add(index, injection.payload);
            } else {
                base.add(injection.payload);
            }
        }

        JsonArray result = new JsonArray();
        base.forEach(result::add);
        return result;
    }

    private static int findIndex(List<JsonObject> base, Injection injection) {
        if ("-".equals(injection.after) || "-".equals(injection.before)) {
            return 0;
        }

        for (int i = 0; i < base.size(); i++) {
            String section = base.get(i).get("section").getAsString();

            if (section.equals(injection.after)) {
                return i + 1;
            }

            if (section.equals(injection.before)) {
                return i;
            }
        }
        return -1;
    }

    private static JsonElement convert(CustomValue value) {
        return switch (value.getType()) {
            case OBJECT -> {
                JsonObject obj = new JsonObject();
                value.getAsObject().forEach((k) -> obj.add(k.getKey(), convert(k.getValue())));
                yield obj;
            }
            case ARRAY -> {
                JsonArray arr = new JsonArray();
                value.getAsArray().forEach(v -> arr.add(convert(v)));
                yield arr;
            }
            case STRING -> new JsonPrimitive(value.getAsString());
            case NUMBER -> new JsonPrimitive(value.getAsNumber());
            case BOOLEAN -> new JsonPrimitive(value.getAsBoolean());
            case NULL -> JsonNull.INSTANCE;
        };
    }

    private record Injection(String after, String before, JsonObject payload) {}
}