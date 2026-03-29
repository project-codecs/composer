package com.codex.composer.internal.registry;

import com.codex.composer.api.v1.feature.FeatureBuilder;
import com.codex.composer.api.v1.feature.FeatureHandle;
import com.codex.composer.api.v1.registry.lazy.DeferredFeatureRegistry;
import com.codex.composer.api.v1.registry.lazy.feature.Feature;
import com.codex.composer.internal.Composer;

public class ModFeatures {
    private static final DeferredFeatureRegistry FEATURES = new DeferredFeatureRegistry(Composer.MOD_ID);

    public static class TargetSynchronization {
        static final String FREQ_KEY = "update_frequency";

        public static final Feature ENTITY = FEATURES.hang("entity", TargetSynchronization::config);
        public static final Feature BLOCK = FEATURES.hang("block", TargetSynchronization::config);

        private static void config(FeatureBuilder builder) {
            builder.defaultEnabled(true).configInt(FREQ_KEY, 1);
        }

        public static boolean entity() {
            return ENTITY.getHandle().enabled();
        }

        public static int eFreq() {
            return ENTITY.getHandle().getInt(FREQ_KEY);
        }

        public static boolean block() {
            return BLOCK.getHandle().enabled();
        }

        public static int bFreq() {
            return BLOCK.getHandle().getInt(FREQ_KEY);
        }
    }

    public static final FeatureHandle DEBUG = FEATURES.register("debug", false);
    public static boolean debug() {
        return DEBUG.enabled();
    }

    public static void initialize() {
        FEATURES.group(
                "target_synchronization",
                TargetSynchronization.BLOCK,
                TargetSynchronization.ENTITY
        );
    }
}
