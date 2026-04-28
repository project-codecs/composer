package com.codex.composer.internal.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

//? if minecraft: >=1.21.9
//import net.minecraft.client.input.KeyInput;

public class ModifiedCreditsScreen extends CreditsScreen {
    private final boolean showCredits;

    private float holdTime = 0.0F;
    private float fade = 0.15F;
    private boolean holdingCloseKey = false;

    private static final float REQUIRED_HOLD_TIME = 3.0F;

    public ModifiedCreditsScreen(boolean credits, boolean poem, Runnable finishAction) {
        super(poem, finishAction);
        this.showCredits = credits;
    }

    @Override
    //? if minecraft: <=1.21.6 {
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    //? } else {
    /*public boolean keyPressed(KeyInput input) {
    *///? }
        if (/*? if minecraft: <=1.21.6 { */keyCode/*?} else {*//*input.getKeycode()*//*?}*/ == MinecraftClient.getInstance().options.inventoryKey.boundKey.getCode()) {
            holdingCloseKey = true;
        }
        return super.keyPressed(/*? if minecraft: <=1.21.6 { */keyCode, scanCode, modifiers/*?} else {*//*input*//*?}*/);
    }

    @Override
    //? if minecraft: <=1.21.6 {
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    //? } else {
    /*public boolean keyReleased(KeyInput input) {
    *///? }
        if (/*? if minecraft: <=1.21.6 { */keyCode/*?} else {*//*input.getKeycode()*//*?}*/ == MinecraftClient.getInstance().options.inventoryKey.boundKey.getCode()) {
            holdingCloseKey = false;
        }
        return super.keyReleased(/*? if minecraft: <=1.21.6 { */keyCode, scanCode, modifiers/*?} else {*//*input*//*?}*/);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        float dt = delta / 20.0F;

        if (holdingCloseKey) {
            holdTime += dt;
            fade = Math.min(1.0F, fade + dt * 2.5F);
        } else {
            holdTime = Math.max(0.0F, holdTime - dt * 2.0F);
            fade = Math.max(0.15F, fade - dt * 2.0F);
        }

        if (holdTime >= REQUIRED_HOLD_TIME) {
            this.close();
        }

        Text text = Text.translatable("composer.credits.screen.close", MinecraftClient.getInstance().options.inventoryKey.boundKey.getLocalizedText()).formatted(Formatting.GRAY);
        int alpha = (int)(fade * 255) << 24;
        int color = 0xFFFFFF | alpha;

        int textWidth = this.textRenderer.getWidth(text);
        int textX = this.width - textWidth - 10;
        int textY = this.height - 30;

        context.drawTextWithShadow(this.textRenderer, text, textX, textY, color);

        float progress = Math.min(1.0F, holdTime / REQUIRED_HOLD_TIME);
        int barWidth = 120;
        int barHeight = 3;

        int barX = this.width - barWidth - 10;
        int barY = textY + 12;

        int bgAlpha = (int)(fade * 80) << 24;
        context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFFFFFF | bgAlpha);

        int progressWidth = (int)(barWidth * progress);
        int fgAlpha = (int)(fade * 200) << 24;
        context.fill(barX, barY, barX + progressWidth, barY + barHeight, 0xFFFFFF | fgAlpha);
    }

    public boolean showCredits() {
        return showCredits;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}