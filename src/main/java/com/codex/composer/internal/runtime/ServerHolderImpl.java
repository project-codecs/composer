package com.codex.composer.internal.runtime;

import net.minecraft.server.MinecraftServer;
import com.codex.composer.api.v1.feature.state.FeatureState;

public enum ServerHolderImpl implements com.codex.composer.api.v1.runtime.ServerHolder {
    INSTANCE;

    private MinecraftServer server;
    private FeatureState cachedFeatures;

    public void accept(MinecraftServer s) {
        server = s;
        reloadFeatures();
    }

    public MinecraftServer server() {
        return server;
    }

    public boolean has() {
        return server != null;
    }

    public FeatureState features() {
        if (server == null) throw new IllegalStateException("Server not initialized");

        if (cachedFeatures == null) {
            cachedFeatures = FeatureState.get(server);
        }
        return cachedFeatures;
    }

    public void reloadFeatures() {
        if (server != null) {
            cachedFeatures = FeatureState.get(server);
        }
    }
}
