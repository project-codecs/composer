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

public class ModEnglishProvider extends SemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "zn0n_ Plush");
        stat(ModStatistics.PLUSH_BOOP, "zn0n_ Plushies Booped");
        group(ModItemGroups.COMPOSER, "Composer's Silly Little Additions");
        sound(ModSounds.ZN0N_SQUISH, "Plush Booped");

        prefix("composer.command");
        add("exception.player_not_found", "Player not found.");
        add("exception.no_players_found", "No players found.");
        add("exception.debug_not_enabled", "This command requires developer mode, which is not enabled! You can enable it in Composer's server config from mod menu or using /configure.");

        prefix("composer.credits");
        add("success_both", "Showing credits & end poem to %s players.");
        add("success_credits", "Showing credits to %s players.");
        add("success_poem", "Showing end poem to %s players.");
        add("screen.close", "Hold %s to close");

        prefix("composer.overlay");
        add("prefix", "Composer Overlays");
        add("cleared_all", "Successfully cleared all visible and queued overlays.");
        add("cleared_all_for", "Successfully cleared all visible and queued overlays for %s.");
        add("cleared_all_visible", "Successfully cleared all visible overlays.");
        add("cleared_all_visible_for", "Successfully cleared all visible overlays for %s.");
        add("cleared_all_queued", "Successfully cleared all queued overlays.");
        add("cleared_all_queued_for", "Successfully cleared all queued overlays for %s.");
        add("invalid_texture", "Invalid texture identifier.");
        add("sent_texture_scale_duration", "Sent textured overlay (%s) with scale %.2f for %d ticks.");
        add("sent_texture_scale_fade", "Sent textured overlay (%s) with scale %.2f (fade %d/%d/%d).");
        add("sent_texture_duration", "Sent textured overlay (%s) for %d ticks.");
        add("sent_texture_fade", "Sent textured overlay (%s) (fade %d/%d/%d).");
        add("sent_text_scale_duration", "Sent text overlay \"%s\" (%s) with scale %.2f for %d ticks.");
        add("sent_text_scale_fade", "Sent text overlay \"%s\" (%s) with scale %.2f (fade %d/%d/%d).");
        add("sent_text_duration", "Sent text overlay \"%s\" (%s) for %d ticks.");
        add("sent_text_fade", "Sent text overlay \"%s\" (%s) (fade %d/%d/%d).");

        prefix("composer");
        add("registry.prefix", "Composer Utilities");

        prefix("composer.tooltips");
        add("soulbound", "This item is soulbound");
        add("soulbound.droppable.not", "This item is not droppable when soulbound");

        //? if minecraft: <=1.21.6 {
        prefix("option.composer");
        add("composer.config.title", "Duplicate Keybinds", false);
        add("rainbow_speed", "Rainbow Effect Speed");
        add("rainbow_dupes", "Rainbow Effect On Duplicates");
        add("dupes_mode", "Duplicate Keybinds Mode");
        add("dupes_mode.tooltip", "Makes it so if two things have the same key bound to them, both are activated instead of just one of them.");
        translatableEnum(DupedBindsConfig.BindsMode.class, "None", "Minecraft & Mods That Use Composer", "All");
        //? }
    }
}
