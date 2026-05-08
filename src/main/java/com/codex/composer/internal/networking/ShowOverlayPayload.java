package com.codex.composer.internal.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.overlay.impl.Overlay;
import com.codex.composer.api.v1.util.misc.PacketSerializer;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.networking.handler.ShowOverlayHandler;


import static com.codex.composer.api.v1.registry.ComposerRegistries.OVERLAY_SERIALIZERS;
import static com.codex.composer.api.v1.registry.ComposerRegistryKeys.OVERLAY_SERIALIZERS_KEY;

//? if minecraft: <=1.20.4 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///? } else {
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
//? }

public record ShowOverlayPayload<T extends Overlay>(T overlay)
        implements /*? if minecraft: <=1.20.4 { *//*FabricPacket*//*? } else {*/CustomPayload/*?}*/ {
    public static final Identifier oID = Composer.identify("show_overlay_s2c");

    private static ShowOverlayPayload<?> read(PacketByteBuf buf) {
        Identifier id = buf.readIdentifier();
        PacketSerializer<? extends Overlay> serializer = OVERLAY_SERIALIZERS.getOrThrow(RegistryKey.of(OVERLAY_SERIALIZERS_KEY, id))/*? if minecraft: >=1.21.3 { */.value()/*? }*/;
        return new ShowOverlayPayload<>(serializer.read(buf));
    }

    //? if minecraft: <= 1.20.4 {
    /*public static final Identifier ID = oID;

    @Override
    *///?}
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(overlay.getId());
        @SuppressWarnings("unchecked")
        PacketSerializer<T> serializer = (PacketSerializer<T>) OVERLAY_SERIALIZERS.getOrThrow(RegistryKey.of(OVERLAY_SERIALIZERS_KEY, overlay.getId()))/*? if minecraft: >=1.21.3 { */.value()/*? }*/;
        serializer.write(overlay, buf);
    }

    //? if minecraft: <=1.20.4 {
    /*private static final PacketType<ShowOverlayPayload<?>> TYPE = PacketType.create(ID, ShowOverlayPayload::read);
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    *///? } else {
    public static final PacketCodec<PacketByteBuf, ShowOverlayPayload<?>> CODEC = PacketCodec.of(ShowOverlayPayload::write, ShowOverlayPayload::read);
    public static final CustomPayload.Id<ShowOverlayPayload<?>> ID = new Id<>(oID);

    public CustomPayload.Id<ShowOverlayPayload<?>> getId() {
        return ID;
    }
    //? }

    @Environment(EnvType.CLIENT)
    public static void registerHandler() {
        //? if minecraft: >=1.20.6 {
        PayloadTypeRegistry./*? if legacy {*/playS2C/*? } else {*//*clientboundPlay*//*? }*/().register(ID, CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ID, new ShowOverlayHandler());
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(TYPE, new ShowOverlayHandler());
        *///?}
    }
}
