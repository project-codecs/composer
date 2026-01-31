package com.codex.composer.api.v1.tooltips.layout;

import com.codex.composer.api.v1.tooltips.TooltipContext;

import java.util.Locale;

/**
 * Represents required modifier buttons for a section.
 */
public enum Modifier {
    SHIFT, CTRL, ALT;

    /**
     * Matches this modifier against the context.
     */
    public boolean matches(TooltipContext ctx) {
        return switch (this) {
            case SHIFT -> ctx.shiftHeld;
            case CTRL -> ctx.ctrlHeld;
            case ALT -> ctx.altHeld;
        };
    }

    /**
     * Matches a combination of modifiers.
     */
    public static boolean matchesAll(TooltipContext ctx, Modifier... mods) {
        for (Modifier m : mods) {
            if (!m.matches(ctx)) return false;
        }
        return true;
    }

    /**
     * Returns a human-readable string for the modifier combination.
     */
    public static String toString(Modifier... mods) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < mods.length; i++) {
            if (i > 0) sb.append(" + ");
            sb.append(mods[i].name().toLowerCase(Locale.ROOT));
        }
        sb.append(")");
        return sb.toString();
    }
}