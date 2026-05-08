package com.codex.composer.internal.runtime;

import com.codex.composer.api.v1.util.runtime.ServerHolder;
import net.minecraft.server.MinecraftServer;

public enum ServerHolderImpl implements ServerHolder {
    INSTANCE;

    private MinecraftServer server;

    public void accept(MinecraftServer s) {
        server = s;
    }

    public MinecraftServer server() {
        return server;
    }

    public boolean has() {
        return server != null;
    }
}
