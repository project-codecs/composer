package com.codex.composer.api.v1.feature;

import com.codex.composer.api.v1.feature.config.BooleanConfigSerializer;
import com.codex.composer.api.v1.feature.config.DoubleConfigSerializer;
import com.codex.composer.api.v1.feature.config.IntConfigSerializer;
import com.codex.composer.api.v1.feature.config.StringConfigSerializer;
import com.codex.composer.api.v1.data.GsonSerializer;

@SuppressWarnings("UnusedReturnValue")
public class FeatureBuilder {
    private final FeatureNode node;

    public FeatureBuilder(FeatureNode node) { this.node = node; }

    public FeatureBuilder defaultEnabled(boolean def) {
        node.setDefaultEnabled(def);
        return this;
    }

    public FeatureBuilder e() {
        return defaultEnabled(true);
    }

    final FeatureBuilder d() {
        return defaultEnabled(false);
    }

    public <T> FeatureBuilder config(String key, T defaultValue, GsonSerializer<T> serializer) {
        node.putConfigDefault(key, serializer, defaultValue);
        return this;
    }

    public FeatureBuilder configDouble(String key, double defaultValue) {
        node.putConfigDefault(key, DoubleConfigSerializer.INSTANCE, defaultValue);
        return this;
    }

    public FeatureBuilder configInt(String key, int defaultValue) {
        node.putConfigDefault(key, IntConfigSerializer.INSTANCE, defaultValue);
        return this;
    }

    public FeatureBuilder configBoolean(String key, boolean defaultValue) {
        node.putConfigDefault(key, BooleanConfigSerializer.INSTANCE, defaultValue);
        return this;
    }

    public FeatureBuilder configString(String key, String defaultValue) {
        node.putConfigDefault(key, StringConfigSerializer.INSTANCE, defaultValue);
        return this;
    }

    FeatureNode node() { return node; }
}
