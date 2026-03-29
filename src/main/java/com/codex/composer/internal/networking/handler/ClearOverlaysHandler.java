package com.codex.composer.internal.networking.handler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.codex.composer.api.v1.overlay.OverlayManager;
import com.codex.composer.internal.networking.ClearOverlaysPayload;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
*///? }

//? if minecraft: <=1.20.4 {
/*public class ClearOverlaysHandler implements ClientPlayNetworking.PlayPacketHandler<ClearOverlaysPayload> {
*///?} else {
public class ClearOverlaysHandler implements ClientPlayNetworking.PlayPayloadHandler<ClearOverlaysPayload> {
//?}
    @Override
    //? if minecraft: <=1.20.4 {
    /*public void receive(ClearOverlaysPayload payload, ClientPlayerEntity player, PacketSender sender) {
    *///? } else {
    public void receive(ClearOverlaysPayload payload, ClientPlayNetworking.Context context) {
    //? }
        if (payload.queue()) OverlayManager.getInstance().clearQueue();
        if (payload.visible()) OverlayManager.getInstance().clearVisible();
    }
}
