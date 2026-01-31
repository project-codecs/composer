package com.codex.composer.internal.client.duped_binds;

import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.ApiStatus;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.client.config.ComposerConfig;

import java.util.Collection;
import java.util.HashSet;

public class BindTracker {
    @ApiStatus.Internal
    public static final Collection<KeyBinding> MC_CM_BINDS = new HashSet<>();

    @ApiStatus.Internal
    public static boolean bindAllowed(KeyBinding keyBinding) {
        if (Composer.disableDupedBinds()) return false;
        if (keyBinding == null) return false;
        return switch (ComposerConfig.INSTANCE.allowDuplicateKeybinds) {
            case NONE -> false;
            case MC_AND_CM -> MC_CM_BINDS.contains(keyBinding);
            case ALL -> true;
        };
    }
}
