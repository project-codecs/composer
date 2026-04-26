package com.codex.composer.api.v1.multiblock;

import com.codex.composer.api.v1.util.misc.AbstractPseudoRegistry;
import com.codex.composer.internal.Composer;

public final class MultiblockRegistry extends AbstractPseudoRegistry<Multiblock> {
    private static MultiblockRegistry INSTANCE;

    private MultiblockRegistry() {
        super();
    }

    @Override
    protected void bootstrap() {
        AbstractPseudoRegistry.identify(Composer.identify("multiblocks"), this);
    }

    public static MultiblockRegistry getInstance() {
        if (INSTANCE == null) INSTANCE = new MultiblockRegistry();
        return INSTANCE;
    }
}
