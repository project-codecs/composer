package com.codex.composer.api.v1.tooltips;

/**
 * Functional interface for providing section content based on context.
 */
@FunctionalInterface
public interface ContentProvider {
    String get(TooltipContext context);
}