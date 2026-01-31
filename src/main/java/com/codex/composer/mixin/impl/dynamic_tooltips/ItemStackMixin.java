package com.codex.composer.mixin.impl.dynamic_tooltips;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.codex.composer.api.v1.tooltips.layout.DynamicTooltip.Location.*;
import static com.codex.composer.api.v1.tooltips.layout.DynamicTooltip.appendRegistered;

import java.util.List;


//? minecraft: >=1.20.6 <1.21 {
/*import net.minecraft.client.item.TooltipType;
*///? } else if minecraft: >=1.21 {
import net.minecraft.item.tooltip.TooltipType;
//? }

//? minecraft: >=1.20.6
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract ItemStack copy();

    //? if minecraft: >=1.20.6 {
    @SuppressWarnings("DiscouragedShift")
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE, ordinal = 0))
    private void composer$append$0(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, HEAD);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 0))
    private void composer$append$1(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_NAME);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 1))
    private void composer$append$2(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_MAP_ID);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER))
    *///? }
    private void composer$append$3(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ITEM_TOOLTIP);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 0))
    private void composer$append$4(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_JUKEBOX_PLAYABLE);
    }
    //? }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 1))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 0))
    *///? }
    private void composer$append$5(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_TRIM);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 2))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 1))
    *///? }
    private void composer$append$6(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_STORED_ENCHANTMENTS);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 3))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 2))
    *///? }
    private void composer$append$7(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ENCHANTMENTS);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 4))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 3))
    *///? }
    private void composer$append$8(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DYED_COLOR);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 5))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 4))
    *///? }
    private void composer$append$9(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_LORE);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendAttributeModifiersTooltip(Ljava/util/function/Consumer;Lnet/minecraft/entity/player/PlayerEntity;)V", shift = At.Shift.AFTER))
    private void composer$append$10(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ATTRIBUTE_MODIFIERS);
    }

    //? if minecraft: >=1.21 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 6))
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/DataComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/client/item/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 5))
    *///? }
    private void composer$append$11(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_UNBREAKABLE);
    }

    //? if minecraft: >=1.21.3 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 7))
    private void composer$append$12(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_OMINOUS_BOTTLE_AMPLIFIER);
    }
    //? }

    //? if minecraft: >=1.21.3 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER, ordinal = 8))
    private void composer$append$13(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_SUSPICIOUS_STEW_EFFECTS);
    }
    //? }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockPredicatesChecker;addTooltips(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER, ordinal = 0))
    private void composer$append$14(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_CAN_BREAK);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockPredicatesChecker;addTooltips(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER, ordinal = 1))
    private void composer$append$15(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_CAN_PLACE_ON);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 2))
    private void composer$append$16(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DURABILITY);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 3))
    private void composer$append$17(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ITEM_ID);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 4))
    private void composer$append$18(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_COMPONENTS);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 5))
    private void composer$append$19(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DISABLED_TEXT);
    }

    //? if minecraft: >=1.21.4 {
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z", shift = At.Shift.AFTER))
    private void composer$append$20(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_OPERATOR_WARNINGS);
    }
    //? }

    @Inject(method = "getTooltip", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void composer$append$21(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> r = cir.getReturnValue();
        appendRegistered(copy(), r, TAIL);
        cir.setReturnValue(r);
    }
    //? } else {
    /*@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;"))
    private void composer$append$0(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, HEAD);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER))
    private void composer$append$1(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_NAME);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1, shift = At.Shift.AFTER))
    private void composer$append$2(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_MAP_ID);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void composer$append$3(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ITEM_TOOLTIP);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/trim/ArmorTrim;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void composer$append$5(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_TRIM);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void composer$append$6(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ENCHANTMENTS);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2, shift = At.Shift.AFTER))
    private void composer$append$7$0(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DYED_COLOR);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3, shift = At.Shift.AFTER))
    private void composer$append$7$1(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DYED_COLOR);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 4, shift = At.Shift.AFTER))
    private void composer$append$8(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_LORE);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 1, shift = At.Shift.BEFORE))
    private void composer$append$9(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ATTRIBUTE_MODIFIERS);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 10, shift = At.Shift.AFTER))
    private void composer$append$10(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_UNBREAKABLE);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 15, shift = At.Shift.AFTER))
    private void composer$append$15(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DURABILITY);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 16, shift = At.Shift.AFTER))
    private void composer$append$16(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_ITEM_ID);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 17, shift = At.Shift.AFTER))
    private void composer$append$17(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_TAGS);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 18, shift = At.Shift.AFTER))
    private void composer$append$18(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, AFTER_DISABLED_TEXT);
    }

    @Inject(method = "getTooltip", at = @At("TAIL"))
    private void composer$append$20(PlayerEntity player, net.minecraft.client.item.TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        appendRegistered(copy(), list, TAIL);
    }
    *///? }
}
