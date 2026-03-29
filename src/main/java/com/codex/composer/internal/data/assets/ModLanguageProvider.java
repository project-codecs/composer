package com.codex.composer.internal.data.assets;

import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import com.codex.composer.api.v1.datagen.ComposerLanguageProvider;
import com.codex.composer.internal.client.config.ComposerConfig;

//? minecraft: >=1.20.6 {
import net.minecraft.registry.RegistryWrapper;
import java.util.concurrent.CompletableFuture;
//? }

import static com.codex.composer.internal.registry.ModFeatures.*;

public class ModLanguageProvider extends ComposerLanguageProvider {
    //? if minecraft: <=1.20.4 {
    /*public ModLanguageProvider(FabricDataOutput output) {
        super(output);
    }
    *///? } else {
    public ModLanguageProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }
    //? }

    @Override
    public void generate() {
        block(ModBlocks.PLUSH, "LilBro Plush");
        //? if minecraft: >=1.21.3
        item(ModBlocks.PLUSH, "LilBro Plush");
        stat(ModStatistics.PLUSH_BOOP, "LilBro Plushies Booped");
        group(ModItemGroups.COMPOSER, "Composer's Silly Little Additions");

        enumTranslatable(
                ComposerConfig.BindsMode.class,
                "None",
                "Vanilla & Mods that use Composer",
                "All"
        );

        sound(ModSounds.LILBRO_SQUISH, "Plush Booped");

        feature(TargetSynchronization.ENTITY, "Synchronizes players' target entities to the client. Frequency controls how often (in ticks) updates are sent. Changing this or disabling it may break other mods.");
        feature(TargetSynchronization.BLOCK, "Synchronizes players' target blocks to the client. Frequency controls how often (in ticks) updates are sent. Changing this or disabling it may break other mods.");
        feature(DEBUG, "Enables some debug commands and features usually not accessible. Commands require a world reload to appear/disappear, but will still be disabled when this option is changed.");

        prefix("command.exception.player_not_found", "Player not found.");
        prefix("command.exception.no_players_found", "No players found.");
        prefix("command.exception.debug_not_enabled", "This command requires debug mode, which is not enabled! Re-log or run /reload to make this command disappear, or enable debug mode using /features.");

        prefix("credits.success_both", "Showing credits & end poem to %s players.");
        prefix("credits.success_credits", "Showing credits to %s players.");
        prefix("credits.success_poem", "Showing end poem to %s players.");
        prefix("credits.screen.close", "Hold %s to close");

        prefix("feature.enable", "Enabled feature %s");
        prefix("feature.disable", "Disabled feature %s");
        prefix("feature.missing", "Unknown feature %s");
        prefix("feature.description.missing", "No description was provided.");
        prefix("feature.prefix", "Composer Features");

        prefix("toast.cleared_for_player", "Cleared toasts for %s.");
        prefix("toast.cleared_all", "Successfully cleared all toasts.");
        prefix("toast.invalid_icon_texture", "Invalid identifier for icon texture.");
        prefix("toast.sent_simple", "Sent toast: %s\n | Icon texture: %s\n | Background color: %s\n | Border color: %s");
        prefix("toast.sent_notify", "Sent notify toast: %s\n | Background color: %s\n | Border color: %s");
        prefix("toast.prefix", "Composer Toasts");

        prefix("overlay.prefix", "Composer Overlays");
        prefix("overlay.cleared_all", "Successfully cleared all credits and queued overlays.");
        prefix("overlay.cleared_all_for", "Successfully cleared all credits and queued overlays for %s.");
        prefix("overlay.cleared_all_visible", "Successfully cleared all credits overlays.");
        prefix("overlay.cleared_all_visible_for", "Successfully cleared all credits overlays for %s.");
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
