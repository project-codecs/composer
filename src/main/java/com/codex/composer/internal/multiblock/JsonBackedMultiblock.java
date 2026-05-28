package com.codex.composer.internal.multiblock;

import com.codex.ambarella.api.v1.util.misc.PredicateVoid;
import com.codex.composer.api.v1.multiblock.Multiblock;
import com.codex.composer.internal.data.loader.json.MultiblockJsonLoader;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


public class JsonBackedMultiblock implements Multiblock {
    private final List<List<List<Predicate<BlockState>>>> blocks;
    private final Vec3i controllerPos;

    private JsonBackedMultiblock(List<List<List<Predicate<BlockState>>>> blocks, Vec3i controllerPos) {
        this.blocks = blocks;
        this.controllerPos = controllerPos;
    }

    public static JsonBackedMultiblock ofParsed(MultiblockJsonLoader.ParsedMultiblock parsed) {
        Map<Character, Predicate<BlockState>> pattern = new HashMap<>();

        parsed.getKeys().forEach(key -> {
            JsonPrimitive primitive = key.value();

            if (primitive.isString()) {
                String value = primitive.getAsString();

                if (value.startsWith("#")) {
                    value = value.substring(1);
                    Identifier id = Identifier.tryParse(value);
                    if (id == null) throw new IllegalStateException("Invalid block identifier: " + value);
                    TagKey<Block> tag = TagKey.of(RegistryKeys.BLOCK, id);
                    pattern.put(key.symbol(), state -> state.isIn(tag));
                } else {
                    Identifier id = Identifier.tryParse(value);
                    if (id == null) throw new IllegalStateException("Invalid block identifier: " + value);
                    pattern.put(key.symbol(), state -> state.getBlock() == Registries.BLOCK.get(id));
                }
            }
        });

        List<List<List<Predicate<BlockState>>>> blocks = new ArrayList<>();

        parsed.getLayers().forEach(layer -> {
            List<List<Predicate<BlockState>>> lyr = new ArrayList<>();

            layer.shape().forEach(line -> {
                List<Predicate<BlockState>> row = new ArrayList<>();

                line.forEach(key -> {
                    if (key.symbol() == '*') row.add(PredicateVoid::always);
                    else if (key.symbol() == ' ' && !pattern.containsKey(' ')) row.add(BlockState::isAir);
                    else {
                        Predicate<BlockState> predicate = pattern.get(key.symbol());

                        if (predicate == null) {
                            throw new IllegalStateException("Unknown key in multiblock: '" + key.symbol() + "'");
                        }

                        row.add(predicate);
                    }
                });

                lyr.add(row);
            });

            blocks.add(lyr);
        });

        return new JsonBackedMultiblock(blocks, parsed.getControllerPos());
    }

    public boolean isValid(BlockPos pos, BlockState state) {
        return this.validWhen(pos).test(state);
    }

    @SuppressWarnings("SequencedCollectionMethodCanBeUsed")
    public Predicate<BlockState> validWhen(BlockPos pos) {
        int layerCount = this.blocks.size();
        int rowCount = this.blocks.get(0).size();
        int colCount = this.blocks.get(0).get(0).size();

        int y = pos.getY();
        int z = pos.getZ();
        int x = pos.getX();

        if (y < 0 || y >= layerCount) return PredicateVoid::never;
        if (z < 0 || z >= rowCount) return PredicateVoid::never;
        if (x < 0 || x >= colCount) return PredicateVoid::never;

        int rowIndex = (rowCount - 1) - z;

        return this.blocks.get(y).get(rowIndex).get(x);
    }

    @SuppressWarnings("SequencedCollectionMethodCanBeUsed")
    public Vec3i shape() {
        int height = this.blocks.size();                  // Y: number of layers
        int depth = this.blocks.get(0).size();           // Z: number of rows per layer
        int width = this.blocks.get(0).get(0).size();   // X: number of columns per row
        return new Vec3i(width, height, depth);
    }

    @Override
    public Vec3i controllerPos() {
        return controllerPos;
    }
}
