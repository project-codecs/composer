package com.codex.composer.api.v1.util.runtime;

import com.codex.composer.internal.runtime.ServerHolderImpl;
import net.minecraft.server.MinecraftServer;

public interface ServerHolder {
    MinecraftServer server();
    boolean has();

    static ServerHolder get() {
        return ServerHolderImpl.INSTANCE;
    }
}
