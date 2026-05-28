package com.codex.composer.api.v1.util.wildcard;

import com.codex.ambarella.api.v1.util.math.CharCount;

public class IdentifierWildcards {
    public static boolean matches(String id, String namespacePattern, String pathPattern) {
        if (CharCount.count(id, ':') != 1) return false;
        String[] parts = id.split(":");
        return Wildcards.matches(namespacePattern, parts[0], '_') && Wildcards.matches(pathPattern, parts[1], '_');
    }
}
