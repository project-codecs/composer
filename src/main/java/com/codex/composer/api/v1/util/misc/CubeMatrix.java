package com.codex.composer.api.v1.util.misc;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CubeMatrix<T> {
    private final Map<Integer, Map<Integer, Map<Integer, T>>> mat = new HashMap<>();

    public CubeMatrix() {}

    public void set(Vector3i pos, T value) {
        mat.computeIfAbsent(pos.x, k -> new HashMap<>())
                .computeIfAbsent(pos.y, k -> new HashMap<>())
                .put(pos.z, value);
    }

    public T get(Vector3i pos, T def) {
        Map<Integer, Map<Integer, T>> byX = mat.get(pos.x);
        if (byX == null) return def;
        Map<Integer, T> byY = byX.get(pos.y);
        if (byY == null) return def;
        return byY.get(pos.z);
    }

    public @Nullable T get(Vector3i pos) {
        return get(pos, null);
    }

    public boolean has(Vector3i pos) {
        return get(pos) != null;
    }

    public void remove(Vector3i pos) {
        Map<Integer, Map<Integer, T>> byX = mat.get(pos.x);
        if (byX == null) return;
        Map<Integer, T> byY = byX.get(pos.y);
        if (byY == null) return;
        byY.remove(pos.z);
        if (byY.isEmpty()) byX.remove(pos.y);
        if (byX.isEmpty()) mat.remove(pos.x);
    }

    public <R> CubeMatrix<R> flatMap(Function<T, R> mapper) {
        CubeMatrix<R> res = new CubeMatrix<>();

        for (var xEntry : mat.entrySet()) {
            int x = xEntry.getKey();

            for (var yEntry : xEntry.getValue().entrySet()) {
                int y = yEntry.getKey();

                for (var zEntry : yEntry.getValue().entrySet()) {
                    int z = zEntry.getKey();
                    T value = zEntry.getValue();

                    if (value != null) {
                        res.set(new Vector3i(x, y, z), mapper.apply(value));
                    }
                }
            }
        }

        return res;
    }

    public Bounds getBounds() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        for (var xEntry : mat.entrySet()) {
            int x = xEntry.getKey();
            for (var yEntry : xEntry.getValue().entrySet()) {
                int y = yEntry.getKey();
                for (var zEntry : yEntry.getValue().entrySet()) {
                    int z = zEntry.getKey();

                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    minZ = Math.min(minZ, z);

                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                    maxZ = Math.max(maxZ, z);
                }
            }
        }

        return new Bounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void clear() {
        mat.clear();
    }

    public record Bounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {}
}