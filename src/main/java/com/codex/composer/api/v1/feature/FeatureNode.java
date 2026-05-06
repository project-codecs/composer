package com.codex.composer.api.v1.feature;

import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.data.GsonSerializer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FeatureNode {
    private final Identifier id;
    private final FeatureNode parent;
    private final Map<String, FeatureNode> children = new LinkedHashMap<>();
    public final Map<String, ConfigDefault<?>> configDefaults = new LinkedHashMap<>();
    private Boolean defaultEnabled = null;

    public FeatureNode(Identifier id, FeatureNode parent) {
        this.id = id;
        this.parent = parent;
    }

    public Identifier id() { return id; }
    public FeatureNode parent() { return parent; }
    public Collection<FeatureNode> children() { return children.values(); }

    public FeatureNode child(String name, Identifier idForChild) {
        return children.computeIfAbsent(name, k -> new FeatureNode(idForChild, this));
    }

    public Optional<FeatureNode> getChild(String name) {
        return Optional.ofNullable(children.get(name));
    }

    public void setDefaultEnabled(Boolean def) { this.defaultEnabled = def; }
    public Boolean defaultEnabled() { return defaultEnabled; }

    public void putConfigDefault(String key, GsonSerializer<?> serializer, Object defaultValue) {
        configDefaults.put(key, new ConfigDefault<>(serializer, defaultValue));
    }

    public Optional<ConfigDefault<?>> findConfigDefaultLocal(String key) {
        return Optional.ofNullable(configDefaults.get(key));
    }

    public Optional<ConfigDefault<?>> findConfigDefaultInherited(String key) {
        FeatureNode cur = this;
        while (cur != null) {
            ConfigDefault<?> cd = cur.configDefaults.get(key);
            if (cd != null) return Optional.of(cd);
            cur = cur.parent;
        }
        return Optional.empty();
    }

    public static final class ConfigDefault<T> {
        public final GsonSerializer<T> serializer;
        public final Object defaultValue;

        public ConfigDefault(GsonSerializer<T> serializer, Object defaultValue) {
            this.serializer = serializer;
            this.defaultValue = defaultValue;
        }
    }
}
