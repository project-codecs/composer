package com.codex.composer.api.v1.overlay;

import com.codex.composer.api.v1.overlay.impl.Overlay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverlayManager {
    private static OverlayManager INSTANCE;
    private final List<Overlay> visible = new ArrayList<>();
    private final Queue<Overlay> queue = new ConcurrentLinkedQueue<>();

    private OverlayManager() {

    }

    public static OverlayManager getInstance() {
        if (INSTANCE == null) INSTANCE = new OverlayManager();
        return INSTANCE;
    }

    public List<Overlay> getVisible() {
        return visible;
    }

    public Queue<Overlay> getQueue() {
        return queue;
    }

    public void queue(@NotNull Overlay overlay) {
        if (!overlay.shouldRemove()) queue.add(overlay);
    }

    public void clearVisible() {
        visible.clear();
    }

    public void clearQueue() {
        queue.clear();
    }
}
