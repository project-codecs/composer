package com.codex.composer.mixin.impl.soul_binding;

import com.codex.ambarella.api.v1.util.collections.ComposedStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import com.codex.composer.api.v1.item.settings.component.SoulboundComponent;
import com.codex.composer.api.v1.util.misc.CollectionCollector;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//? if minecraft: >=1.21.4 {
import net.minecraft.server.world.ServerWorld;
//? }

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow @Final /*? if minecraft: <=1.20.4 {*//*private *//*?}*/PlayerInventory inventory;

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;vanishCursedItems()V"), cancellable = true)
    public void composer$keepSoulboundItems(/*? if minecraft: >=1.21.4 {*/ServerWorld world, /*?}*/CallbackInfo ci) {
        List<ItemStack> soulboundStacks = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack == null || stack.isEmpty()) continue;

            if (SoulboundComponent.isSoulbound(stack)) {
                soulboundStacks.add(stack.copy());
                inventory.removeStack(i);
            }
        }

        List<Integer> toRemove = new ArrayList<>();
        ComposedStream<ItemStack> stacks = ComposedStream.of(CollectionCollector.stream(inventory::size, inventory::getStack));

        stacks.filter(stack -> stack != null && !stack.isEmpty(), SoulboundComponent::isSoulbound)
                .forEachIndexed((stack, index) -> {
                    soulboundStacks.add(stack.copy());
                    toRemove.add(index);
                });

        toRemove.sort(Collections.reverseOrder());
        toRemove.forEach(inventory::removeStack);

        if (!soulboundStacks.isEmpty()) {
            ci.cancel();
            inventory.dropAll();
            soulboundStacks.forEach(inventory::insertStack);
        }
    }
}
