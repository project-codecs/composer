package com.codex.composer.internal.cca;


import com.codex.composer.internal.Composer;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import com.codex.composer.internal.cca.entity.TargetedBlockComponent;
import com.codex.composer.internal.cca.entity.TargetedEntityComponent;

//? if minecraft: <=1.20.4 {
/*import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
 *///? } else {
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
//?}

public class ModCardinalComponents implements EntityComponentInitializer, WorldComponentInitializer {
    public static final ComponentKey<TargetedBlockComponent> TARGETED_BLOCK = ComponentRegistry
            .getOrCreate(Composer.identify("targeted_block"), TargetedBlockComponent.class);
    public static final ComponentKey<TargetedEntityComponent> TARGETED_ENTITY = ComponentRegistry
            .getOrCreate(Composer.identify("targeted_entity"), TargetedEntityComponent.class);

    public static final ComponentKey<MultiblocksComponent> MULTIBLOCKS = ComponentRegistry
            .getOrCreate(Composer.identify("multiblocks"), MultiblocksComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TARGETED_ENTITY, TargetedEntityComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(TARGETED_BLOCK, TargetedBlockComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(MULTIBLOCKS, MultiblocksComponent::new);
    }
}
