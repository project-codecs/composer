package com.codex.composer.internal;

import com.codex.composer.internal.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.codex.composer.api.v1.easytags.impl.DefaultSerializers;
import com.codex.composer.api.v1.events.composite.ComposerCompositeEvents;
import com.codex.composer.api.v1.feature.ComposerFeatures;
import com.codex.composer.internal.runtime.ServerHolderImpl;
import com.codex.composer.api.v1.util.misc.AbstractPseudoRegistry;
import com.codex.composer.api.v1.util.misc.EventStacker;
import com.codex.composer.internal.client.config.ComposerConfig;
import com.codex.composer.internal.command.FeatureCommand;
import com.codex.composer.internal.command.OverlayCommand;
import com.codex.composer.internal.command.RegistryCommand;
import com.codex.composer.internal.command.ToastCommand;
import com.codex.composer.internal.data.loader.FeatureStateLoader;
import com.codex.composer.internal.data.loader.SimpleItemFixerLoader;
import com.codex.composer.api.v1.util.exception.InvalidMetadataException;
import com.codex.composer.internal.networking.ScrollActionPayload;
import com.codex.composer.internal.networking.TargetBlockPayload;
import com.codex.composer.internal.networking.TargetEntityPayload;

public class Composer implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(Composer.class);
    public static final String MOD_ID = "composer";
    private static boolean dupedKeybindsEnabled = false;

    @Override
    public void onInitialize() {
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

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata meta = mod.getMetadata();
            String modId = meta.getId();
            CustomValue section = meta.getCustomValue("composer-features");

            if (section != null && section.getType() == CustomValue.CvType.ARRAY)
                throw new InvalidMetadataException("Mod " + modId + " is using the deprecated feature registration system. Update the mod, contact the developer or downgrade composer if possible.");
        }

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

        ComposerConfig.initialize();
        ModRegistries.initialize();
        ModOverlaySerializers.initialize();
        ModToastSerializers.initialize();
        DefaultSerializers.initialize();

        TargetEntityPayload.registerHandler();
        TargetBlockPayload.registerHandler();
        ScrollActionPayload.registerHandler();

        EventStacker.registerAll(
                CommandRegistrationCallback.EVENT,
                new FeatureCommand(),
                new ToastCommand(),
                new OverlayCommand(),
                new RegistryCommand()
        );

        EventStacker.registerAll(
                ServerLifecycleEvents.SERVER_STARTED,
                ServerHolderImpl.INSTANCE::accept,
                AbstractPseudoRegistry::runAfterInit,
                ComposerFeatures.getInstance()::afterInitialization
        );

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleItemFixerLoader());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new FeatureStateLoader());
    }

    public static boolean disableDupedBinds() {
        return !dupedKeybindsEnabled;
    }

    public static Identifier identify(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
