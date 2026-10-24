package com.codex.composer.api.v1.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Multiblock {
    Vec3i controllerPos();
    Vec3i shape();

    Predicate<BlockState> validWhen(BlockPos local);
    Set<Identifier> extensionsAt(BlockPos local);
    Map<Identifier, MultiblockExtension> extensions();

    @Nullable
    default Identifier matchExtension(BlockPos local, BlockState state) {
        Map<Identifier, MultiblockExtension> defs = extensions();
        for (Identifier id : extensionsAt(local)) {
            MultiblockExtension ext = defs.get(id);
            if (ext != null && ext.predicate().test(state)) return id;
        }
        return null;
    }

    default boolean isValid(BlockPos local, BlockState state) {
        return matchExtension(local, state) != null || validWhen(local).test(state);
    }

    default Stream<BlockPos> stream() {
        Vec3i shape = shape();
        return BlockPos.stream(0, 0, 0, shape.getX() - 1, shape.getY() - 1, shape.getZ() - 1);
    }

    default BlockPos toWorld(BlockPos controller, BlockPos local, Direction orientation) {
        int dx = local.getX() - controllerPos().getX();
        int dy = local.getY() - controllerPos().getY();
        int dz = local.getZ() - controllerPos().getZ();
        int wx, wz;
        switch (orientation) {
            case NORTH -> { wx = dx; wz = -dz; }
            case WEST -> { wx = -dz; wz = -dx; }
            case EAST -> { wx = dz; wz = dx; }
            default -> { wx = -dx; wz = dz; }
        }
        return controller.add(wx, dy, wz);
    }

    default BlockBox bounds(BlockPos controller, Direction orientation) {
        Vec3i shape = shape();
        BlockPos a = toWorld(controller, BlockPos.ORIGIN, orientation);
        BlockPos b = toWorld(controller, new BlockPos(shape.getX() - 1, shape.getY() - 1, shape.getZ() - 1), orientation);
        return BlockBox.create(a, b);
    }

    default MultiblockMatch match(BlockView world, BlockPos controller) {
        return match(world, controller, Direction.SOUTH);
    }

    default MultiblockMatch match(BlockView world, BlockPos controller, Direction orientation) {
        Vec3i shape = shape();
        Map<Identifier, List<BlockPos>> found = new LinkedHashMap<>();

        for (int x = 0; x < shape.getX(); x++) {
            for (int y = 0; y < shape.getY(); y++) {
                for (int z = 0; z < shape.getZ(); z++) {
                    BlockPos local = new BlockPos(x, y, z);
                    BlockPos current = toWorld(controller, local, orientation);
                    BlockState state = world.getBlockState(current);

                    Identifier ext = matchExtension(local, state);
                    if (ext != null) found.computeIfAbsent(ext, k -> new ArrayList<>()).add(current);
                    else if (!validWhen(local).test(state)) return MultiblockMatch.mismatch(current);
                }
            }
        }

        for (MultiblockExtension ext : extensions().values()) {
            int count = found.getOrDefault(ext.id(), List.of()).size();
            if (count < ext.min() || count > ext.max()) return MultiblockMatch.countMismatch();
        }

        return MultiblockMatch.success(found);
    }
}