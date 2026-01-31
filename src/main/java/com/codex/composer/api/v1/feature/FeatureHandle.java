package com.codex.composer.api.v1.feature;

import com.codex.composer.api.v1.runtime.ServerHolder;
import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.feature.config.BooleanConfigSerializer;
import com.codex.composer.api.v1.feature.config.DoubleConfigSerializer;
import com.codex.composer.api.v1.feature.config.IntConfigSerializer;
import com.codex.composer.api.v1.feature.config.StringConfigSerializer;
import com.codex.composer.api.v1.feature.state.FeatureState;
import com.codex.composer.api.v1.nbt.GsonSerializer;
import com.codex.composer.api.v1.util.misc.Translatable;

import java.util.Optional;

public class FeatureHandle implements Translatable {
    private final FeatureNode node;

    public FeatureHandle(FeatureNode node) { this.node = node; }

    public Identifier id() { return node.id(); }

    @Override
    public String getTranslationKey() {
        return "feature.description." + id().toTranslationKey().replace('/', '.');
    }

    public Translatable getTranslatable() {
        return this;
    }

    public boolean enabled() {
        for (FeatureHandle group : ComposerFeatures.getInstance().getGroupsContaining(this)) {
            if (!group.enabled()) return false;
        }

        if (ServerHolder.get().has()) {
            FeatureState state = ServerHolder.get().features();
            Boolean v = state.getEnabled(id().toString());
            if (v != null) return v;
        }

        FeatureNode cur = node;
        while (cur != null) {
            if (cur.defaultEnabled() != null) return cur.defaultEnabled();
            cur = cur.parent();
        }

        return true;
    }

    private Optional<FeatureNode.ConfigDefault<?>> findInheritedDefault(String key) {
        return node.findConfigDefaultInherited(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfig(String key, GsonSerializer<T> serializer, Class<T> cls) {
        if (ServerHolder.get().has()) {
            FeatureState state = ServerHolder.get().features();
            JsonElement val = state.getConfigValue(id().toString(), key, null);
            if (val != null) {
                return serializer.read(val);
            }
        }

        Optional<FeatureNode.ConfigDefault<?>> cd = findInheritedDefault(key);
        return cd.map(configDefault -> (T) configDefault.defaultValue).orElse(null);
    }

    public Object getConfigAny(String key) {
        Optional<FeatureNode.ConfigDefault<?>> cd = findInheritedDefault(key);
        if (ServerHolder.get().has()) {
            FeatureState state = ServerHolder.get().features();
            JsonElement val = state.getConfigValue(id().toString(), key, null);
            if (val != null && cd.isPresent()) {
                return cd.get().serializer.read(val);
            }
        }
        return cd.map(cd2 -> cd2.defaultValue).orElse(null);
    }

    public <T> void setConfig(String key, GsonSerializer<T> serializer, T value) {
        if (ServerHolder.get().has()) {
            FeatureState state = ServerHolder.get().features();
            state.setConfigValue(id().toString(), key, serializer.writeToJson(value));
        }
    }

    public FeatureNode node() { return node; }

    public boolean group() { return node == null; }

    public Double getDouble(String key) { return getConfig(key, DoubleConfigSerializer.INSTANCE, Double.class); }
    public Integer getInt(String key) { return getConfig(key, IntConfigSerializer.INSTANCE, Integer.class); }
    public Boolean getBoolean(String key) { return getConfig(key, BooleanConfigSerializer.INSTANCE, Boolean.class); }
    public String getString(String key) { return getConfig(key, StringConfigSerializer.INSTANCE, String.class); }
}
