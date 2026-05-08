package com.codex.composer.internal.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.networking.handler.ScrollActionHandler;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///? } else {
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
        //? }

public record ScrollActionPayload(Identifier channel, double scrollAmount)
        implements /*? if minecraft: <=1.20.4 { *//*FabricPacket*//*? } else {*/CustomPayload/*?}*/ {
    public static final Identifier oID = Composer.identify("scroll_action_c2s");

    private ScrollActionPayload(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readDouble());
    }

    //? if minecraft: <= 1.20.4 {
    /*public static final Identifier ID = oID;

    @Override
    *///?}
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(channel);
        buf.writeDouble(scrollAmount);
    }

    //? if minecraft: <=1.20.4 {
    /*private static final PacketType<ScrollActionPayload> TYPE = PacketType.create(ID, ScrollActionPayload::new);
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    *///? } else {
    public static final PacketCodec<PacketByteBuf, ScrollActionPayload> CODEC = PacketCodec.of(ScrollActionPayload::write, ScrollActionPayload::new);
    public static final CustomPayload.Id<ScrollActionPayload> ID = new Id<>(oID);

    public CustomPayload.Id<ScrollActionPayload> getId() {
        return ID;
    }
    //? }

    public static void registerHandler() {
        //? if minecraft: >=1.20.6 {
        PayloadTypeRegistry./*? if legacy {*/playC2S/*? } else {*//*serverboundPlay*//*? }*/().register(ID, CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ID, new ScrollActionHandler());
        //?} else {
        /*ServerPlayNetworking.registerGlobalReceiver(TYPE, new ScrollActionHandler());
        *///?}
    }
}
