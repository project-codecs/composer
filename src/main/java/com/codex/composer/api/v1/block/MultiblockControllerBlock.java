package com.codex.composer.api.v1.block;

import com.codex.composer.api.v1.multiblock.ingame.MultiblockController;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;
import java.util.Optional;

public abstract class MultiblockControllerBlock extends BlockWithEntity {
    protected MultiblockControllerBlock(Settings settings) {
        super(settings);
    }

    public static Optional<MultiblockController> getController(BlockView world, BlockPos pos) {
        return MultiblockController.at(world, pos);
    }

    public static boolean isFormed(BlockView world, BlockPos pos) {
        return getController(world, pos).map(MultiblockController::isFormed).orElse(false);
    }

    public static List<BlockPos> getExtensions(BlockView world, BlockPos controllerPos, Identifier extensionId) {
        return getController(world, controllerPos).map(c -> c.getExtensions(extensionId)).orElse(List.of());
    }
}