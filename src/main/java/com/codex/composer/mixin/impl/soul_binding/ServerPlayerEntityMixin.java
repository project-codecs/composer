package com.codex.composer.mixin.impl.soul_binding;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import com.codex.composer.api.v1.item.settings.component.SoulboundComponent;
import com.codex.composer.api.v1.util.misc.CollectionCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if minecraft: <=1.21.5
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    //? if minecraft: <=1.21.5 {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    //? } else {
    /*public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }
    *///? }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void composer$restoreSoulboundItems(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (!alive) {
            PlayerInventory inv = oldPlayer.getInventory();
            CollectionCollector.stream(inv::size, inv::getStack)
                    .filter(SoulboundComponent::isSoulbound)
                    .forEach(getInventory()::insertStack);
        }
    }
}
