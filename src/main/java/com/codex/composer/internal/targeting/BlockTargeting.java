package com.codex.composer.internal.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockTargeting {
    @Nullable
    public static BlockHitResult raycastBlocks(Entity player, Box box) {
        World world = player./*? if minecraft: <=1.21.6 { */getWorld/*?} else {*//*getEntityWorld*//*?}*/();
        Vec3d start = player.getEyePos();
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(box./*? if minecraft: <=1.20.1 { *//*getXLength*//*?} else {*/getLengthX/*?}*/()));

        BlockHitResult blockHitResult = world.raycast(new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            return blockHitResult;
        }
        return null;
    }
}
