package com.codex.composer.internal.data.assets.languages;

import com.codex.composer.api.v1.datagen.lang.ComposerSemiLanguageProvider;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import com.codex.composer.internal.client.config.ComposerClientConfig;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static com.codex.composer.internal.registry.ModFeatures.*;

public class ModEnglishProvider extends ComposerSemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "LilBro Plush");
        stat(ModStatistics.PLUSH_BOOP, "LilBro Plushies Booped");
        group(ModItemGroups.COMPOSER, "Composer's Silly Little Additions");

        enumTranslatable(
                ComposerClientConfig.BindsMode.class,
                "None",
                "Vanilla & Mods that use Composer",
                "All"
        );

        sound(ModSounds.LILBRO_SQUISH, "Plush Booped");

        feature(TargetSynchronization.ENTITY, "Synchronizes players' target entities to the client. Frequency controls how often (in ticks) updates are sent. Changing this or disabling it may break other mods.");
        feature(TargetSynchronization.BLOCK, "Synchronizes players' target blocks to the client. Frequency controls how often (in ticks) updates are sent. Changing this or disabling it may break other mods.");

        prefix("command.exception.player_not_found", "Player not found.");
        prefix("command.exception.no_players_found", "No players found.");
        prefix("command.exception.debug_not_enabled", "This command requires developer mode, which is not enabled! You can enable it in Composer's server config from mod menu or using /configure.");

        prefix("credits.success_both", "Showing credits & end poem to %s players.");
        prefix("credits.success_credits", "Showing credits to %s players.");
        prefix("credits.success_poem", "Showing end poem to %s players.");
        prefix("credits.screen.close", "Hold %s to close");

        prefix("feature.enable", "Enabled feature %s");
        prefix("feature.disable", "Disabled feature %s");
        prefix("feature.missing", "Unknown feature %s");
        prefix("feature.description.missing", "No description was provided.");
        prefix("feature.prefix", "Composer Features");

        prefix("overlay.prefix", "Composer Overlays");
        prefix("overlay.cleared_all", "Successfully cleared all visible and queued overlays.");
        prefix("overlay.cleared_all_for", "Successfully cleared all visible and queued overlays for %s.");
        prefix("overlay.cleared_all_visible", "Successfully cleared all visible overlays.");
        prefix("overlay.cleared_all_visible_for", "Successfully cleared all visible overlays for %s.");
        prefix("overlay.cleared_all_queued", "Successfully cleared all queued overlays.");
        prefix("overlay.cleared_all_queued_for", "Successfully cleared all queued overlays for %s.");
        prefix("overlay.invalid_texture", "Invalid texture identifier.");
        prefix("overlay.sent_texture_scale_duration", "Sent textured overlay (%s) with scale %.2f for %d ticks.");
        prefix("overlay.sent_texture_scale_fade", "Sent textured overlay (%s) with scale %.2f (fade %d/%d/%d).");
        prefix("overlay.sent_texture_duration", "Sent textured overlay (%s) for %d ticks.");
        prefix("overlay.sent_texture_fade", "Sent textured overlay (%s) (fade %d/%d/%d).");
        prefix("overlay.sent_text_scale_duration", "Sent text overlay \"%s\" (%s) with scale %.2f for %d ticks.");
        prefix("overlay.sent_text_scale_fade", "Sent text overlay \"%s\" (%s) with scale %.2f (fade %d/%d/%d).");
        prefix("overlay.sent_text_duration", "Sent text overlay \"%s\" (%s) for %d ticks.");
        prefix("overlay.sent_text_fade", "Sent text overlay \"%s\" (%s) (fade %d/%d/%d).");

        prefix("registry.prefix", "Composer Utilities");
        prefix("dynamic_tooltips.hidden", "Press %s to show %s");

        prefix("tooltips.soulbound", "This item is soulbound");
        prefix("tooltips.soulbound.not", "This item is not soulbound");
        prefix("tooltips.soulbound.details", "binding details");
        prefix("tooltips.soulbound.droppable", "This item is droppable when soulbound");
        prefix("tooltips.soulbound.droppable.not", "This item is not droppable when soulbound");
    }

    @Override
    public String prefix() {
        return "composer";
    }
}
