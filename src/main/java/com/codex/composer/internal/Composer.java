package com.codex.composer.internal;

import com.codex.composer.api.v1.util.misc.CachedLogger;
import com.codex.composer.internal.cca.ModCardinalComponents;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import com.codex.composer.internal.command.*;
import com.codex.composer.internal.config.ComposerServerConfig;
import com.codex.composer.internal.data.loader.MultiblockLoader;
import com.codex.composer.internal.multiblock.MultiblockUpdateHandler;
import com.codex.composer.internal.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.codex.composer.api.v1.events.composite.ComposerCompositeEvents;
import com.codex.composer.api.v1.feature.ComposerFeatures;
import com.codex.composer.internal.runtime.ServerHolderImpl;
import com.codex.composer.api.v1.util.misc.AbstractPseudoRegistry;
import com.codex.composer.api.v1.util.misc.EventStacker;
import com.codex.composer.internal.client.config.ComposerClientConfig;
import com.codex.composer.internal.data.loader.FeatureStateLoader;
import com.codex.composer.internal.networking.ScrollActionPayload;
import com.codex.composer.internal.networking.TargetBlockPayload;
import com.codex.composer.internal.networking.TargetEntityPayload;

//? if minecraft: <=1.21.6 {
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
//? }

public class Composer implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(Composer.class);
    public static final CachedLogger CACHED_LOGGER = new CachedLogger(LOGGER);
    public static final String MOD_ID = "composer";
    private static boolean dupedKeybindsEnabled = false;

    @Override
    public void onInitialize() {
        //? if minecraft: <=1.21.6 {
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

        ComposerServerConfig.initialize();
        ComposerCompositeEvents.initialize();
        ModDynamicTooltips.initialize();
        ModBlockEntities.initialize();
        ModArgumentTypes.initialize();
        ModStatistics.initialize();
        ModItemGroups.initialize();
        ModFeatures.initialize();
        ModSounds.initialize();
        ModBlocks.initialize();
        ModItems.initialize();

        //? if minecraft: >=1.20.6
        ModDataComponentTypes.initialize();

        ComposerClientConfig.initialize();
        ModRegistries.initialize();
        ModOverlaySerializers.initialize();

        TargetEntityPayload.registerHandler();
        TargetBlockPayload.registerHandler();
        ScrollActionPayload.registerHandler();

        EventStacker.registerAll(
                CommandRegistrationCallback.EVENT,
                new FeatureCommand(),
                new OverlayCommand(),
                new RegistryCommand(),
                new CreditsCommand()
        );

        EventStacker.registerAll(
                ServerLifecycleEvents.SERVER_STARTED,
                ServerHolderImpl.INSTANCE::accept,
                AbstractPseudoRegistry::runAfterInit,
                ComposerFeatures.getInstance()::afterInitialization
        );

        ServerLifecycleEvents.SERVER_STARTED.register(server -> server.getWorlds().forEach(world -> {
                MultiblocksComponent component = ModCardinalComponents.MULTIBLOCKS.get(world);
                if (component.multiblocks.isEmpty()) return;

                MultiblockUpdateHandler.runUpdates(world, component, null);
            })
        );

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MultiblockLoader());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new FeatureStateLoader());
    }

    public static boolean disableDupedBinds() {
        return !dupedKeybindsEnabled;
    }

    public static Identifier identify(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
