package com.codex.composer.internal.client.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;
import com.codex.composer.internal.Composer;

//? if minecraft: <=1.20.1 || >=1.21 {
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
//? }

@SuppressWarnings("CanBeFinal")
public class ComposerConfig extends Config {
    public ComposerConfig() {
        super(Composer.identify("config"));
    }

    public static ComposerConfig INSTANCE;

    @Override
    public int defaultPermLevel() {
        return 0;
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

    //? if minecraft: <=1.20.1 || >=1.21
    @Name("Rainbow Effect on Duplicate Keybinds")
    public boolean rainbowEffectOnDuplicateKeybinds = false;

    //? if minecraft: <=1.20.1 || >=1.21
    @Name("Rainbow Effect Speed")
    public ValidatedInt rainbowEffectSpeed = new ValidatedInt(3, 10, 1);

    //? if minecraft: <=1.20.1 || >=1.21
    @Name("Allow Duplicate Keybinds")
    public BindsMode allowDuplicateKeybinds = BindsMode.MC_AND_CM;

    //? if minecraft: <=1.20.1 || >=1.21
    @Prefix("Always shows the \"Press <> to show <>\" in tooltips (or the tooltip if the keys are held) for all tooltips, regardless of them being relevant or not.")
    public ValidatedBoolean alwaysShowTooltips = new ValidatedBoolean(false);

    public static void initialize() {
        INSTANCE = ConfigApiJava.registerAndLoadConfig(ComposerConfig::new, RegisterType.CLIENT);
    }

    public enum BindsMode implements EnumTranslatable {
        NONE, MC_AND_CM, ALL;

        @Override
        public @NotNull String prefix() {
            return "composer.binds_mode";
        }
    }
}
