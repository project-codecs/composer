package com.codex.composer.api.v1.tooltips.impl;

import net.minecraft.text.Text;
import com.codex.composer.api.v1.tooltips.layout.DynamicTooltip;
import com.codex.composer.api.v1.tooltips.layout.Section;
import com.codex.composer.api.v1.tooltips.TooltipContext;

import java.util.List;

public abstract class SimpleDynamicTooltip implements DynamicTooltip {
    private final Section tooltip = root();

    public SimpleDynamicTooltip() {

    }

    @Override
    public void appendTooltip(TooltipContext context, List<Text> out) {
        tooltip.append(context, out);
    }

    public abstract Section root();
}
