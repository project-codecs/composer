package com.codex.composer.api.v1.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//? legacy {
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//? } else
//import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

public abstract class ComposerMultiblockProvider implements DataProvider {
    private final List<CompletableFuture<?>> futures = new ArrayList<>();
    private final /*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ output;

    public ComposerMultiblockProvider(/*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generate(writer);
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[]{}));
    }

    public abstract void generate(DataWriter writer);

    protected MultiblockJsonBuilder create(Identifier id) {
        return new MultiblockJsonBuilder(this, id);
    }

    @Override
    public String getName() {
        return "Multiblocks";
    }

    public static class MultiblockJsonBuilder {
        private final ComposerMultiblockProvider provider;
        private final Identifier id;
        private final List<List<String>> layers = new ArrayList<>();
        private final Map<Character, JsonElement> pattern = new HashMap<>();
        private Vec3i controllerPos = null;
        private int rowWidth = -1;
        private int layerWidth = -1;

        private MultiblockJsonBuilder(ComposerMultiblockProvider provider, Identifier id) {
            this.provider = provider;
            this.id = id;
        }

        public LayerBuilder layer() {
            return new LayerBuilder(this, rowWidth);
        }

        public PatternBuilder pattern(char character) {
            return new PatternBuilder(this, character);
        }

        public MultiblockJsonBuilder controller(Vec3i pos) {
            this.controllerPos = pos;
            return this;
        }

        private MultiblockJsonBuilder push(LayerBuilder builder) {
            if (rowWidth == -1) rowWidth = builder.rowWidth;
            if (layerWidth == -1) layerWidth = builder.rows.size();
            else if (builder.rows.size() != layerWidth) throw new IllegalArgumentException("All layers must contain the same amount of rows! Use spaces as air if needed.");
            layers.add(builder.rows);
            return this;
        }

        private MultiblockJsonBuilder push(char character, JsonElement obj) {
            pattern.put(character, obj);
            return this;
        }

        public void write(DataWriter writer) {
            if (controllerPos == null) throw new IllegalStateException("'controllerPos' must be set for a multiblock!");
            JsonObject json = new JsonObject();
            JsonArray layers = new JsonArray();
            for (List<String> layer : this.layers) {
                JsonArray rows = new JsonArray();
                layer.forEach(rows::add);
                layers.add(rows);
            }

            JsonObject pattern = new JsonObject();
            this.pattern.forEach((key, value) -> pattern.add(key.toString(), value));

            this.layers.forEach(layer -> layer.forEach(row -> {
                for (int i = 0; i < row.length(); i++) {
                    char chr = row.charAt(i);
                    if (chr != ' ' && chr != '*' && !pattern.keySet().contains(String.valueOf(chr))) throw new IllegalArgumentException("Unknown character '" + chr + "' in multiblock layer!");
                }
            }));

            JsonObject controllerPos = new JsonObject();
            controllerPos.addProperty("x", this.controllerPos.getX());
            controllerPos.addProperty("y", this.controllerPos.getY());
            controllerPos.addProperty("z", this.controllerPos.getZ());

            json.addProperty("id", id.toString());
            json.add("layers", layers);
            json.add("pattern", pattern);
            json.add("controller_pos", controllerPos);

            provider.futures.add(DataProvider.writeToPath(
                    writer,
                    json,
                    provider.output.getPath().resolve("data/" + id.getNamespace() + "/multiblocks/" + id.getPath() + ".json")
            ));
        }

        public static class PatternBuilder {
            private final MultiblockJsonBuilder parent;
            private final char character;

            private PatternBuilder(MultiblockJsonBuilder parent, char character) {
                this.parent = parent;
                this.character = character;
            }

            public MultiblockJsonBuilder block(Block block) {
                return parent.push(character, new JsonPrimitive(Registries.BLOCK.getId(block).toString()));
            }

            public MultiblockJsonBuilder tag(TagKey<Block> tag) {
                return parent.push(character, new JsonPrimitive("#" + tag.id()));
            }
        }

        public static class LayerBuilder {
            private final MultiblockJsonBuilder parent;
            private final List<String> rows = new ArrayList<>();
            private int rowWidth;

            private LayerBuilder(MultiblockJsonBuilder parent, int rowWidth) {
                this.parent = parent;
                this.rowWidth = rowWidth;
            }

            public LayerBuilder row(String row) {
                if (rowWidth == -1) rowWidth = row.length();
                if (row.length() != rowWidth) throw new IllegalArgumentException("Every row of every layer of a multiblock must be the same! Use spaces as air if needed.");
                rows.add(row);
                return this;
            }

            public LayerBuilder rows(String row, int times) {
                if (times <= 0) throw new IllegalArgumentException("'times' must not be 0 or less in LayerBuilder#rows!");
                for (int i = 0; i < times - 1; i++) {
                    this.row(row);
                }
                return this.row(row);
            }

            public MultiblockJsonBuilder end() {
                return parent.push(this);
            }

            public MultiblockJsonBuilder repeat(int times) {
                if (times <= 0) throw new IllegalArgumentException("'times' must not be 0 or less in LayerBuilder#repeat!");
                for (int i = 0; i < times - 1; i++) {
                    parent.push(this);
                }
                return parent.push(this);
            }
        }
    }
}
