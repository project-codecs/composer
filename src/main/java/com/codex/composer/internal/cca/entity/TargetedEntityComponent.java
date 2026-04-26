package com.codex.composer.internal.cca.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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

    @Override
    public void writeToNbt(NbtCompound nbtCompound /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        if (this.uuid != null) {
            nbtCompound.putString(UUID_KEY, uuid.toString());
        }
        nbtCompound.putInt(TICKS_KEY, ticks);
    }

    @Override
    public void readFromNbt(NbtCompound tag /*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        if (tag.contains(UUID_KEY)) {
            //? if minecraft: <=1.21.4
            //this.uuid = tag.contains(UUID_KEY) ? NbtHelper.toUuid(Objects.requireNonNull(tag.get(UUID_KEY))) : null;
            //? if minecraft: >=1.21.5
            this.uuid = tag.getString(UUID_KEY).map(UUID::fromString).orElse(null);
        } else {
            this.uuid = null;
        }
        ticks = tag.getInt(TICKS_KEY)/*? if minecraft: >=1.21.5 {*/.orElse(0)/*? }*/;
    }
}
