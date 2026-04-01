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

public class ModLolcatProvider extends ComposerSemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "LilBro Plushie (SO SQUISHY)");
        stat(ModStatistics.PLUSH_BOOP, "LilBro Plushiez Booped (hehehe)");
        group(ModItemGroups.COMPOSER, "Composer's Silly Lil Addishuns (iz funni)");

        enumTranslatable(
                ComposerConfig.BindsMode.class,
                "Nun",
                "Vanilla & Modz Dat Use Composer",
                "ALL TEH THINGZ"
        );

        sound(ModSounds.LILBRO_SQUISH, "Plushie Booped!! :3");

        feature(TargetSynchronization.ENTITY, "Makez playerz's target entitiez go to teh client. Frequency iz how often (in tickz) updatez iz sended. Changing dis or turning it off mite brek other modz (oh noes).");
        feature(TargetSynchronization.BLOCK, "Makez playerz's target blockz go to teh client. Frequency iz how often (in tickz) updatez iz sended. Changing dis or turning it off mite brek other modz (rip).");
        feature(DEBUG, "Turnz on sum debug commandz an feachurz dat iz usually hidden. Commandz need a world reload to show/disappear, but will still be off when u change dis option (iz complicated ok).");

        prefix("command.exception.player_not_found", "Dat player iz not founded. :(");
        prefix("command.exception.no_players_found", "No playerz iz founded. :(");
        prefix("command.exception.debug_not_enabled", "Dis command needz debug mode, which iz not on!! Re-log or run /reload to make dis command go away, or turn on debug mode with /features.");

        prefix("credits.success_both", "Showing creditz & end poem to %s playerz!! :3");
        prefix("credits.success_credits", "Showing creditz to %s playerz!");
        prefix("credits.success_poem", "Showing end poem to %s playerz!");
        prefix("credits.screen.close", "Hold %s to maek it go away");

        prefix("feature.enable", "Feachur %s iz now ON!! yay");
        prefix("feature.disable", "Feachur %s iz now off. :(");
        prefix("feature.missing", "Idk wat feachur %s iz??");
        prefix("feature.description.missing", "Nobody wroted a descripshun. lazy.");
        prefix("feature.prefix", "Composer's Feachurz");

        prefix("toast.cleared_for_player", "Toastz for %s iz all gone now.");
        prefix("toast.cleared_all", "All toastz iz gone!! (no more toastz)");
        prefix("toast.invalid_icon_texture", "Dat icon texchur identifier iz WRONG.");
        prefix("toast.sent_simple", "Sended toast: %s\n | Icon texchur: %s\n | Background color: %s\n | Border color: %s");
        prefix("toast.sent_notify", "Sended notify toast: %s\n | Background color: %s\n | Border color: %s");
        prefix("toast.prefix", "Composer Toastz");

        prefix("overlay.prefix", "Composer Overlays (ooh shiny)");
        prefix("overlay.cleared_all", "All creditz and queued overlays iz gone now!!");
        prefix("overlay.cleared_all_for", "All creditz and queued overlays for %s iz gone now.");
        prefix("overlay.cleared_all_visible", "All creditz overlays iz gone!!");
        prefix("overlay.cleared_all_visible_for", "All creditz overlays for %s iz gone!!");
        prefix("overlay.cleared_all_queued", "All queued overlays iz gone!!");
        prefix("overlay.cleared_all_queued_for", "All queued overlays for %s iz gone!!");
        prefix("overlay.invalid_texture", "Dat texchur identifier iz WRONG.");
        prefix("overlay.sent_texture_scale_duration", "Sended texchured overlay (%s) wif scale %.2f for %d tickz.");
        prefix("overlay.sent_texture_scale_fade", "Sended texchured overlay (%s) wif scale %.2f (fade %d/%d/%d).");
        prefix("overlay.sent_texture_duration", "Sended texchured overlay (%s) for %d tickz.");
        prefix("overlay.sent_texture_fade", "Sended texchured overlay (%s) (fade %d/%d/%d).");
        prefix("overlay.sent_text_scale_duration", "Sended tekst overlay \"%s\" (%s) wif scale %.2f for %d tickz.");
        prefix("overlay.sent_text_scale_fade", "Sended tekst overlay \"%s\" (%s) wif scale %.2f (fade %d/%d/%d).");
        prefix("overlay.sent_text_duration", "Sended tekst overlay \"%s\" (%s) for %d tickz.");
        prefix("overlay.sent_text_fade", "Sended tekst overlay \"%s\" (%s) (fade %d/%d/%d).");

        prefix("registry.prefix", "Composer Utilitiez (very useful, much wow)");
        prefix("dynamic_tooltips.hidden", "Press %s to see %s (iz hidden rn)");

        prefix("tooltips.soulbound", "Dis item iz soulbound (iz yours 4evr)");
        prefix("tooltips.soulbound.not", "Dis item iz NOT soulbound");
        prefix("tooltips.soulbound.details", "binding detailz");
        prefix("tooltips.soulbound.droppable", "U can drop dis item even tho it iz soulbound");
        prefix("tooltips.soulbound.droppable.not", "U CANNOT drop dis item when it iz soulbound");
    }

    @Override
    public String prefix() {
        return "composer";
    }
}