package com.codex.composer.internal.networking.handler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.codex.composer.api.v1.overlay.OverlayManager;
import com.codex.composer.internal.networking.ShowOverlayPayload;

//? if <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
*///? }

//? if <=1.20.4 {
/*public class ShowOverlayHandler implements ClientPlayNetworking.PlayPacketHandler<ShowOverlayPayload<?>> {
*///?} else {
 public class ShowOverlayHandler implements ClientPlayNetworking.PlayPayloadHandler<ShowOverlayPayload<?>> {
//?}
    @Override
    //? if <=1.20.4 {
    /*public void receive(ShowOverlayPayload<?> payload, ClientPlayerEntity player, PacketSender sender) {
    *///? } else {
    public void receive(ShowOverlayPayload<?> payload, ClientPlayNetworking.Context context) {
    //? }
        OverlayManager.getInstance().queue(payload.overlay());
    }
}
