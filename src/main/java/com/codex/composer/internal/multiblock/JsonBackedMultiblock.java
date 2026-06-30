package com.codex.composer.internal.multiblock;

import com.codex.ambarella.api.v1.util.misc.PredicateVoid;
import com.codex.composer.api.v1.multiblock.Multiblock;
import com.codex.composer.api.v1.util.misc.CubeMatrix;
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
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;


public class JsonBackedMultiblock implements Multiblock {
    private final CubeMatrix<Predicate<BlockState>> blocks;
    private final Vec3i controllerPos;

    private JsonBackedMultiblock(CubeMatrix<Predicate<BlockState>> blocks, Vec3i controllerPos) {
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
                    if (id == null) throw new IllegalStateException("Invalid block tag: " + value);
                    TagKey<Block> tag = TagKey.of(RegistryKeys.BLOCK, id);
                    pattern.put(key.symbol(), state -> state.isIn(tag));
                } else {
                    Identifier id = Identifier.tryParse(value);
                    if (id == null) throw new IllegalStateException("Invalid block identifier: " + value);
                    pattern.put(key.symbol(), state -> state.getBlock() == Registries.BLOCK.get(id));
                }
            }
        });

        CubeMatrix<Predicate<BlockState>> blocks = parsed.getMatrix().flatMap(key -> {
            if (key.symbol() == '*') return PredicateVoid::always;
            else if (key.symbol() == ' ' && !pattern.containsKey(' ')) return BlockState::isAir;
            else {
                if (!pattern.containsKey(key.symbol())) throw new IllegalStateException("Unknown key in multiblock: '" + key.symbol() + "'");
                return pattern.get(key.symbol());
            }
        });

        return new JsonBackedMultiblock(blocks, parsed.getControllerPos());
    }

    public boolean isValid(BlockPos pos, BlockState state) {
        return this.validWhen(pos).test(state);
    }

    public Predicate<BlockState> validWhen(BlockPos pos) {
        Vector3i p = new Vector3i(pos.getX(), pos.getY(), pos.getZ());
        Predicate<BlockState> predicate = blocks.get(p);
        return predicate != null ? predicate : PredicateVoid::never;
    }

    public Vec3i shape() {
        var b = blocks.getBounds();

        return new Vec3i(
                b.maxX() - b.minX() + 1,
                b.maxY() - b.minY() + 1,
                b.maxZ() - b.minZ() + 1
        );
    }

    @Override
    public Vec3i controllerPos() {
        return controllerPos;
    }
}
