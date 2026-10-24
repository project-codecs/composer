package com.codex.composer.internal.multiblock;

import com.codex.ambarella.api.v1.util.misc.PredicateVoid;
import com.codex.composer.api.v1.multiblock.Multiblock;
import com.codex.composer.api.v1.multiblock.MultiblockExtension;
import com.codex.composer.api.v1.util.misc.CubeMatrix;
import com.codex.composer.internal.data.loader.json.MultiblockJsonLoader.ParsedMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3i;

import java.util.*;
import java.util.function.Predicate;

public class JsonBackedMultiblock implements Multiblock {
    private final CubeMatrix<Predicate<BlockState>> blocks;
    private final CubeMatrix<Set<Identifier>> replaceable;
    private final Map<Identifier, MultiblockExtension> extensions;
    private final Vec3i controllerPos;
    private final Vec3i shape;

    private JsonBackedMultiblock(CubeMatrix<Predicate<BlockState>> blocks, CubeMatrix<Set<Identifier>> replaceable, Map<Identifier, MultiblockExtension> extensions, Vec3i controllerPos) {
        this.blocks = blocks;
        this.replaceable = replaceable;
        this.extensions = Collections.unmodifiableMap(extensions);
        this.controllerPos = controllerPos;

        var b = blocks.getBounds();
        this.shape = new Vec3i(b.maxX() - b.minX() + 1, b.maxY() - b.minY() + 1, b.maxZ() - b.minZ() + 1);
    }

    public static JsonBackedMultiblock ofParsed(ParsedMultiblock parsed) {
        Map<Character, Predicate<BlockState>> pattern = new HashMap<>();
        Map<Character, Set<Identifier>> replaceableBySymbol = new HashMap<>();

        parsed.pattern().forEach((symbol, entry) -> {
            pattern.put(symbol, blockPredicate(entry.blocks()));

            for (Identifier ext : entry.extensions()) {
                if (!parsed.extensions().containsKey(ext)) {
                    throw new IllegalStateException("Key '" + symbol + "' references unknown extension " + ext);
                }
            }
            if (!entry.extensions().isEmpty()) replaceableBySymbol.put(symbol, entry.extensions());
        });

        Map<Identifier, MultiblockExtension> extensions = new LinkedHashMap<>();
        parsed.extensions().forEach((id, ext) -> extensions.put(id, new MultiblockExtension(id, blockPredicate(ext.blocks()), ext.min(), ext.max())));

        CubeMatrix<Predicate<BlockState>> base = parsed.matrix().flatMap(symbol -> predicateFor(symbol, pattern));
        CubeMatrix<Set<Identifier>> replaceable = parsed.matrix().flatMap(symbol -> extensionsFor(symbol, replaceableBySymbol));

        return new JsonBackedMultiblock(base, replaceable, extensions, parsed.controllerPos());
    }

    private static Predicate<BlockState> predicateFor(char symbol, Map<Character, Predicate<BlockState>> pattern) {
        if (symbol == '*') return PredicateVoid::always;
        if (symbol == ' ' && !pattern.containsKey(' ')) return BlockState::isAir;
        Predicate<BlockState> predicate = pattern.get(symbol);
        if (predicate == null) throw new IllegalStateException("Unknown key in multiblock: '" + symbol + "'");
        return predicate;
    }

    private static Set<Identifier> extensionsFor(char symbol, Map<Character, Set<Identifier>> replaceable) {
        return replaceable.getOrDefault(symbol, Set.of());
    }

    private static Predicate<BlockState> blockPredicate(List<String> entries) {
        if (entries.isEmpty()) throw new IllegalStateException("Empty block list");
        List<Predicate<BlockState>> parts = new ArrayList<>();

        for (String entry : entries) {
            if (entry.startsWith("#")) {
                Identifier id = Identifier.tryParse(entry.substring(1));
                if (id == null) throw new IllegalStateException("Invalid block tag: " + entry);
                TagKey<Block> tag = TagKey.of(RegistryKeys.BLOCK, id);
                parts.add(state -> state.isIn(tag));
            } else {
                Identifier id = Identifier.tryParse(entry);
                if (id == null || !Registries.BLOCK.containsId(id)) throw new IllegalStateException("Unknown block: " + entry);
                Block block = Registries.BLOCK.get(id);
                parts.add(state -> state.getBlock() == block);
            }
        }

        return state -> {
            for (Predicate<BlockState> part : parts) if (part.test(state)) return true;
            return false;
        };
    }

    @Override
    public Predicate<BlockState> validWhen(BlockPos local) {
        Predicate<BlockState> predicate = blocks.get(new Vector3i(local.getX(), local.getY(), local.getZ()));
        return predicate != null ? predicate : PredicateVoid::never;
    }

    @Override
    public Set<Identifier> extensionsAt(BlockPos local) {
        Set<Identifier> set = replaceable.get(new Vector3i(local.getX(), local.getY(), local.getZ()));
        return set != null ? set : Set.of();
    }

    @Override
    public Map<Identifier, MultiblockExtension> extensions() {
        return extensions;
    }

    @Override
    public Vec3i shape() {
        return shape;
    }

    @Override
    public Vec3i controllerPos() {
        return controllerPos;
    }
}