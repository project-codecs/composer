package com.codex.composer.api.v1.tooltips.impl;

import net.minecraft.text.Text;
import com.codex.composer.api.v1.tooltips.layout.DynamicTooltip;
import com.codex.composer.api.v1.tooltips.layout.Section;
import com.codex.composer.api.v1.tooltips.TooltipContext;

//? if minecraft: <=1.21.4
import java.util.List;
//? if minecraft: >=1.21.5
//import java.util.function.Consumer;

public abstract class SimpleDynamicTooltip implements DynamicTooltip {
    private final Section tooltip = root();

    public SimpleDynamicTooltip() {

    }

    @Override
    //? if minecraft: <=1.21.4
    public void appendTooltip(TooltipContext context, List<Text> out) {
    //? if minecraft: >=1.21.5
    //public void appendTooltip(TooltipContext context, Consumer<Text> out) {
        tooltip.append(context, out);
    }

    public abstract Section root();
}
