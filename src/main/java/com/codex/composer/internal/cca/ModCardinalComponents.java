package com.codex.composer.internal.cca;

import com.codex.composer.internal.Composer;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import com.codex.composer.internal.cca.entity.TargetedBlockComponent;
import com.codex.composer.internal.cca.entity.TargetedEntityComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

//? legacy {
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
//? } else {
/*import org.ladysnake.cca.api.v8.level.LevelComponentInitializer;
import org.ladysnake.cca.api.v8.level.LevelComponentFactoryRegistry;
*///? }

public class ModCardinalComponents implements EntityComponentInitializer, /*? if legacy {*/WorldComponentInitializer/*? } else {*//*LevelComponentInitializer*//*? }*/ {
    public static final ComponentKey<TargetedBlockComponent> TARGETED_BLOCK =
            ComponentRegistry.getOrCreate(Composer.identify("targeted_block"), TargetedBlockComponent.class);
    public static final ComponentKey<TargetedEntityComponent> TARGETED_ENTITY =
            ComponentRegistry.getOrCreate(Composer.identify("targeted_entity"), TargetedEntityComponent.class);

    public static final ComponentKey<MultiblocksComponent> MULTIBLOCKS =
            ComponentRegistry.getOrCreate(Composer.identify("multiblocks"), MultiblocksComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TARGETED_ENTITY, TargetedEntityComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(TARGETED_BLOCK, TargetedBlockComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }

    @Override
    public void /*? if legacy {*/registerWorldComponentFactories/*? } else {*//*registerLevelComponentFactories*//*? }*/(/*? if legacy {*/WorldComponentFactoryRegistry/*? } else {*//*LevelComponentFactoryRegistry*//*? }*/  registry) {
        registry.register(MULTIBLOCKS, MultiblocksComponent::new);
    }
}
