package com.codex.composer.api.v1.util.misc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;

import java.util.HashSet;
import java.util.Set;

public class CachedLogger {
    private final Logger delegate;
    private final Set<String> cache;

    public CachedLogger(Logger delegate) {
        this.delegate = delegate;
        cache = new HashSet<>();
    }

    public void warnOnce(String message, Object... parameters) {
        if (sentAlready(message, parameters)) return;
        delegate.warn(message, parameters);
    }

    public void errorOnce(String message, Object... parameters) {
        if (sentAlready(message, parameters)) return;
        delegate.error(message, parameters);
    }

    public void infoOnce(String message, Object... parameters) {
        if (sentAlready(message, parameters)) return;
        delegate.info(message, parameters);
    }

    private boolean sentAlready(String message, Object... params) {
        String formatted = ParameterizedMessageFactory.INSTANCE.newMessage(message, params).getFormattedMessage();
        if (cache.contains(formatted)) return true;
        else cache.add(formatted);
        return false;
    }
}
