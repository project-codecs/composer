package com.codex.composer.internal.config;

import com.codex.composer.internal.Composer;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import org.jetbrains.annotations.NotNull;

//? if minecraft: <=1.20.1 || >=1.21 {
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
//? }

public class ComposerServerConfig extends Config {
    public ComposerServerConfig() {
        super(Composer.identify("server-config"));
    }

    //? if minecraft: <=1.20.1 || >=1.21 {
    @Override
    public @NotNull FileType fileType() {
        return FileType.JSONC;
    }

    @Override
    public @NotNull SaveType saveType() {
        return SaveType.SEPARATE;
    }
    //? }

    //? if minecraft: <=1.20.1 || >=1.21 {
    @Name("Developer Mode")
    @Prefix("Adds some extra command functionality and other features to help with debugging the mod.")
    //? }
    public ValidatedBoolean developerMode = new ValidatedBoolean(false);

    public static ComposerServerConfig INSTANCE;

    public static void initialize() {
        INSTANCE = ConfigApiJava.registerAndLoadConfig(ComposerServerConfig::new, RegisterType.BOTH);
    }
}
