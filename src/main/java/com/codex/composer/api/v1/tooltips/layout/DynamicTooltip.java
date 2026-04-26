package com.codex.composer.api.v1.tooltips.layout;

import com.codex.composer.api.v1.tooltips.DynamicTooltipRegistry;
import com.codex.composer.api.v1.tooltips.TooltipContext;
import com.codex.composer.internal.client.config.ComposerClientConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a tooltip section, which can have nested content sections.
 */
public interface DynamicTooltip {
    /**
     * The main method that appends tooltip lines to a list of strings.
     */
    //? if minecraft: <=1.21.4
    //void appendTooltip(TooltipContext context, List<Text> out);
    //? if minecraft: >=1.21.5
    void appendTooltip(TooltipContext context, Consumer<Text> out);
    boolean isRelevant(TooltipContext context);
    Location where();

    //? if minecraft: <=1.21.4
    //static void appendRegistered(ItemStack stack, List<Text> text, DynamicTooltip.Location location) {
    //? if minecraft: >=1.21.5
    static void appendRegistered(ItemStack stack, Consumer<Text> text, DynamicTooltip.Location location) {
        List<DynamicTooltip> tooltips = DynamicTooltipRegistry.getInstance()
                .getAll()
                .values()
                .stream()
                .filter(t -> t.where() == location)
                .toList();

        if (tooltips.isEmpty()) return;

        TooltipContext ctx = new TooltipContext(
                stack,
                Screen.hasShiftDown(),
                Screen.hasControlDown(),
                Screen.hasAltDown()
        );

        tooltips
                .stream()
                .filter(t -> t.isRelevant(ctx) || ComposerClientConfig.INSTANCE.alwaysShowTooltips.get())
                .forEach(t -> t.appendTooltip(ctx, text));
    }

    enum Location {
        // ---- Normal Locations ----
        HEAD,
        AFTER_NAME,
        AFTER_MAP_ID,
        AFTER_ITEM_TOOLTIP,
        //? if minecraft: >=1.21
        AFTER_JUKEBOX_PLAYABLE,
        AFTER_TRIM,
        //? if minecraft: >=1.20.6
        AFTER_STORED_ENCHANTMENTS,
        AFTER_ENCHANTMENTS,
        AFTER_DYED_COLOR,
        AFTER_LORE,
        AFTER_ATTRIBUTE_MODIFIERS,
        AFTER_UNBREAKABLE,
        //? if minecraft: >=1.21 {
        AFTER_OMINOUS_BOTTLE_AMPLIFIER,
        AFTER_SUSPICIOUS_STEW_EFFECTS,
        //? }
        //? if minecraft: >=1.20.6 {
        AFTER_CAN_BREAK,
        AFTER_CAN_PLACE_ON,
        //? }
        AFTER_DURABILITY,
        AFTER_ITEM_ID,
        //? if minecraft: >=1.20.6 {
        AFTER_COMPONENTS,
        //? } else {
        /*AFTER_TAGS,
        *///? }
        AFTER_DISABLED_TEXT,
        //? if minecraft: >=1.21
        AFTER_OPERATOR_WARNINGS,
        TAIL,

        // ---- Creative Inventory Only Locations ----
        AFTER_CREATIVE_TOOLTIP
    }
}
