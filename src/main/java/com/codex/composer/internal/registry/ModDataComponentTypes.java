package com.codex.composer.internal.registry;


//? if minecraft: >= 1.21 {
import net.minecraft.component.ComponentType;
//? } else if minecraft: >=1.20.6 {
/*import net.minecraft.component.DataComponentType;
*///? }

//? if minecraft: >=1.20.6 {
import com.codex.composer.api.v1.registry.lazy.DeferredDataComponentTypeRegistry;
import com.codex.composer.internal.Composer;

public class ModDataComponentTypes {
    private static final DeferredDataComponentTypeRegistry REGISTRY = new DeferredDataComponentTypeRegistry(Composer.MOD_ID);

    public static final /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/<Integer> STEPS = REGISTRY.register(
            "steps",
            DeferredDataComponentTypeRegistry.TypePrefab.NON_NEGATIVE_INT
    );

    public static final /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/<Boolean> SOULBOUND = REGISTRY.register(
            "soulbound",
            DeferredDataComponentTypeRegistry.TypePrefab.BOOLEAN
    );

    public static final /*? minecraft: >=1.21 {*/ComponentType/*? } else {*//*DataComponentType*//*? }*/<Boolean> SOULBOUND_CAN_DROP = REGISTRY.register(
            "soulbound_can_drop",
            DeferredDataComponentTypeRegistry.TypePrefab.BOOLEAN
    );

    public static void initialize() {

    }
}
//? }
