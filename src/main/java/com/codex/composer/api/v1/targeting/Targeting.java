package com.codex.composer.api.v1.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import com.codex.composer.internal.cca.ModCardinalComponents;
import com.codex.composer.mixin.accessor.WorldMethodAccessor;

import java.util.UUID;

public class Targeting {
    public static BlockPos getTargetedBlock(PlayerEntity player, TargetingContext options) {
        if (ModCardinalComponents.TARGETED_BLOCK.get(player).getTicks() > options.decayTicks) return null;
        BlockPos pos = ModCardinalComponents.TARGETED_BLOCK.get(player).getPos();

        if (pos == null) return null;
        if (!pos.isWithinDistance(player.getBlockPos(), options.maxDistance)) return null;
        if (pos.getSquaredDistance(player.getBlockPos()) < MathHelper.square(options.minDistance)) return null;

        return pos;
    }

    public static Entity getTargetedEntity(PlayerEntity player, TargetingContext options) {
        if (ModCardinalComponents.TARGETED_ENTITY.get(player).getTicks() > options.decayTicks) return null;

        UUID uuid = ModCardinalComponents.TARGETED_ENTITY.get(player).getUuid();
        if (uuid == null) return null;

        if (!(player./*? if minecraft: <=1.21.6 { */getWorld/*?} else {*//*getEntityWorld*//*?}*/() instanceof WorldMethodAccessor worldMethodAccessor)) return null;
        Entity entity = worldMethodAccessor.composer$getEntityByUuid(uuid);
        if (entity == null) return null;
        if (!options.targetNonLiving && !(entity instanceof LivingEntity)) return null;

        double sqDist = entity.squaredDistanceTo(player);
        if (sqDist > options.maxDistance * options.maxDistance) return null;
        if (sqDist < options.minDistance * options.minDistance) return null;

        if (!entity.isAlive() && !options.targetDead) return null;
        if (!options.targetTamed && entity instanceof Tameable tameable && tameable.getOwner() != null) return null;

        return entity;
    }
}
