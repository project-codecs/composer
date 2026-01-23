package com.codex.composer.internal.registry;

import net.minecraft.item.BlockItem;
import com.codex.composer.api.v1.item.settings.ComposerItemSettings;
import com.codex.composer.api.v1.registry.lazy.DeferredItemRegistry;
import com.codex.composer.internal.Composer;

public class ModItems {
    private static final DeferredItemRegistry REGISTRY = new DeferredItemRegistry(Composer.MOD_ID, ModItemGroups.COMPOSER);

    public static final BlockItem PLUSHIE = REGISTRY.register(
            ModBlocks.PLUSH,
            "plush",
            new ComposerItemSettings().soulbound(true)
    );

    public static void initialize() {
        REGISTRY.finalizeRegistration();
    }
}
