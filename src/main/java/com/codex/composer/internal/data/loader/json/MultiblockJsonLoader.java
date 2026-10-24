package com.codex.composer.internal.data.loader.json;

import com.codex.composer.api.v1.util.data.JsonUtils;
import com.codex.composer.api.v1.util.misc.CubeMatrix;
import com.codex.composer.internal.Composer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class MultiblockJsonLoader {
    private MultiblockJsonLoader() {}

    @Nullable
    public static ParsedMultiblock parse(Identifier fileId, Resource resource) {
        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (!(element instanceof JsonObject root)) {
                Composer.LOGGER.warn("Multiblock file {} is empty or not a JSON object!", fileId);
                return null;
            }

            return process(fileId, root);
        } catch (IOException | JsonParseException e) {
            Composer.LOGGER.warn("Failed to read multiblock file {}!", fileId, e);
            return null;
        }
    }

    @Nullable
    private static ParsedMultiblock process(Identifier fileId, JsonObject root) {
        String rawId = JsonUtils.getStr(root, "id", null);
        Identifier id = rawId == null ? null : Identifier.tryParse(rawId);
        if (id == null) {
            Composer.LOGGER.warn("Invalid identifier for multiblock {}!", fileId);
            return null;
        }

        JsonObject controller = JsonUtils.get(root, "controller_pos", null);
        if (controller == null) {
            Composer.LOGGER.warn("Multiblock {} is missing 'controller_pos'!", id);
            return null;
        }
        Vec3i controllerPos = new Vec3i(JsonUtils.getInt(controller, "x"), JsonUtils.getInt(controller, "y"), JsonUtils.getInt(controller, "z"));

        Map<Character, ParsedPattern> pattern = new LinkedHashMap<>();
        JsonUtils.get(root, "pattern").entrySet().forEach(entry -> {
            if (entry.getKey().length() != 1) {
                Composer.LOGGER.warn("Multiblock {}: pattern key '{}' must be a single character!", id, entry.getKey());
                return;
            }
            char symbol = entry.getKey().charAt(0);
            JsonElement value = entry.getValue();

            if (value instanceof JsonObject obj) {
                Set<Identifier> extensions = new LinkedHashSet<>();
                for (String raw : JsonUtils.asStringList(obj.get("extensions"))) {
                    Identifier extId = qualify(id, raw);
                    if (extId == null) Composer.LOGGER.warn("Multiblock {}: invalid extension id '{}' on key '{}'!", id, raw, symbol);
                    else extensions.add(extId);
                }
                pattern.put(symbol, new ParsedPattern(JsonUtils.asStringList(obj.get("block")), extensions));
            } else {
                pattern.put(symbol, new ParsedPattern(JsonUtils.asStringList(value), Set.of()));
            }
        });

        CubeMatrix<Character> matrix = new CubeMatrix<>();
        int y = 0;
        for (JsonElement layerElement : JsonUtils.getArray(root, "layers")) {
            List<String> rows = JsonUtils.asStringList(layerElement);

            for (int z = 0; z < rows.size(); z++) {
                String row = rows.get(z);
                for (int x = 0; x < row.length(); x++) {
                    matrix.set(new Vector3i(x, y, rows.size() - 1 - z), row.charAt(x));
                }
            }
            y++;
        }

        if (y == 0) {
            Composer.LOGGER.warn("Multiblock {} has no layers!", id);
            return null;
        }

        Map<Identifier, ParsedExtension> extensions = new LinkedHashMap<>();
        JsonUtils.get(root, "extensions").entrySet().forEach(entry -> {
            Identifier extId = qualify(id, entry.getKey());
            if (extId == null) {
                Composer.LOGGER.warn("Multiblock {}: invalid extension id '{}'!", id, entry.getKey());
                return;
            }

            JsonUtils.ifObject(entry.getValue(), obj -> extensions.put(extId, new ParsedExtension(
                    JsonUtils.asStringList(obj.get("block")),
                    JsonUtils.getInt(obj, "min", 0),
                    JsonUtils.getInt(obj, "max", Integer.MAX_VALUE)
            )));
        });

        return new ParsedMultiblock(id, matrix, pattern, extensions, controllerPos);
    }

    @Nullable
    private static Identifier qualify(Identifier multiblockId, String raw) {
        return Identifier.tryParse(raw.contains(":") ? raw : multiblockId.getNamespace() + ":" + raw);
    }

    public record ParsedMultiblock(Identifier id, CubeMatrix<Character> matrix, Map<Character, ParsedPattern> pattern, Map<Identifier, ParsedExtension> extensions, Vec3i controllerPos) {}
    public record ParsedPattern(List<String> blocks, Set<Identifier> extensions) {}
    public record ParsedExtension(List<String> blocks, int min, int max) {}
}