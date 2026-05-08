package com.codex.composer.internal.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.networking.handler.TargetBlockHandler;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///? } else {
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
//? }

public record TargetBlockPayload(BlockPos pos)
        implements /*? if minecraft: <=1.20.4 { *//*FabricPacket*//*? } else {*/CustomPayload/*?}*/ {
    public static final Identifier oID = Composer.identify("target_block_c2s");

    private TargetBlockPayload(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }

    //? if minecraft: <= 1.20.4 {
    /*public static final Identifier ID = oID;

    @Override
    *///?}
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    //? if minecraft: <=1.20.4 {
    /*private static final PacketType<TargetBlockPayload> TYPE = PacketType.create(ID, TargetBlockPayload::new);
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    *///? } else {
    public static final PacketCodec<PacketByteBuf, TargetBlockPayload> CODEC = PacketCodec.of(TargetBlockPayload::write, TargetBlockPayload::new);
    public static final CustomPayload.Id<TargetBlockPayload> ID = new Id<>(oID);

    public CustomPayload.Id<TargetBlockPayload> getId() {
        return ID;
    }
    //? }

    public static void registerHandler() {
        //? if minecraft: >=1.20.6 {
        PayloadTypeRegistry./*? if legacy {*/playC2S/*? } else {*//*serverboundPlay*//*? }*/().register(ID, CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ID, new TargetBlockHandler());
        //?} else {
        /*ServerPlayNetworking.registerGlobalReceiver(TYPE, new TargetBlockHandler());
        *///?}
    }
}
