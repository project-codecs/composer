package com.codex.composer.internal.multiblock;

import com.codex.composer.api.v1.block.MultiblockControllerBlock;
import com.codex.composer.api.v1.multiblock.Multiblock;
import com.codex.composer.api.v1.multiblock.MultiblockRegistry;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class MultiblockUpdateHandler {
    public static void runUpdates(World world, MultiblocksComponent component, @Nullable BlockPos checkAgainst) {
        List<PendingUpdate> pending = new ArrayList<>();

        for (Pair<BlockPos, Pair<Identifier, Boolean>> instance : component.multiblocks) {
            BlockPos controllerPos = instance.getA();
            Identifier id = instance.getB().getA();
            boolean wasComplete = instance.getB().getB();

            if (checkAgainst != null && !controllerPos.equals(checkAgainst) && !controllerPos.isWithinDistance(checkAgainst, 16)) continue;

            Multiblock multiblock = MultiblockRegistry.getInstance().get(id);
            if (multiblock == null) continue;

            BlockEntity be = world.getBlockEntity(controllerPos);
            if (!(be instanceof MultiblockControllerBlock controller)) continue;

            boolean isComplete = controller.isComplete(world, controllerPos);
            if (isComplete != wasComplete) pending.add(new PendingUpdate(controllerPos, id, isComplete));
        }

        for (PendingUpdate update : pending) {
            BlockEntity be = world.getBlockEntity(update.controllerPos());
            if (!(be instanceof MultiblockControllerBlock controller)) continue;

            component.removeIfPresent(update.controllerPos());
            component.computeIfAbsent(update.controllerPos(), update.id());
            component.markAs(update.controllerPos(), update.isComplete());

            if (update.isComplete()) {
                controller.onMultiblockFormed();
            } else {
                controller.onMultiblockBroken();
            }
        }
    }

    record PendingUpdate(BlockPos controllerPos, Identifier id, boolean isComplete) {}

}
