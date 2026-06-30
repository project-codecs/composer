package com.codex.composer.internal.data.assets.languages;

import com.codex.composer.api.v1.datagen.lang.SemiLanguageProvider;
import com.codex.composer.internal.client.config.ComposerClientConfig;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLolcatProvider extends SemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "LilBro Plushie (SO SQUISHY)");
        stat(ModStatistics.PLUSH_BOOP, "LilBro Plushiez Booped (hehehe)");
        group(ModItemGroups.COMPOSER, "Composer's Silly Lil Addishuns (iz funni)");

        enumTranslatable(
                ComposerClientConfig.BindsMode.class,
                "Nun",
                "Vanilla & Modz Dat Use Composer",
                "ALL TEH THINGZ"
        );

        sound(ModSounds.LILBRO_SQUISH, "Plushie Booped!! :3");

        prefix("composer.command");
        add("exception.player_not_found", "Dat player iz not founded. :(");
        add("exception.no_players_found", "No playerz iz founded. :(");
        add("exception.debug_not_enabled", "Dis command needz developr mode, which iz not on!! U canz enablez it in Composer'servr config from Mod Menu or uzing /configure!!");

        prefix("composer.credits");
        add("success_both", "Showing creditz & end poem to %s playerz!! :3");
        add("success_credits", "Showing creditz to %s playerz!");
        add("success_poem", "Showing end poem to %s playerz!");
        add("screen.close", "Hold %s to maek it go away");

        prefix("composer.feature");
        add("enable", "Feachur %s iz now ON!! yay");
        add("disable", "Feachur %s iz now off. :(");
        add("missing", "Idk wat feachur %s iz??");
        add("description.missing", "Nobody wroted a descripshun. lazy.");
        add("prefix", "Composer's Feachurz");

        prefix("composer.overlay");
        add("prefix", "Composer Overlays (ooh shiny)");
        add("cleared_all", "All creditz and queued overlays iz gone now!!");
        add("cleared_all_for", "All creditz and queued overlays for %s iz gone now.");
        add("cleared_all_visible", "All creditz overlays iz gone!!");
        add("cleared_all_visible_for", "All creditz overlays for %s iz gone!!");
        add("cleared_all_queued", "All queued overlays iz gone!!");
        add("cleared_all_queued_for", "All queued overlays for %s iz gone!!");
        add("invalid_texture", "Dat texchur identifier iz WRONG.");
        add("sent_texture_scale_duration", "Sended texchured overlay (%s) wif scale %.2f for %d tickz.");
        add("sent_texture_scale_fade", "Sended texchured overlay (%s) wif scale %.2f (fade %d/%d/%d).");
        add("sent_texture_duration", "Sended texchured overlay (%s) for %d tickz.");
        add("sent_texture_fade", "Sended texchured overlay (%s) (fade %d/%d/%d).");
        add("sent_text_scale_duration", "Sended tekst overlay \"%s\" (%s) wif scale %.2f for %d tickz.");
        add("sent_text_scale_fade", "Sended tekst overlay \"%s\" (%s) wif scale %.2f (fade %d/%d/%d).");
        add("sent_text_duration", "Sended tekst overlay \"%s\" (%s) for %d tickz.");
        add("sent_text_fade", "Sended tekst overlay \"%s\" (%s) (fade %d/%d/%d).");

        prefix("composer");
        add("registry.prefix", "Composer Utilitiez (very useful, much wow)");
        add("dynamic_tooltips.hidden", "Press %s to see %s (iz hidden rn)");
        add("dynamic_tooltips.details", "da detailz");

        prefix("composer.tooltips");
        add("soulbound", "Dis item iz soulbound (iz yours 4evr)");
        add("soulbound.not", "Dis item iz NOT soulbound");
        add("soulbound.details", "binding detailz");
        add("soulbound.droppable", "U can drop dis item even tho it iz soulbound");
        add("soulbound.droppable.not", "U CANNOT drop dis item when it iz soulbound");
    }
}