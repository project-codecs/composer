package com.codex.composer.api.v1.block;

import com.codex.composer.api.v1.multiblock.Multiblock;
import com.codex.composer.api.v1.multiblock.MultiblockRegistry;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.cca.ModCardinalComponents;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public interface MultiblockControllerBlock {
    Identifier multiblock();
    default void onMultiblockFormed() { }
    default void onMultiblockBroken() { }

    default Direction getOrientation(BlockState state) {
        return switch (state.getOrEmpty(Properties.HORIZONTAL_FACING).orElse(Direction.NORTH)) {
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            case SOUTH -> Direction.NORTH;
            default -> Direction.SOUTH;
        };
    }

    default void onPlaced(World world, BlockPos pos) {
        MultiblocksComponent component = ModCardinalComponents.MULTIBLOCKS.get(world);
        component.computeIfAbsent(pos, multiblock());
    }

    default void onBroken(World world, BlockPos pos) {
        MultiblocksComponent component = ModCardinalComponents.MULTIBLOCKS.get(world);
        component.removeIfPresent(pos);
    }

    default boolean isComplete(BlockView world, BlockPos pos) {
        Multiblock multiblock = MultiblockRegistry.getInstance().get(multiblock());

        if (multiblock == null) {
            Composer.CACHED_LOGGER.warnOnce("Invalid multiblock!");
            return false;
        }

        BlockPos offset = new BlockPos(multiblock.controllerPos());
        List<BlockPos> shape = multiblock.stream().map(BlockPos::toImmutable).toList();
        Vec3i mbShape = multiblock.shape();

        int maxX = mbShape.getX() - 1;
        int maxZ = mbShape.getZ() - 1;

        int rotation = toRotation(getOrientation(world.getBlockState(pos)));

        BlockPos rotatedOffset = rotateAroundY(offset, maxX, maxZ, rotation);

        for (BlockPos mbPos : shape) {
            BlockPos rotatedPos = rotateAroundY(mbPos, maxX, maxZ, rotation);
            BlockPos worldPos = pos.add(rotatedPos.subtract(rotatedOffset));
            BlockState worldState = world.getBlockState(worldPos);

            if (!multiblock.isValid(mbPos, worldState)) {
                return false;
            }
        }

        return true;
    }

    private static int toRotation(Direction o) {
        return switch (o) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
    }

    private static BlockPos rotateAroundY(BlockPos pos, int maxX, int maxZ, int rotation) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return switch (rotation & 3) {
            case 1 -> new BlockPos(-z, y, maxX - x);
            case 2 -> new BlockPos(maxX - x, y, maxZ + z);
            case 3 -> new BlockPos(maxZ + z, y, x);
            default -> new BlockPos(x, y, -z);
        };
    }
}
