package com.codex.composer.api.v1.util.controls;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import com.codex.composer.internal.client.duped_binds.BindTracker;
import net.minecraft.client.world.ClientWorld;

import java.util.function.BiConsumer;

public class BindManager {
    public static void addDuplicateAllowedKeybind(KeyBinding bind) {
        BindTracker.MC_CM_BINDS.add(bind);
    }

    public static void whileHeld(KeyBinding bind, At when, BiConsumer<MinecraftClient, ClientWorld> method) {
        register(bind, when, true, true, true, method);
    }

    public static void whileHeld(KeyBinding bind, At when, boolean checkPlayer, boolean checkWorld, BiConsumer<MinecraftClient, ClientWorld> method) {
        register(bind, when, checkPlayer, checkWorld, true, method);
    }

    public static void whenPressed(KeyBinding bind, At when, BiConsumer<MinecraftClient, ClientWorld> method) {
        register(bind, when, true, true, false, method);
    }

    public static void whenPressed(KeyBinding bind, At when, boolean checkPlayer, boolean checkWorld, BiConsumer<MinecraftClient, ClientWorld> method) {
        register(bind, when, checkPlayer, checkWorld, false, method);
    }

    public static void register(KeyBinding bind, At when, boolean checkPlayer, boolean checkWorld, boolean held, BiConsumer<MinecraftClient, ClientWorld> method) {
        switch (when) {
            case START_CLIENT -> ClientTickEvents.START_CLIENT_TICK.register(client ->
                    handle(client, client.world, bind, checkPlayer, checkWorld, held, method)
            );

            case END_CLIENT -> ClientTickEvents.END_CLIENT_TICK.register(client ->
                    handle(client, client.world, bind, checkPlayer, checkWorld, held, method)
            );

            case START_WORLD -> ClientTickEvents./*? if legacy {*/START_WORLD_TICK/*? } else {*//*START_LEVEL_TICK*//*? }*/.register(world ->
                    handle(MinecraftClient.getInstance(), world, bind, checkPlayer, checkWorld, held, method)
            );

            case END_WORLD -> ClientTickEvents./*? if legacy {*/START_WORLD_TICK/*? } else {*//*END_LEVEL_TICK*//*? }*/.register(world ->
                    handle(MinecraftClient.getInstance(), world, bind, checkPlayer, checkWorld, held, method)
            );
        }
    }

    private static void handle(MinecraftClient client, ClientWorld world, KeyBinding bind, boolean checkPlayer, boolean checkWorld, boolean held, BiConsumer<MinecraftClient, ClientWorld> method) {
        if ((held ? bind.isPressed() : bind.wasPressed()) && (!checkWorld || world != null) && (!checkPlayer || client.player != null)) {
            method.accept(client, world);
        }
    }

    public enum At {
        START_CLIENT,
        END_CLIENT,
        START_WORLD,
        END_WORLD
    }
}
