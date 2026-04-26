package com.codex.composer.mixin.impl.targeting;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import com.codex.composer.api.v1.targeting.TargetingContext;
import com.codex.composer.internal.networking.TargetBlockPayload;
import com.codex.composer.internal.networking.TargetEntityPayload;
import com.codex.composer.internal.targeting.BlockTargeting;
import com.codex.composer.internal.targeting.Targeting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Nullable
    public abstract Entity getCameraEntity();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;tick(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/hit/HitResult;)V"))
    public void composer$getLastTarget(CallbackInfo ci) {
        Entity camera = this.getCameraEntity();
        if (this.player == null || camera == null || this.world == null) return;
        double distanceCap = 128f * 128f;
        Vec3d cameraPos = camera.getCameraPosVec(1.0F);
        Vec3d cameraRot = camera.getRotationVec(1.0F);
        Vec3d cameraTarget = cameraPos.add(cameraRot.multiply(distanceCap));
        Box box = camera.getBoundingBox().stretch(cameraTarget).expand(1.0, 1.0, 1.0);
        Predicate<Entity> predicate = entity -> Targeting.isValidTarget(entity, TargetingContext.getIgnoring());
        EntityHitResult hitResult = Targeting.raycast(this.player, box, predicate, 1.5f);
        BlockHitResult blockHitResult = BlockTargeting.raycastBlocks(player, box);
        if (hitResult != null) {
            Entity target = hitResult.getEntity();
            ClientPlayNetworking.send(new TargetEntityPayload(target.getUuid()));
        }
        if (blockHitResult != null) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            ClientPlayNetworking.send(new TargetBlockPayload(blockPos));
        }
    }
}
