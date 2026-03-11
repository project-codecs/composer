package com.codex.composer.api.v1.registry.lazy;

//? if minecraft: >= 1.21 {
import net.minecraft.component.ComponentType;
//? } else if minecraft: >=1.20.6 {
/*import net.minecraft.component.DataComponentType;
 *///? }

//? if minecraft: >=1.20.6 {
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import com.mojang.serialization.Codec;

import java.util.function.UnaryOperator;

public class DeferredDataComponentTypeRegistry extends EmptyDeferredRegistry {
    public DeferredDataComponentTypeRegistry(String modId) {
        super(modId);
    }

    public <T> /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/<T> register(String name, UnaryOperator</*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/.Builder<T>> handler) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(modId, name),
                handler.apply(/*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/.builder()).build()
        );
    }

    public <T> /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/<T> register(String name, TypePrefab<T> type) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(modId, name),
                type.make()
        );
    }

    public static final class TypePrefab<T> {
        public static final TypePrefab<String> STRING = new TypePrefab<>(builder -> builder
                .codec(Codec.STRING)
                .packetCodec(PacketCodecs.STRING)
        );

        public static final TypePrefab<Boolean> BOOLEAN = new TypePrefab<>(builder -> builder
                .codec(Codec.BOOL)
                .packetCodec(PacketCodecs./*? minecraft: >=1.21.4 {*/BOOLEAN/*? } else {*//*BOOL*//*? }*/)
        );

        public static final TypePrefab<Integer> NON_NEGATIVE_INT = new TypePrefab<>(builder -> builder
                .codec(Codecs./*? minecraft: >=1.21.3 {*/NON_NEGATIVE_INT/*? } else {*//*NONNEGATIVE_INT*//*? }*/)
                .packetCodec(PacketCodecs.INTEGER)
        );

        public static final TypePrefab<Integer> INTEGER = new TypePrefab<>(builder -> builder
                .codec(Codec.INT)
                .packetCodec(PacketCodecs.INTEGER)
        );

        public static final TypePrefab<Float> FLOAT = new TypePrefab<>(builder -> builder
                .codec(Codec.FLOAT)
                .packetCodec(PacketCodecs.FLOAT)
        );

        public static final TypePrefab<Double> DOUBLE = new TypePrefab<>(builder -> builder
                .codec(Codec.DOUBLE)
                .packetCodec(PacketCodecs.DOUBLE)
        );

        private final UnaryOperator<
                /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/
                .Builder<T>
                > handler;

        private TypePrefab(UnaryOperator<
                /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/
                .Builder<T>
                > handler) {
            this.handler = handler;
        }

        public /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/<T> make() {
            return handler.apply(/*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/.builder()).build();
        }
    }
}
//? }