package com.codex.composer.mixin.impl.item_group_modifications;

import com.codex.composer.api.v1.item.group.ItemGroupModifications;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemGroup.class)
public class ItemGroupMixin {
    @Mixin(ItemGroup.EntriesImpl.class)
    public static abstract class ItemGroupEntriesMixin {
        @Shadow
        @Final
        private ItemGroup group;

        @Shadow
        public abstract void add(ItemStack stack, ItemGroup.StackVisibility visibility);

        @Unique
        private boolean deep = false;

        @Inject(method = "add(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemGroup$StackVisibility;)V", at = @At("TAIL"))
        public void composer$appendModifications(ItemStack stack, ItemGroup.StackVisibility visibility, CallbackInfo ci) {
            if (deep) {
                deep = false;
                return;
            }

            List<Pair<Item, ItemStack[]>> modifications = ItemGroupModifications.getFor(Registries.ITEM_GROUP.getKey(group).orElseThrow());
            if (modifications != null && !modifications.isEmpty()) {
                modifications.stream()
                        .filter(pair -> stack.getItem().equals(pair.getLeft()))
                        .forEach(pair -> {
                            for (ItemStack item : pair.getRight()) {
                                deep = true;
                                add(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
                            }
                        });
            }
        }
    }
}
