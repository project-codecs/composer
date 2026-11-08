package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

//? if >=1.20.6 {
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import com.mojang.serialization.MapCodec;
//?} else {
/*import com.mojang.serialization.Codec;
*///?}

//? legacy {
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
//? } else
//import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

public class DeferredParticleRegistry extends EmptyDeferredRegistry {
    public DeferredParticleRegistry(String modId) {
        super(modId);
    }

    //? if >=1.20.6 {
    public <T extends ParticleEffect> ParticleType<T> register(String name, MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return Registry.register(Registries.PARTICLE_TYPE, id(name),
                new ParticleType<T>(true) {
                    @Override
                    public MapCodec<T> getCodec() {
                        return codec;
                    }

                    @Override
                    public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                        return packetCodec;
                    }
                });
    }

    public <P extends ParticleEffect, T extends ParticleType<P>> T register(String name, T type) {
        return Registry.register(Registries.PARTICLE_TYPE, id(name), type);
    }
    //?}

    //? if <=1.20.4{
    /*public <T extends ParticleEffect> ParticleType<T> register(
            String name,
            Codec<T> codec,
            @SuppressWarnings("deprecation") ParticleEffect.Factory<T> deserializer
    ) {
        Identifier id = id(name);
        return Registry.register(
                Registries.PARTICLE_TYPE,
                id,
                new ParticleType<T>(true, deserializer) {
                    @Override
                    public Codec<T> getCodec() {
                        return codec;
                    }
                }
        );
    }
    *///?}

    public <T extends ParticleEffect> void registerClient(ParticleType<T> type, /*? if legacy {*/ParticleFactoryRegistry.PendingParticleFactory/*? } else {*//*ParticleProviderRegistry.PendingParticleProvider*//*? }*/<T> factory) {
        /*? if legacy {*/ParticleFactoryRegistry/*? } else {*//*ParticleProviderRegistry*//*? }*/.getInstance().register(type, factory);
    }
}
