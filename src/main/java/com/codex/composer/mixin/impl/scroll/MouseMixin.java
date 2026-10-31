package com.codex.composer.mixin.impl.scroll;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.world.ClientWorld;
import com.codex.composer.api.v1.events.ClientScrollEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//? minecraft: >=1.21.4 {
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
//? }

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    final List<Event<ClientScrollEvents.ClientScrollAction>> priorities = List.of(
            ClientScrollEvents.HIGH_PRIORITY,
            ClientScrollEvents.MEDIUM_PRIORITY,
            ClientScrollEvents.LOW_PRIORITY
    );

    //? if >=1.21.4
    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;"), cancellable = true)
    public void composer$triggerActions(long window, double horizontal, double vertical, CallbackInfo ci) {
        ClientWorld world = client.world;

        for (Event<ClientScrollEvents.ClientScrollAction> event : priorities) {
            if (event.invoker().onScroll(client, world, client.player, vertical)) {
                ci.cancel();
                break;
            }
        }
    }
}
