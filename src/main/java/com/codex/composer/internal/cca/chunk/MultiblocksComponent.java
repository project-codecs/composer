package com.codex.composer.internal.cca.chunk;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


//? if minecraft: <=1.21.4
import net.minecraft.nbt.NbtElement;

//? if minecraft: <=1.21.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
//? } else {
/*import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
*///? }

public class MultiblocksComponent implements AutoSyncedComponent {
    private static final String MULTIBLOCKS = "multiblocks";
    private static final String POS = "pos";
    private static final String COMPLETE = "complete";
    private static final String ID = "id";

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

    //? if minecraft: <=1.21.5 {
    @Override
    public void readFromNbt(NbtCompound tag/*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        multiblocks.clear();

        tag.getList(MULTIBLOCKS/*? if minecraft: <=1.21.4 {*/, NbtElement.COMPOUND_TYPE/*? }*/)/*? if minecraft: >=1.21.5 {*//*.orElse(new NbtList())*//*? }*/.forEach(element -> {
            if (element instanceof NbtCompound compound) {
                multiblocks.add(new Pair<>(
                        BlockPos.fromLong(compound.getLong(POS)/*? if minecraft: >=1.21.5 {*//*.orElse(BlockPos.ORIGIN.asLong())*//*? }*/),
                        new Pair<>(Identifier.tryParse(compound.getString(ID)/*? if minecraft: >=1.21.5 {*//*.orElse("")*//*? }*/), compound.getBoolean(COMPLETE)/*? if minecraft: >=1.21.5 {*//*.orElse(false)*//*? }*/)
                ));
            }
        });
    }

    @Override
    public void writeToNbt(NbtCompound tag/*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        NbtList list = new NbtList();

        multiblocks.forEach(pair -> {
            NbtCompound compound = new NbtCompound();
            compound.putLong(POS, pair.getA().asLong());
            compound.putString(ID, pair.getB().getA().toString());
            compound.putBoolean(COMPLETE, pair.getB().getB());
            list.add(compound);
        });

        tag.put(MULTIBLOCKS, list);
    }
    //? } else {
    /*@Override
    public void readData(ReadView tag) {
        multiblocks.clear();

        for (ReadView element : tag.getListReadView(MULTIBLOCKS)) {
            multiblocks.add(new Pair<>(
                    BlockPos.fromLong(element.getLong(POS, BlockPos.ORIGIN.asLong())),
                    new Pair<>(Identifier.tryParse(element.getString(ID, "minecraft:air")), element.getBoolean(COMPLETE, false))
            ));
        }
    }

    @Override
    public void writeData(WriteView tag) {
        WriteView.ListView list = tag.getList(MULTIBLOCKS);

        multiblocks.forEach(pair -> {
            WriteView nbt = list.add();
            nbt.putLong(POS, pair.getA().asLong());
            nbt.putString(ID, pair.getB().getA().toString());
            nbt.putBoolean(COMPLETE, pair.getB().getB());
        });
    }
    *///? }
}
