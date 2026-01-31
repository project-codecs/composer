package com.codex.composer.api.v1.tooltips;

import com.codex.composer.api.v1.tooltips.layout.DynamicTooltip;
import com.codex.composer.api.v1.util.misc.AbstractPseudoRegistry;
import com.codex.composer.internal.Composer;

public class DynamicTooltipRegistry extends AbstractPseudoRegistry.Impl<DynamicTooltip> {
    private static DynamicTooltipRegistry INSTANCE;

    private DynamicTooltipRegistry() {

    }

    @Override
    protected void bootstrap() {
        identify(Composer.identify("dynamic_tooltips"), this);
    }

    public static DynamicTooltipRegistry getInstance() {
        if (INSTANCE == null) INSTANCE = new DynamicTooltipRegistry();
        return INSTANCE;
    }
}
