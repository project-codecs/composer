package com.codex.composer.api.v1.runtime;

import com.codex.composer.api.v1.feature.state.FeatureState;
import com.codex.composer.internal.runtime.ServerHolderImpl;
import net.minecraft.server.MinecraftServer;

public interface ServerHolder {
    FeatureState features();
    MinecraftServer server();
    boolean has();

    static ServerHolder get() {
        return ServerHolderImpl.INSTANCE;
    }
}
