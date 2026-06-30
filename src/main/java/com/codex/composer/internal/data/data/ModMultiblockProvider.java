package com.codex.composer.internal.data.data;

//? if !release {
import com.codex.composer.api.v1.datagen.ComposerMultiblockProvider;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataWriter;
import net.minecraft.util.math.Vec3i;

public class ModMultiblockProvider extends ComposerMultiblockProvider {
    public ModMultiblockProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(DataWriter writer) {
        create(Composer.identify("ritual"))
                .controller(new Vec3i(1, 1, 0))
                .layer().rows("NNN", 3).end()
                .layer().row("T*T").row("TFT").row("TPT").end()
                .pattern('P').block(ModBlocks.PLUSH)
                .pattern('N').block(Blocks.NETHERRACK)
                .pattern('T').block(Blocks.REDSTONE_TORCH)
                .pattern('F').block(Blocks.FIRE)
                .write(writer);
    }
}
//? }
