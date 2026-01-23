package com.codex.composer.api.v1.util.misc;

import java.util.List;
import java.util.function.Supplier;

public class LanguageUtils {
    public static String plural(String key, String plural, List<?> ls) {
        return String.format("%s%s", key, ls.size() == 1 ? "" : plural);
    }

    public static String plural(String key, List<?> ls) {
        return plural(key, "s", ls);
    }

    public static String negate(String key, String negate, boolean b) {
        return String.format("%s%s", key, b ? "" : negate);
    }

    public static String negate(String key, String negate, Supplier<Boolean> b) {
        return String.format("%s%s", key, b.get() ? "" : negate);
    }

    public static String negate(String key, boolean b) {
        return negate(key, ".not", b);
    }

    public static String negate(String key, Supplier<Boolean> b) {
        return negate(key, ".not", b);
    }
}
