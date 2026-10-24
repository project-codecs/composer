package com.codex.composer.internal.client.screen;

//? if minecraft: <=1.21.6 {
import com.codex.composer.internal.client.config.DupedBindsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;

//? if minecraft: <=1.20.4 {
/*import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenTexts;
*///? }

public class DupedBindsConfigScreen extends GameOptionsScreen {
    public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    private DupedBindsConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatableWithFallback("composer.config.title", "Duplicate Keybinds"));
    }

    //? if minecraft: <=1.20.4 {
    /*@Override
    protected void init() {
        super.init();
        int leftX = this.width / 2 - 155;
        int rightX = leftX + 160;
        int y = this.height / 6 - 12;

        addDrawableChild(DupedBindsConfig.INSTANCE.rainbowEffectOnDuplicateKeybinds.createWidget(gameOptions, leftX, y, 150));
        addDrawableChild(DupedBindsConfig.INSTANCE.rainbowEffectSpeed.createWidget(gameOptions, rightX, y, 150));
        y += 24;
        addDrawableChild(DupedBindsConfig.INSTANCE.allowDuplicateKeybinds.createWidget(gameOptions, leftX, y, 150));
        y += 24;
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.client.setScreen(this.parent)).dimensions(this.width / 2 - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 16777215);
    }

    *///? }

    //? if minecraft: >=1.20.6 {
    public static void modify(ControlsOptionsScreen screen) {
        screen.layout.addHeader(
                ButtonWidget.builder(Text.translatableWithFallback("composer.config.title", "Duplicate Keybinds"), btn -> MinecraftClient.getInstance().setScreen(new DupedBindsConfigScreen(screen)))
                        .size(110, 20).build(),
                p -> p.alignRight().marginRight(5)
        );
    }
    //? } else {
    /*public static void modify(Screen parent, Consumer<ButtonWidget> consumer) {
        consumer.accept(
                ButtonWidget.builder(Text.translatableWithFallback("composer.config.title", "Duplicate Keybinds"), btn -> MinecraftClient.getInstance().setScreen(new DupedBindsConfigScreen(parent)))
                        .dimensions(parent.width - 115, 5, 110, 20).build()
        );
    }
    *///? }

    //? if minecraft: >=1.21 {
    @Override
    protected void addOptions() {
        assert body != null;
        body.addAll(DupedBindsConfig.INSTANCE.allOptions());
    }
    //? }
}
//? }
