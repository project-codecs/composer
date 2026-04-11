package com.codex.composer.api.v1.util.misc;

import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementManager {
    public static void grantAdvancement(ServerPlayerEntity player, Identifier id) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        var advancement = server.getAdvancementLoader().get(id);
        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);
            if (!progress.isDone()) {
                progress.getUnobtainedCriteria().forEach(criterion -> player.getAdvancementTracker().grantCriterion(advancement, criterion));
            }
        }
    }
}
