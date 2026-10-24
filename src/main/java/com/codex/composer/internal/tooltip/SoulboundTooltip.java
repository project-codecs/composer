package com.codex.composer.internal.tooltip;

import com.codex.composer.api.v1.item.settings.component.SoulboundComponent;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

//? if minecraft: <=1.20.4 {
/*import net.minecraft.client.item.TooltipContext;
*///? } else if minecraft: <=1.20.6 {
/*import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
*///? } else {
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.item.Item;
//? }

import java.util.List;

public enum SoulboundTooltip implements ItemTooltipCallback {
    INST;

    //? if minecraft: <=1.20.4 {
    /*public void getTooltip(ItemStack stack, TooltipContext ctx, List<Text> lines) {
    *///?} else {
    public void getTooltip(ItemStack stack, Item.TooltipContext ctx, TooltipType type, List<Text> lines) {
    //?}
        if (!SoulboundComponent.isSoulbound(stack)) return;
        lines.add(Text.translatable("composer.tooltips.soulbound").formatted(Formatting.GRAY));
        if (/*~ if minecraft: <=1.20.4 'type' -> 'ctx' {*/type/*~}*/.isAdvanced() && SoulboundComponent.shouldNotDrop(stack))
            lines.add(Text.translatable("composer.tooltips.soulbound.droppable.not").formatted(Formatting.GRAY));
    }
}
