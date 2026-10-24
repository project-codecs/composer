package com.codex.composer.internal;

import com.codex.composer.api.v1.multiblock.ingame.MultiblockController;
import com.codex.composer.api.v1.registry.ComposerPostInitialization;
import com.codex.composer.api.v1.util.misc.CachedLogger;
import com.codex.composer.internal.command.*;
import com.codex.composer.internal.data.loader.MultiblockLoader;
import com.codex.composer.internal.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.codex.composer.internal.runtime.ServerHolderImpl;
import com.codex.composer.api.v1.util.misc.AbstractPseudoRegistry;
import com.codex.composer.api.v1.util.misc.EventStacker;
import com.codex.composer.internal.networking.ScrollActionPayload;
import net.fabricmc.loader.api.FabricLoader;

//? if minecraft: >=26.2 {
/*import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
*///? } else {
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
//? }

//? if minecraft: <=1.21.6 {
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import com.codex.composer.internal.client.config.DupedBindsConfig;
//? }

//? if minecraft: >=1.20.6 {
import com.codex.composer.internal.networking.ClearOverlaysPayload;
import com.codex.composer.internal.networking.ShowCreditsPayload;
import com.codex.composer.internal.networking.ShowOverlayPayload;
//? }

public class Composer implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(Composer.class);
    public static final CachedLogger CACHED_LOGGER = new CachedLogger(LOGGER);
    public static final String MOD_ID = "composer";
    private static boolean dupedKeybindsEnabled = true;

    @Override
    public void onInitialize() {
        //? if minecraft: <=1.21.6 {
        DupedBindsConfig.initialize();

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata meta = mod.getMetadata();

            if (meta.getId().equals("enchancement")) {
                dupedKeybindsEnabled = false;
                break;
            }

            CustomValue section = meta.getCustomValue("composer-duped-keybinds");

            if (section == null || section.getType() != CustomValue.CvType.BOOLEAN) continue;
            if (section.getAsBoolean()) {
                dupedKeybindsEnabled = true;
            }
        }
        //? }

        ModTooltips.initialize();
        ModBlockEntities.initialize();
        ModArgumentTypes.initialize();
        ModStatistics.initialize();
        ModItemGroups.initialize();
        ModSounds.initialize();
        ModBlocks.initialize();
        ModItems.initialize();

        ServerTickEvents./*~ if !legacy 'WORLD' -> 'LEVEL' {*/END_WORLD_TICK/*~}*/.register(MultiblockController::processPending);
        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register(MultiblockController::onLoad);
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register(MultiblockController::onUnload);

        //? if minecraft: >=1.20.6
        ModDataComponentTypes.initialize();

        ModRegistries.initialize();
        ModOverlaySerializers.initialize();

        ScrollActionPayload.registerHandler();

        //? if minecraft: >=1.20.6 {
        ClearOverlaysPayload.registerPacket();
        ShowCreditsPayload.registerPacket();
        ShowOverlayPayload.registerPacket();
        //?}

        EventStacker.registerAll(
                CommandRegistrationCallback.EVENT,
                new OverlayCommand(),
                new RegistryCommand(),
                new CreditsCommand()
        );

        EventStacker.registerAll(
                ServerLifecycleEvents.SERVER_STARTED,
                ServerHolderImpl.INSTANCE::accept,
                AbstractPseudoRegistry::runAfterInit
        );

        //? if minecraft: <26.2 {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MultiblockLoader());
        //? } else {
        /*ResourceLoader.get(ResourceType.SERVER_DATA).registerReloadListener(identify("multiblocks"), new MultiblockLoader());
        *///? }

        FabricLoader.getInstance()
                .getEntrypointContainers("composer-post-init", ComposerPostInitialization.class)
                .forEach(e -> e.getEntrypoint().initialize());
    }

    public static boolean disableDupedBinds() {
        return !dupedKeybindsEnabled;
    }

    public static Identifier identify(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
