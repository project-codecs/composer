package com.codex.composer.internal.cca;

import com.codex.composer.internal.Composer;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

//? legacy {
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
//? } else {
/*import org.ladysnake.cca.api.v8.level.LevelComponentInitializer;
import org.ladysnake.cca.api.v8.level.LevelComponentFactoryRegistry;
*///? }

public class ModCardinalComponents implements /*? if legacy {*/WorldComponentInitializer/*? } else {*//*LevelComponentInitializer*//*? }*/ {
    public static final ComponentKey<MultiblocksComponent> MULTIBLOCKS =
            ComponentRegistry.getOrCreate(Composer.identify("multiblocks"), MultiblocksComponent.class);

    @Override
    public void /*? if legacy {*/registerWorldComponentFactories/*? } else {*//*registerLevelComponentFactories*//*? }*/(/*? if legacy {*/WorldComponentFactoryRegistry/*? } else {*//*LevelComponentFactoryRegistry*//*? }*/  registry) {
        registry.register(MULTIBLOCKS, MultiblocksComponent::new);
    }
}
