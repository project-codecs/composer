package com.codex.composer.internal.cca.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import com.codex.composer.internal.cca.ModCardinalComponents;

import static com.codex.composer.internal.registry.ModFeatures.TargetSynchronization.*;

//? if minecraft: <=1.20.4 {
/*import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
 *///? } else {
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
//?}

//? if minecraft: <=1.21.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
//? } else {
/*import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
*///? }

public class TargetedBlockComponent implements AutoSyncedComponent, ServerTickingComponent {
    private static final String POS = "pos";
    private static final String TICKS = "ticks";

    private final PlayerEntity player;
    private BlockPos pos = null;
    private int ticks = -1;

    public TargetedBlockComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        ModCardinalComponents.TARGETED_BLOCK.sync(player);
    }

    public @Nullable BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
        ticks = 0;
        sync();
    }

    public int getTicks() {
        return ticks;
    }

    //? if minecraft: <=1.21.5 {
    @Override
    public void readFromNbt(NbtCompound tag /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        this.pos = BlockPos.fromLong(tag.getLong(POS)/*? if minecraft: >=1.21.5 {*//*.orElse(BlockPos.ORIGIN.asLong())*//*? }*/);
        ticks = tag.getInt(TICKS)/*? if minecraft: >=1.21.5 {*//*.orElse(0)*//*? }*/;
    }

    @Override
    public void writeToNbt(NbtCompound tag /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        tag.putLong(POS, (pos == null ? BlockPos.ORIGIN : pos).asLong());
        tag.putInt(TICKS, ticks);
    }
    //? } else {
    /*@Override
    public void readData(ReadView tag) {
        pos = BlockPos.fromLong(tag.getLong(POS, BlockPos.ORIGIN.asLong()));
        ticks = tag.getInt(TICKS, 0);
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putLong(POS, (pos == null ? BlockPos.ORIGIN : pos).asLong());
        tag.putInt(TICKS, ticks);
    }
    *///? }

    @Override
    public void serverTick() {
        ticks++;
        if (block() && player.age % bFreq() == 0) sync();
    }
}
