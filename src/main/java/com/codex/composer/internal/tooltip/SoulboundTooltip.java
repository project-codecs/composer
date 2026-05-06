package com.codex.composer.internal.tooltip;

import com.codex.composer.api.v1.item.settings.component.SoulboundComponent;
import com.codex.composer.api.v1.tooltips.TooltipContext;
import com.codex.composer.api.v1.tooltips.layout.Modifier;
import com.codex.composer.api.v1.tooltips.layout.Section;
import com.codex.composer.api.v1.tooltips.impl.SimpleDynamicTooltip;
import com.codex.composer.api.v1.tooltips.layout.SectionBuilder;
import com.codex.composer.api.v1.util.misc.LanguageUtils;
import net.minecraft.text.Text;

import java.util.List;

public class SoulboundTooltip extends SimpleDynamicTooltip {
    @Override
    public Section root() {
        Section droppable = SectionBuilder.create()
                .title("")
                .keyCombo(ctx -> Modifier.ALT)
                .content(ctx -> List.of(Text.translatable(LanguageUtils.negate("composer.tooltips.soulbound.droppable", SoulboundComponent.canDropSoulbound(ctx.stack)))))
                .build();

        return SectionBuilder.create()
                .title("")
                .details(Text.translatable("composer.tooltips.soulbound.details"))
                .keyCombo(ctx -> SoulboundComponent.isSoulbound(ctx.stack) ? null : Modifier.SHIFT)
                .content(ctx -> List.of(Text.translatable(LanguageUtils.negate("composer.tooltips.soulbound", SoulboundComponent.isSoulbound(ctx.stack)))))
                .children().push(droppable).end()
                .build();
    }

    @Override
    public boolean isRelevant(TooltipContext context) {
        return SoulboundComponent.isSoulbound(context.stack);
    }

    @Override
    public Location where() {
        return Location.AFTER_NAME;
    }
}
