package com.codex.composer.internal.networking.handler;

import com.codex.composer.api.v1.util.misc.PredicateVoid;
import com.codex.composer.internal.client.screen.ModifiedCreditsScreen;
import com.codex.composer.internal.networking.ShowCreditsPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
*///? }

//? if minecraft: <=1.20.4 {
/*public class ShowCreditsHandler implements ClientPlayNetworking.PlayPacketHandler<ShowCreditsPayload> {
*///?} else {
public class ShowCreditsHandler implements ClientPlayNetworking.PlayPayloadHandler<ShowCreditsPayload> {
//?}
    @Override
    //? if minecraft: <=1.20.4 {
    /*public void receive(ShowCreditsPayload payload, ClientPlayerEntity player, PacketSender sender) {
    *///? } else {
    public void receive(ShowCreditsPayload payload, ClientPlayNetworking.Context context) {
    //? }
        MinecraftClient.getInstance().setScreen(new ModifiedCreditsScreen(
                payload.credits(),
                payload.poem(),
                () -> MinecraftClient.getInstance().setScreen(null)
        ));
    }
}
