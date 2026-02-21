package com.codex.composer.api.v1.toast;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.StringIdentifiable;
import com.codex.composer.api.v1.toast.impl.AbstractToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//? if minecraft: >=1.21
import net.minecraft.client.render.RenderTickCounter;

public class ToastManager {
    private final HashMap<Corner, List<AbstractToast>> toastMap = new HashMap<>();
    private static ToastManager INSTANCE;

    private ToastManager() {
        toastMap.put(Corner.TOP_LEFT, new ArrayList<>());
        toastMap.put(Corner.TOP_RIGHT, new ArrayList<>());
        toastMap.put(Corner.BOTTOM_RIGHT, new ArrayList<>());
        toastMap.put(Corner.BOTTOM_LEFT, new ArrayList<>());
    }

    public static ToastManager getInstance() {
        if (INSTANCE == null) INSTANCE = new ToastManager();
        return INSTANCE;
    }

    public void addToast(AbstractToast toast, Corner corner) {
        toastMap.get(corner).add(toast);
        toast.init(MinecraftClient.getInstance().textRenderer);
    }

    public boolean addToastToEmptySlot(AbstractToast toast) {
        boolean alreadyExists = toastMap.values().stream()
                .flatMap(List::stream)
                .anyMatch(existing -> existing.getClass().equals(toast.getClass()));

        if (alreadyExists) {
            return false;
        }

        for (Corner corner : new Corner[]{Corner.TOP_LEFT, Corner.TOP_RIGHT, Corner.BOTTOM_RIGHT, Corner.BOTTOM_LEFT}) {
            List<AbstractToast> list = toastMap.get(corner);
            if (list.isEmpty()) {
                addToast(toast, corner);
                return true;
            }
        }

        return false;
    }

    public void render(DrawContext context, /*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ fIgnored) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        for (Corner corner : Corner.values()) {
            List<AbstractToast> toasts = toastMap.get(corner);
            if (!toasts.isEmpty()) {
                AbstractToast toast = toasts.get(0);
                int centerX;
                int centerY = switch (corner) {
                    case TOP_LEFT -> {
                        centerX = toast.margin() + toast.size().x / 2;
                        yield toast.margin() + toast.size().y / 2;
                    }
                    case TOP_RIGHT -> {
                        centerX = screenWidth - toast.margin() - toast.size().x / 2;
                        yield toast.margin() + toast.size().y / 2;
                    }
                    case BOTTOM_LEFT -> {
                        centerX = toast.margin() + toast.size().x / 2;
                        yield screenHeight - toast.margin() - toast.size().y / 2;
                    }
                    case BOTTOM_RIGHT -> {
                        centerX = screenWidth - toast.margin() - toast.size().x / 2;
                        yield screenHeight - toast.margin() - toast.size().y / 2;
                    }
                };
                toast.draw(context, centerX, centerY);
                if (toast.shouldRemove()) {
                    toasts.removeFirst();
                }
            }
        }
    }

    public void clear() {
        toastMap.values().forEach(List::clear);
    }

    public enum Corner implements StringIdentifiable {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

        public static final com.mojang.serialization.Codec<Corner> CODEC = StringIdentifiable.createCodec(Corner::values);

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
