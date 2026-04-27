package com.codex.composer.internal.cca.entity;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import com.codex.composer.internal.cca.ModCardinalComponents;

import java.util.UUID;

import static com.codex.composer.internal.registry.ModFeatures.TargetSynchronization.*;

//? if minecraft: <=1.20.4 {
/*import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
*///? } else {
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.registry.RegistryWrapper;
//?}

//? if minecraft: <=1.21.5 {
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtCompound;
import java.util.Objects;
//? } else {
/*import net.minecraft.storage.ReadView;
import java.util.Optional;
import net.minecraft.storage.WriteView;
*///? }

public class TargetedEntityComponent implements AutoSyncedComponent, ServerTickingComponent {
    private static final String UUID_KEY = "uuid";
    private static final String TICKS_KEY = "ticks";

    private final PlayerEntity player;
    private UUID uuid = null;
    private int ticks = -1;

    public TargetedEntityComponent(PlayerEntity player) {
        this.player = player;
    }

    public @Nullable UUID getUuid() {
        return uuid;
    }

    public void sync() {
        ModCardinalComponents.TARGETED_ENTITY.sync(player);
    }

    public void setUuid(UUID pos) {
        this.uuid = pos;
        ticks = 0;
        sync();
    }

    public int getTicks() {
        return ticks;
    }

    public void serverTick() {
        ticks++;
        if (entity() && player.age % eFreq() == 0) sync();
    }

    //? if minecraft: <=1.21.5 {
    @Override
    public void readFromNbt(NbtCompound tag /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        if (tag.contains(UUID_KEY)) {
            //? if minecraft: <=1.21.4
            this.uuid = tag.contains(UUID_KEY) ? NbtHelper.toUuid(Objects.requireNonNull(tag.get(UUID_KEY))) : null;
            //? if minecraft: >=1.21.5
            //this.uuid = tag.getString(UUID_KEY).map(UUID::fromString).orElse(null);
        } else {
            this.uuid = null;
        }
        ticks = tag.getInt(TICKS_KEY)/*? if minecraft: >=1.21.5 {*//*.orElse(0)*//*? }*/;
    }

    @Override
    public void writeToNbt(NbtCompound tag /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        if (this.uuid != null) tag.putString(UUID_KEY, uuid.toString());
        tag.putInt(TICKS_KEY, ticks);
    }
    //? } else {
    /*@Override
    public void readData(ReadView tag) {
        uuid = tag.getOptionalString(UUID_KEY).map(UUID::fromString).orElse(null);
        ticks = tag.getInt(TICKS_KEY, 0);
    }

    @Override
    public void writeData(WriteView tag) {
        if (uuid != null) tag.putString(UUID_KEY, uuid.toString());
        tag.putInt(TICKS_KEY, ticks);
    }
    *///? }
}
