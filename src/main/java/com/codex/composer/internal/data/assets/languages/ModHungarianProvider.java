package com.codex.composer.internal.data.assets.languages;

import com.codex.composer.api.v1.datagen.lang.SemiLanguageProvider;

import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import com.codex.composer.internal.client.config.ComposerClientConfig;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModHungarianProvider extends SemiLanguageProvider {
    @Override
    public void generate(CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        block(ModBlocks.PLUSH, "LilBroCodes plüss");
        stat(ModStatistics.PLUSH_BOOP, "Megbökött LilBroCodes plüssök");
        group(ModItemGroups.COMPOSER, "A Composer vicces kis cuccai");

        enumTranslatable(
                ComposerClientConfig.BindsMode.class,
                "Nincs",
                "Vanilla és Composert használó modok",
                "Összes"
        );

        sound(ModSounds.LILBRO_SQUISH, "Plüss megbökve");

        prefix("composer.command");
        add("exception.player_not_found", "A játékos nem található.");
        add("exception.no_players_found", "Nem találhatók játékosok.");
        add("exception.debug_not_enabled", "Ehhez a parancshoz a fejlesztői mód szükséges, amely nincs bekapcsolva! Ezt be tudod kapcsolni a Composer szerverbeállításaiban a Mod Menün keresztül, vagy a /configure paranccsal.");

        prefix("composer.credits");
        add("success_both", "Stáblista és end-vers megjelenítése %s játékosnak.");
        add("success_credits", "Stáblista megjelenítése %s játékosnak.");
        add("success_poem", "End-vers megjelenítése %s játékosnak.");
        add("screen.close", "Tartsd lenyomva: %s a bezáráshoz");

        prefix("composer.feature");
        add("enable", "%s funkció engedélyezve");
        add("disable", "%s funkció letiltva");
        add("missing", "Ismeretlen funkció: %s");
        add("description.missing", "Nem érkezett leírás.");
        add("prefix", "Composer funkciók");

        prefix("composer.overlay");
        add("prefix", "Composer átfedések");
        add("cleared_all", "Az összes látható és sorban álló átfedés sikeresen törölve.");
        add("cleared_all_for", "Az összes látható és sorban álló átfedés %s számára sikeresen törölve.");
        add("cleared_all_visible", "Az összes látható átfedés sikeresen törölve.");
        add("cleared_all_visible_for", "Az összes látható átfedés %s számára sikeresen törölve.");
        add("cleared_all_queued", "Az összes sorban álló átfedés sikeresen törölve.");
        add("cleared_all_queued_for", "Az összes sorban álló átfedés %s számára sikeresen törölve.");
        add("invalid_texture", "Érvénytelen textúra-azonosító.");
        add("sent_texture_scale_duration", "Textúrált átfedés elküldve (%s), méretarány: %.2f, időtartam: %d tick.");
        add("sent_texture_scale_fade", "Textúrált átfedés elküldve (%s), méretarány: %.2f (elhalványulás: %d/%d/%d).");
        add("sent_texture_duration", "Textúrált átfedés elküldve (%s), időtartam: %d tick.");
        add("sent_texture_fade", "Textúrált átfedés elküldve (%s) (elhalványulás: %d/%d/%d).");
        add("sent_text_scale_duration", "Szöveges átfedés elküldve: \"%s\" (%s), méretarány: %.2f, időtartam: %d tick.");
        add("sent_text_scale_fade", "Szöveges átfedés elküldve: \"%s\" (%s), méretarány: %.2f (elhalványulás: %d/%d/%d).");
        add("sent_text_duration", "Szöveges átfedés elküldve: \"%s\" (%s), időtartam: %d tick.");
        add("sent_text_fade", "Szöveges átfedés elküldve: \"%s\" (%s) (elhalványulás: %d/%d/%d).");

        prefix("composer");
        add("registry.prefix", "Composer segédeszközök");
        add("dynamic_tooltips.hidden", "Nyomd meg a(z) %s-t hogy lásd %s");
        add("dynamic_tooltips.details", "a részleteket");

        prefix("composer.tooltips");
        add("soulbound", "Ez a tárgy lélekhez kötött");
        add("soulbound.not", "Ez a tárgy nincs lélekhez kötve");
        add("soulbound.details", "e tárgy kötési részleteit");
        add("soulbound.droppable", "Ez a tárgy lélekhez kötötten is dobható el");
        add("soulbound.droppable.not", "Ez a tárgy lélekhez kötötten nem dobható el");
    }
}