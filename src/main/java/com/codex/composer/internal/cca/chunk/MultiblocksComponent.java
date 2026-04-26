package com.codex.composer.internal.cca.chunk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//? if minecraft: <=1.20.4
//import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

//? if minecraft: >=1.20.6
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class MultiblocksComponent implements AutoSyncedComponent {
    private static final String MULTIBLOCKS_KEY = "multiblocks";

    public final List<Pair<BlockPos, Pair<Identifier, Boolean>>> multiblocks = new CopyOnWriteArrayList<>();

    public MultiblocksComponent(World ignored) {
    }

    public void computeIfAbsent(BlockPos pos, Identifier id) {
        if (multiblocks.stream().map(Pair::getA).noneMatch(iPos -> iPos.equals(pos))) {
            multiblocks.add(new Pair<>(pos, new Pair<>(id, false)));
        }
    }

    public void removeIfPresent(BlockPos pos) {
        multiblocks.removeIf(pair -> pair.getA().equals(pos));
    }

    public void markAs(BlockPos pos, boolean completed) {
        for (int i = 0; i < multiblocks.size(); i++) {
            Pair<BlockPos, Pair<Identifier, Boolean>> pair = multiblocks.get(i);

            if (pair.getA().equals(pos)) {
                Identifier id = pair.getB().getA();
                multiblocks.set(i, new Pair<>(pos, new Pair<>(id, completed)));
                return;
            }
        }
    }

    public boolean isCompleted(BlockPos pos) {
        return multiblocks.stream()
                .filter(pair -> pair.getA().equals(pos))
                .map(pair -> pair.getB().getB())
                .findFirst()
                .orElse(false);
    }

    @Override
    public void readFromNbt(NbtCompound tag/*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        multiblocks.clear();

        tag.getList(MULTIBLOCKS_KEY, NbtElement.COMPOUND_TYPE).forEach(element -> {
            if (element instanceof NbtCompound compound) {
                multiblocks.add(new Pair<>(
                        BlockPos.fromLong(compound.getLong("pos")),
                        new Pair<>(Identifier.tryParse(compound.getString("id")), compound.getBoolean("complete"))
                ));
            }
        });
    }

    @Override
    public void writeToNbt(NbtCompound tag/*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        NbtList list = new NbtList();

        multiblocks.forEach(pair -> {
            NbtCompound compound = new NbtCompound();
            compound.putLong("pos", pair.getA().asLong());
            compound.putString("id", pair.getB().getA().toString());
            compound.putBoolean("complete", pair.getB().getB());
            list.add(compound);
        });

        tag.put(MULTIBLOCKS_KEY, list);
    }
}
