package com.codex.composer.internal.networking.handler;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.codex.composer.api.v1.events.ServerScrollEvents;
import com.codex.composer.api.v1.events.ServerScrollEvents.ServerScrollAction;
import com.codex.composer.internal.networking.ScrollActionPayload;

import java.util.List;

//? if <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.network.ServerPlayerEntity;
 *///? }

//? if <=1.20.4 {
/*public class ScrollActionHandler implements ServerPlayNetworking.PlayPacketHandler<ScrollActionPayload> {
*///?} else {
 public class ScrollActionHandler implements ServerPlayNetworking.PlayPayloadHandler<ScrollActionPayload> {
//?}
    private static final List<Event<ServerScrollAction>> priorities = List.of(
            ServerScrollEvents.HIGH_PRIORITY,
            ServerScrollEvents.MEDIUM_PRIORITY,
            ServerScrollEvents.LOW_PRIORITY
    );

    //? if <=1.20.4 {
    /*@Override
    public void receive(ScrollActionPayload payload, ServerPlayerEntity player, PacketSender sender) {
        for (Event<ServerScrollAction> event : priorities)
            if (event.invoker().onScroll(payload.channel(), player, sender, payload.scrollAmount())) return;
    }
    *///? } else {
    @Override
    public void receive(ScrollActionPayload payload, ServerPlayNetworking.Context context) {
        for (Event<ServerScrollAction> event : priorities)
            if (event.invoker().onScroll(payload.channel(), context.player(), context.responseSender(), payload.scrollAmount())) return;
    }
    //?}
}
