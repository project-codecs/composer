package com.codex.composer.api.v1.item.settings.component;

import net.minecraft.item.ItemStack;
import java.util.Objects;

//? if >=1.20.6 {
import com.codex.composer.internal.registry.ModDataComponentTypes;
//? } else {
/*import com.codex.composer.mixin.accessor.ItemMethodAccessor;
*///? }

public class SoulboundComponent {
    public static boolean isSoulbound(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        //? if >=1.20.6 {
        return stack.contains(ModDataComponentTypes.SOULBOUND) && Objects.requireNonNull(stack.get(ModDataComponentTypes.SOULBOUND));
        //?} else {
        /*ItemMethodAccessor settings = ItemMethodAccessor.get(stack);
        return settings != null && settings.composer$soulbound();
        *///? }
    }

    public static boolean canDropSoulbound(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        //? if >=1.20.6 {
        return stack.contains(ModDataComponentTypes.SOULBOUND_CAN_DROP) ? Objects.requireNonNull(stack.get(ModDataComponentTypes.SOULBOUND_CAN_DROP)) : true;
        //?} else {
        /*ItemMethodAccessor settings = ItemMethodAccessor.get(stack);
        return settings != null ? settings.composer$soulboundCanDrop() : true;
        *///? }
    }

    public static boolean shouldNotDrop(ItemStack stack) {
        return isSoulbound(stack) && !canDropSoulbound(stack);
    }
}
