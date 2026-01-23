package com.codex.composer.api.v1.util.misc;

@FunctionalInterface
@Deprecated(since = "3.0.5")
public interface Provider<P> {
    P provide();
}
