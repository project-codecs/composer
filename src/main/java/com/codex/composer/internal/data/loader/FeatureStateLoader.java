package com.codex.composer.internal.data.loader;

import com.codex.composer.api.v1.runtime.ServerHolder;
import com.codex.composer.internal.runtime.ServerHolderImpl;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import com.codex.composer.internal.Composer;

public class FeatureStateLoader implements SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = Composer.identify("feature_state");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        if (ServerHolder.get().has()) ((ServerHolderImpl) ServerHolder.get()).reloadFeatures();
    }
}
