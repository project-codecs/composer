package com.codex.composer.internal.registry;

import com.codex.composer.internal.tooltip.SoulboundTooltip;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class ModTooltips {
    public static void initialize() {
        ItemTooltipCallback.EVENT.register(SoulboundTooltip.INST);
    }
}
