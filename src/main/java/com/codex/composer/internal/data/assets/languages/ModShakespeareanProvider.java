package com.codex.composer.internal.data.assets.languages;

import com.codex.composer.api.v1.datagen.lang.SemiLanguageProvider;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

//? if minecraft: <=1.21.6
import com.codex.composer.internal.client.config.DupedBindsConfig;

public class ModShakespeareanProvider extends SemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "LilBro's Plush Effigy");
        stat(ModStatistics.PLUSH_BOOP, "LilBro Effigies Prodded");
        group(ModItemGroups.COMPOSER, "The Composer's Merry Trifling Additions");
        sound(ModSounds.ZN0N_SQUISH, "The Plush Effigy Hath Been Prodded");

        prefix("composer.command");
        add("exception.player_not_found", "The player hath not been found.");
        add("exception.no_players_found", "No players hath been found.");
        add("exception.debug_not_enabled", "This command requireth developer mode, which is not presently enabled! Thou can enable it in Composer's server config from mod menu or using /configure.");

        prefix("composer.credits");
        add("success_both", "Displaying credits & the end poem unto %s players.");
        add("success_credits", "Displaying credits unto %s players.");
        add("success_poem", "Displaying the end poem unto %s players.");
        add("screen.close", "Hold %s to dismiss this");

        prefix("composer.overlay");
        add("prefix", "Composer's Utilities");
        add("cleared_all", "All credits and queued overlays hath been most successfully cleared.");
        add("cleared_all_for", "All credits and queued overlays for %s hath been most successfully cleared.");
        add("cleared_all_visible", "All credits overlays hath been most successfully cleared.");
        add("cleared_all_visible_for", "All credits overlays for %s hath been most successfully cleared.");
        add("cleared_all_queued", "All queued overlays hath been most successfully cleared.");
        add("cleared_all_queued_for", "All queued overlays for %s hath been most successfully cleared.");
        add("invalid_texture", "The texture identifier is most invalid.");
        add("sent_texture_scale_duration", "A textured overlay (%s) hath been dispatched with scale %.2f for %d ticks.");
        add("sent_texture_scale_fade", "A textured overlay (%s) hath been dispatched with scale %.2f (fade %d/%d/%d).");
        add("sent_texture_duration", "A textured overlay (%s) hath been dispatched for %d ticks.");
        add("sent_texture_fade", "A textured overlay (%s) hath been dispatched (fade %d/%d/%d).");
        add("sent_text_scale_duration", "A text overlay \"%s\" (%s) hath been dispatched with scale %.2f for %d ticks.");
        add("sent_text_scale_fade", "A text overlay \"%s\" (%s) hath been dispatched with scale %.2f (fade %d/%d/%d).");
        add("sent_text_duration", "A text overlay \"%s\" (%s) hath been dispatched for %d ticks.");
        add("sent_text_fade", "A text overlay \"%s\" (%s) hath been dispatched (fade %d/%d/%d).");

        prefix("composer");
        add("registry.prefix", "Composer's Utilities");

        prefix("composer.tooltips");
        add("soulbound", "This item is bound to thy soul");
        add("soulbound.droppable.not", "This item may not be dropped whilst bound to thy soul");

        //? if minecraft: <=1.21.6 {
        prefix("option.composer");
        add("composer.config.title", "Duplicate Keybinds", false);
        add("rainbow_speed", "Speed of the Rainbow Effect");
        add("rainbow_dupes", "Rainbow Effect Upon Duplicates");
        add("dupes_mode", "Mode of Duplicate Keybinds");
        add("dupes_mode.tooltip", "Maketh it so that should two things bear the selfsame key unto them, both shall be activated, rather than but one.");
        translatableEnum(DupedBindsConfig.BindsMode.class, "None", "Minecraft & Mods That Use Composer", "All");
        //? }
    }
}