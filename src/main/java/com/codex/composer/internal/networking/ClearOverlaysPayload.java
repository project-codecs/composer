package com.codex.composer.internal.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.networking.handler.ClearOverlaysHandler;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///? } else {
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
//? }

public record ClearOverlaysPayload(boolean visible, boolean queue) implements /*? if minecraft: <=1.20.4 { *//*FabricPacket*//*? } else {*/CustomPayload/*?}*/ {
    public static final Identifier oID = Composer.identify("clear_overlays_s2c");

    private static ClearOverlaysPayload read(PacketByteBuf buf) {
        return new ClearOverlaysPayload(buf.readBoolean(), buf.readBoolean());
    }

    //? if minecraft: <=1.20.4 {
    /*public static final Identifier ID = oID;

    @Override
    *///?}
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(visible);
        buf.writeBoolean(queue);
    }

    //? if minecraft: <=1.20.4 {
    /*private static final PacketType<ClearOverlaysPayload> TYPE = PacketType.create(oID, ClearOverlaysPayload::read);

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    *///? } else {
    public static final PacketCodec<PacketByteBuf, ClearOverlaysPayload> CODEC = PacketCodec.of(ClearOverlaysPayload::write, ClearOverlaysPayload::read);
    public static final Id<ClearOverlaysPayload> ID = new Id<>(oID);

    public Id<ClearOverlaysPayload> getId() {
        return ID;
    }
    //? }

    @Environment(EnvType.CLIENT)
    public static void registerHandler() {
        //? if minecraft: >=1.20.6 {
        PayloadTypeRegistry./*? if legacy {*/playS2C/*? } else {*//*clientboundPlay*//*? }*/().register(ID, CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ID, new ClearOverlaysHandler());
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(TYPE, new ClearOverlaysHandler());
        *///?}
    }
}
