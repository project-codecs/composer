package com.codex.composer.api.v1.multiblock;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record MultiblockMatch(boolean complete, Map<Identifier, List<BlockPos>> extensions, @Nullable BlockPos firstMismatch) {
    static MultiblockMatch success(Map<Identifier, List<BlockPos>> found) {
        Map<Identifier, List<BlockPos>> copy = new LinkedHashMap<>();
        found.forEach((id, list) -> copy.put(id, List.copyOf(list)));
        return new MultiblockMatch(true, Collections.unmodifiableMap(copy), null);
    }

    static MultiblockMatch mismatch(BlockPos pos) {
        return new MultiblockMatch(false, Map.of(), pos);
    }

    static MultiblockMatch countMismatch() {
        return new MultiblockMatch(false, Map.of(), null);
    }
}