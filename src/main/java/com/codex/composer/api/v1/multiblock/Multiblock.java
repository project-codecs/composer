package com.codex.composer.api.v1.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Multiblock {
    /**
     *
     * @param pos Where in the multiblock to check
     * @param state The {@link BlockState} to check
     * @return True if the given {@link BlockState} is valid at the given {@link BlockPos}
     */
    boolean isValid(BlockPos pos, BlockState state);

    /**
     * Determines where the controller of the multiblock is
     * @return Value within the {@link Multiblock#shape()} saying where the controller is.
     */
    Vec3i controllerPos();

    /**
     * Returns a predicate which describes what {@link BlockState} is valid at any given {@link BlockPos}.
     * @param pos Position to get the predicate for
     * @return {@link BlockState} predicate
     */
    Predicate<BlockState> validWhen(BlockPos pos);

    /**
     * Method describing the "shape" of the multiblock's "surrounding box"
     * @return Shape in width, height, depth (X, Y, Z)
     */
    Vec3i shape();

    /**
     * Creates a stream iterating over the multiblock's shape
     * @return Stream containing every position in the multiblock
     */
    default Stream<BlockPos> stream() {
        Vec3i shape = shape();
        return BlockPos.stream(0, 0, 0, shape.getX() - 1, shape.getY() - 1, shape.getZ() - 1);
    }

    /**
     * Returns a list of positions that a given BlockState is valid for
     * @param state The {@link BlockState} to check
     * @return List of block positions
     */
    default List<BlockPos> whereAre(BlockState state) {
        List<BlockPos> found = new ArrayList<>();

        stream().forEach(pos -> {
            if (isValid(pos, state)) found.add(pos);
        });

        return found;
    }
}
