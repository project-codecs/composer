package com.codex.composer.internal.data.assets.languages;

import com.codex.composer.api.v1.datagen.lang.ComposerSemiLanguageProvider;
import com.codex.composer.internal.client.config.ComposerConfig;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static com.codex.composer.internal.registry.ModFeatures.DEBUG;
import static com.codex.composer.internal.registry.ModFeatures.TargetSynchronization;

public class ModShakespeareanProvider extends ComposerSemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "LilBro's Plush Effigy");
        stat(ModStatistics.PLUSH_BOOP, "LilBro Effigies Prodded");
        group(ModItemGroups.COMPOSER, "The Composer's Merry Trifling Additions");

        enumTranslatable(
                ComposerConfig.BindsMode.class,
                "None Whatsoever",
                "Vanilla & Such Mods as Doth Employ Composer",
                "All Manner of Things"
        );

        sound(ModSounds.LILBRO_SQUISH, "The Plush Effigy Hath Been Prodded");

        feature(TargetSynchronization.ENTITY, "Doth synchronise the target entities of players unto the client. The frequency governeth how oft (in ticks) updates are dispatched. To alter this or to disable it may bring ruin upon other mods.");
        feature(TargetSynchronization.BLOCK, "Doth synchronise the target blocks of players unto the client. The frequency governeth how oft (in ticks) updates are dispatched. To alter this or to disable it may bring ruin upon other mods.");
        feature(DEBUG, "Doth enable certain debug commands and features not ordinarily accessible. Commands require a world reload to appear or vanish, yet shall remain disabled whensoever this option is altered.");

        prefix("command.exception.player_not_found", "The player hath not been found.");
        prefix("command.exception.no_players_found", "No players hath been found.");
        prefix("command.exception.debug_not_enabled", "This command requireth debug mode, which is not presently enabled! Re-log or execute /reload that this command may vanish, or enable debug mode by means of /features.");

        prefix("credits.success_both", "Displaying credits & the end poem unto %s players.");
        prefix("credits.success_credits", "Displaying credits unto %s players.");
        prefix("credits.success_poem", "Displaying the end poem unto %s players.");
        prefix("credits.screen.close", "Hold %s to dismiss this");

        prefix("feature.enable", "Feature %s hath been enabled");
        prefix("feature.disable", "Feature %s hath been disabled");
        prefix("feature.missing", "Feature %s is unknown to us");
        prefix("feature.description.missing", "No description hath been provided.");
        prefix("feature.prefix", "Composer's Features");

        prefix("toast.cleared_for_player", "Toasts for %s hath been cleared.");
        prefix("toast.cleared_all", "All toasts hath been most successfully cleared.");
        prefix("toast.invalid_icon_texture", "The identifier for the icon texture is most invalid.");
        prefix("toast.sent_simple", "Toast dispatched: %s\n | Icon texture: %s\n | Background colour: %s\n | Border colour: %s");
        prefix("toast.sent_notify", "Notify toast dispatched: %s\n | Background colour: %s\n | Border colour: %s");
        prefix("toast.prefix", "Composer's Toasts");

        prefix("overlay.prefix", "Composer's Utilities");
        prefix("overlay.cleared_all", "All credits and queued overlays hath been most successfully cleared.");
        prefix("overlay.cleared_all_for", "All credits and queued overlays for %s hath been most successfully cleared.");
        prefix("overlay.cleared_all_visible", "All credits overlays hath been most successfully cleared.");
        prefix("overlay.cleared_all_visible_for", "All credits overlays for %s hath been most successfully cleared.");
        prefix("overlay.cleared_all_queued", "All queued overlays hath been most successfully cleared.");
        prefix("overlay.cleared_all_queued_for", "All queued overlays for %s hath been most successfully cleared.");
        prefix("overlay.invalid_texture", "The texture identifier is most invalid.");
        prefix("overlay.sent_texture_scale_duration", "A textured overlay (%s) hath been dispatched with scale %.2f for %d ticks.");
        prefix("overlay.sent_texture_scale_fade", "A textured overlay (%s) hath been dispatched with scale %.2f (fade %d/%d/%d).");
        prefix("overlay.sent_texture_duration", "A textured overlay (%s) hath been dispatched for %d ticks.");
        prefix("overlay.sent_texture_fade", "A textured overlay (%s) hath been dispatched (fade %d/%d/%d).");
        prefix("overlay.sent_text_scale_duration", "A text overlay \"%s\" (%s) hath been dispatched with scale %.2f for %d ticks.");
        prefix("overlay.sent_text_scale_fade", "A text overlay \"%s\" (%s) hath been dispatched with scale %.2f (fade %d/%d/%d).");
        prefix("overlay.sent_text_duration", "A text overlay \"%s\" (%s) hath been dispatched for %d ticks.");
        prefix("overlay.sent_text_fade", "A text overlay \"%s\" (%s) hath been dispatched (fade %d/%d/%d).");

        prefix("registry.prefix", "Composer's Utilities");
        prefix("dynamic_tooltips.hidden", "Press %s to reveal %s");

        prefix("tooltips.soulbound", "This item is bound to thy soul");
        prefix("tooltips.soulbound.not", "This item is not bound to thy soul");
        prefix("tooltips.soulbound.details", "binding particulars");
        prefix("tooltips.soulbound.droppable", "This item may be dropped even whilst bound to thy soul");
        prefix("tooltips.soulbound.droppable.not", "This item may not be dropped whilst bound to thy soul");
    }

    @Override
    public String prefix() {
        return "composer";
    }
}