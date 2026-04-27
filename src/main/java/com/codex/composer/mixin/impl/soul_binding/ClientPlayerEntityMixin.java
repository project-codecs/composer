package com.codex.composer.mixin.impl.soul_binding;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.codex.composer.api.v1.item.settings.component.SoulboundComponent.*;

//? if minecraft: <=1.21.5
import net.minecraft.util.math.BlockPos;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    //? if minecraft: <=1.21.5 {
    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    //? } else {
    /*public ClientPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }
    *///? }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    public void composer$preventDroppingSoulbound(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        PlayerInventory inv = getInventory();
        //? if minecraft: <=1.21.4
        ItemStack stack = inv.getStack(inv.selectedSlot);
        //? if minecraft: >=1.21.5
        //ItemStack stack = inv.getStack(inv.getSelectedSlot());

        if (shouldNotDrop(stack)) cir.cancel();
    }
}
