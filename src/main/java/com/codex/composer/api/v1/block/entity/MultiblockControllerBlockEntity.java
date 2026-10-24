package com.codex.composer.api.v1.block.entity;

import com.codex.composer.api.v1.multiblock.ingame.MultiblockController;
import com.codex.composer.api.v1.multiblock.ingame.MultiblockHost;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

//? if minecraft: <=1.21.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
//? } else {
/*import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
*///? }

public abstract class MultiblockControllerBlockEntity extends BlockEntity implements MultiblockHost {
    private static final String FORMED = "formed";
    private final MultiblockController controller = new MultiblockController(this);

    protected MultiblockControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public MultiblockController getMultiblockController() {
        return controller;
    }

    //? if minecraft: <=1.21.5 {
    @Override
    //~ if <=1.20.4 'protected' -> 'public'
    protected void readNbt(NbtCompound nbt/*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        super.readNbt(nbt/*? if minecraft: >= 1.20.6 { */, registries /*?}*/);
        controller.restoreFormed(nbt.getBoolean(FORMED)/*? if minecraft: >=1.21.5 {*//*.orElse(false)*//*? }*/);
    }

    @Override
    protected void writeNbt(NbtCompound nbt/*? if minecraft: >= 1.20.6 { */, RegistryWrapper.WrapperLookup registries /*?}*/) {
        super.writeNbt(nbt/*? if minecraft: >= 1.20.6 { */, registries /*?}*/);
        nbt.putBoolean(FORMED, controller.isFormed());
    }
    //? } else {
    /*@Override
    protected void readData(ReadView view) {
        super.readData(view);
        controller.restoreFormed(view.getBoolean(FORMED, false));
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putBoolean(FORMED, controller.isFormed());
    }
    *///? }
}