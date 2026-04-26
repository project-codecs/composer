package com.codex.composer.internal.data.assets.languages;

import com.codex.composer.api.v1.datagen.lang.ComposerSemiLanguageProvider;
import com.codex.composer.internal.client.config.ComposerClientConfig;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItemGroups;
import com.codex.composer.internal.registry.ModSounds;
import com.codex.composer.internal.registry.ModStatistics;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static com.codex.composer.internal.registry.ModFeatures.TargetSynchronization;

public class ModHungarianProvider extends ComposerSemiLanguageProvider {
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

        feature(TargetSynchronization.ENTITY, "Szinkronizálja a játékosok célentitásait a klienssel. A frekvencia azt szabályozza, milyen sűrűn (tick-enként) kerülnek elküldésre a frissítések. Ennek megváltoztatása vagy letiltása más modokat elronthat.");
        feature(TargetSynchronization.BLOCK, "Szinkronizálja a játékosok célblokkjait a klienssel. A frekvencia azt szabályozza, milyen sűrűn (tick-enként) kerülnek elküldésre a frissítések. Ennek megváltoztatása vagy letiltása más modokat elronthat.");

        prefix("command.exception.player_not_found", "A játékos nem található.");
        prefix("command.exception.no_players_found", "Nem találhatók játékosok.");
        prefix("command.exception.debug_not_enabled", "Ehhez a parancshoz a fejlesztői mód szükséges, amely nincs bekapcsolva! Ezt be tudod kapcsolni a Composer szerverbeállításaiban a Mod Menün keresztül, vagy a /configure paranccsal.");

        prefix("credits.success_both", "Stáblista és end-vers megjelenítése %s játékosnak.");
        prefix("credits.success_credits", "Stáblista megjelenítése %s játékosnak.");
        prefix("credits.success_poem", "End-vers megjelenítése %s játékosnak.");
        prefix("credits.screen.close", "Tartsd lenyomva: %s a bezáráshoz");

        prefix("feature.enable", "%s funkció engedélyezve");
        prefix("feature.disable", "%s funkció letiltva");
        prefix("feature.missing", "Ismeretlen funkció: %s");
        prefix("feature.description.missing", "Nem érkezett leírás.");
        prefix("feature.prefix", "Composer funkciók");

        prefix("overlay.prefix", "Composer átfedések");
        prefix("overlay.cleared_all", "Az összes látható és sorban álló átfedés sikeresen törölve.");
        prefix("overlay.cleared_all_for", "Az összes látható és sorban álló átfedés %s számára sikeresen törölve.");
        prefix("overlay.cleared_all_visible", "Az összes látható átfedés sikeresen törölve.");
        prefix("overlay.cleared_all_visible_for", "Az összes látható átfedés %s számára sikeresen törölve.");
        prefix("overlay.cleared_all_queued", "Az összes sorban álló átfedés sikeresen törölve.");
        prefix("overlay.cleared_all_queued_for", "Az összes sorban álló átfedés %s számára sikeresen törölve.");
        prefix("overlay.invalid_texture", "Érvénytelen textúra-azonosító.");
        prefix("overlay.sent_texture_scale_duration", "Textúrált átfedés elküldve (%s), méretarány: %.2f, időtartam: %d tick.");
        prefix("overlay.sent_texture_scale_fade", "Textúrált átfedés elküldve (%s), méretarány: %.2f (elhalványulás: %d/%d/%d).");
        prefix("overlay.sent_texture_duration", "Textúrált átfedés elküldve (%s), időtartam: %d tick.");
        prefix("overlay.sent_texture_fade", "Textúrált átfedés elküldve (%s) (elhalványulás: %d/%d/%d).");
        prefix("overlay.sent_text_scale_duration", "Szöveges átfedés elküldve: \"%s\" (%s), méretarány: %.2f, időtartam: %d tick.");
        prefix("overlay.sent_text_scale_fade", "Szöveges átfedés elküldve: \"%s\" (%s), méretarány: %.2f (elhalványulás: %d/%d/%d).");
        prefix("overlay.sent_text_duration", "Szöveges átfedés elküldve: \"%s\" (%s), időtartam: %d tick.");
        prefix("overlay.sent_text_fade", "Szöveges átfedés elküldve: \"%s\" (%s) (elhalványulás: %d/%d/%d).");

        prefix("registry.prefix", "Composer segédeszközök");
        prefix("dynamic_tooltips.hidden", "Nyomd meg: %s hogy látsd %s");

        prefix("tooltips.soulbound", "Ez a tárgy lélekhez kötött");
        prefix("tooltips.soulbound.not", "Ez a tárgy nincs lélekhez kötve");
        prefix("tooltips.soulbound.details", "e tárgy kötési részleteit");
        prefix("tooltips.soulbound.droppable", "Ez a tárgy lélekhez kötötten is dobható el");
        prefix("tooltips.soulbound.droppable.not", "Ez a tárgy lélekhez kötötten nem dobható el");
    }

    @Override
    public String prefix() {
        return "composer";
    }
}