package com.codex.composer.mixin.impl.dynamic_tooltips;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import com.codex.composer.api.v1.tooltips.layout.DynamicTooltip;
import com.codex.composer.api.v1.util.misc.ComposedStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Inject(method = "getTooltipFromItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroups;getGroupsToDisplay()Ljava/util/List;"), cancellable = true)
    public void composer$fixItemTooltips(ItemStack stack, CallbackInfoReturnable<List<Text>> cir, @Local(name = "list2") List<Text> list) {
        DynamicTooltip.appendRegistered(stack, list, DynamicTooltip.Location.AFTER_SEARCH_TAGS);

        ComposedStream.of(ItemGroups.getGroupsToDisplay().stream())
                .filter(group -> group.contains(stack), group -> !group.getType().equals(ItemGroup.Type.SEARCH))
                .exit()
                .forEach(group -> list.add(group.getDisplayName().copy().formatted(Formatting.BLUE)));

        DynamicTooltip.appendRegistered(stack, list, DynamicTooltip.Location.AFTER_ITEM_GROUPS);

        cir.setReturnValue(list);
    }
}
