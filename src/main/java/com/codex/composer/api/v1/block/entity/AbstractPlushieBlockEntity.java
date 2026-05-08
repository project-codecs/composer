package com.codex.composer.api.v1.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if minecraft: >=1.21.6 {
/*import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
*///? }

//? if minecraft: >=1.20.6
import net.minecraft.registry.RegistryWrapper;

public abstract class AbstractPlushieBlockEntity extends BlockEntity {
    private static final float SQUASH = 3f;
    private static final float SQUASH_EPS = 0.01f;
    public double squash;

    public AbstractPlushieBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, @NotNull AbstractPlushieBlockEntity spark) {
        if (spark.squash > 0) {
            spark.squash /= SQUASH;
            if (spark.squash < SQUASH_EPS) {
                spark.squash = 0;
                if (world != null) world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
            }
        }
    }

    public void squish(int squash) {
        this.squash += squash;
        if (this.world != null)
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        this.markDirty();
    }

    //? if minecraft: <=1.21.5 {
    @Override
    protected void writeNbt(@NotNull NbtCompound nbt /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        nbt.putDouble("squash", this.squash);
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbt /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        this.squash = nbt.getDouble("squash")/*? if minecraft: >=1.21.5 {*//*.orElse(0d)*//*? }*/;
    }
    //? } else {
    /*@Override
    protected void writeData(WriteView view) {
        view.putDouble("squash", this.squash);
    }

    @Override
    protected void readData(ReadView view) {
        this.squash = view.getDouble("squash", 0d);
    }
    *///? }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt( /*? if minecraft: >= 1.20.6 { */RegistryWrapper.WrapperLookup registries /*?}*/) {
        return this.createNbt( /*? if minecraft: >= 1.20.6 { */registries /*?}*/);
    }
}
