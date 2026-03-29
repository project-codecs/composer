package com.codex.composer.internal.networking;

import com.codex.composer.internal.networking.handler.ShowCreditsHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import com.codex.composer.internal.Composer;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///? } else {
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
//? }

public record ShowCreditsPayload(boolean credits, boolean poem) implements /*? if minecraft: <=1.20.4 { *//*FabricPacket*//*? } else {*/CustomPayload/*?}*/ {
    public static final Identifier oID = Composer.identify("show_credits_s2c");

    private static ShowCreditsPayload read(PacketByteBuf buf) {
        return new ShowCreditsPayload(buf.readBoolean(), buf.readBoolean());
    }

    //? if minecraft: <=1.20.4 {
    /*public static final Identifier ID = oID;

    @Override
    *///?}
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(credits);
        buf.writeBoolean(poem);
    }

    //? if minecraft: <=1.20.4 {
    /*private static final PacketType<ShowCreditsPayload> TYPE = PacketType.create(oID, ShowCreditsPayload::read);

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    *///? } else {
    public static final PacketCodec<PacketByteBuf, ShowCreditsPayload> CODEC = PacketCodec.of(ShowCreditsPayload::write, ShowCreditsPayload::read);
    public static final Id<ShowCreditsPayload> ID = new Id<>(oID);

    public Id<ShowCreditsPayload> getId() {
        return ID;
    }
    //? }

    @Environment(EnvType.CLIENT)
    public static void registerHandler() {
        //? if minecraft: >=1.20.6 {
        PayloadTypeRegistry.playS2C().register(ID, CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ID, new ShowCreditsHandler());
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(TYPE, new ShowCreditsHandler());
        *///?}
    }
}
