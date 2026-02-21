package com.codex.composer.mixin.impl.dynamic_tooltips;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import com.codex.composer.api.v1.tooltips.layout.DynamicTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    // This used to be fine, but for some reason it seems that at runtime the @Local fails to capture list2 and it just crashes. Probably because of obfuscation, not going to fix
//    @Inject(method = "getTooltipFromItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroups;getGroupsToDisplay()Ljava/util/List;"), cancellable = true)
//    public void composer$fixItemTooltips(ItemStack stack, CallbackInfoReturnable<List<Text>> cir, @Local(name = "list2") List<Text> list) {
//        DynamicTooltip.appendRegistered(stack, list, DynamicTooltip.Location.AFTER_SEARCH_TAGS);
//
//        ComposedStream.of(ItemGroups.getGroupsToDisplay().stream())
//                .filter(group -> group.contains(stack), group -> !group.getType().equals(ItemGroup.Type.SEARCH))
//                .exit()
//                .forEach(group -> list.add(group.getDisplayName().copy().formatted(Formatting.BLUE)));
//
//        DynamicTooltip.appendRegistered(stack, list, DynamicTooltip.Location.AFTER_ITEM_GROUPS);
//
//        cir.setReturnValue(list);
//    }

    @Inject(method = "getTooltipFromItem", at = @At("RETURN"), cancellable = true)
    public void composer$appendTooltip(ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> tooltip = new ArrayList<>(cir.getReturnValue());
        DynamicTooltip.appendRegistered(stack, tooltip, DynamicTooltip.Location.AFTER_CREATIVE_TOOLTIP);
        cir.setReturnValue(tooltip);
    }
}
