package com.codex.composer.mixin.impl.datafix;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import com.codex.composer.api.v1.datafix.DataFixerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

//? if minecraft: >=1.20.6
import net.minecraft.registry.RegistryWrapper;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    //? if minecraft: <=1.20.4 {
    /*@Inject(method = "fromNbt", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void composer$runDataFixers(NbtCompound tag, CallbackInfoReturnable<ItemStack> cir) {
        if (tag.contains("id", NbtElement.STRING_TYPE)) {
            for (DataFixerRegistry.Item fixer : DataFixerRegistry.ITEM.getAll().values()) {
                Optional<ItemStack> opt = fixer.process(tag);

                if (opt.isPresent()) {
                    cir.setReturnValue(opt.get());
                    return;
                }
            }
        }
    }
    *///? } else {
    @Inject(method = "fromNbt", at = @At(value = "RETURN"), cancellable = true)
    private static void composer$runDataFixers(RegistryWrapper.WrapperLookup registries, NbtElement nbt, CallbackInfoReturnable<Optional<ItemStack>> cir) {
        if (cir.getReturnValue().isPresent() || !(nbt instanceof NbtCompound tag)) return;
        if (tag.contains("id"/*? if minecraft: <=1.21.4 {*//*, NbtElement.STRING_TYPE*//*? }*/)) {
            for (DataFixerRegistry.Item fixer : DataFixerRegistry.ITEM.getAll().values()) {
                Optional<ItemStack> opt = fixer.process(tag);

                if (opt.isPresent()) {
                    cir.setReturnValue(opt);
                    return;
                }
            }
        }
    }
    //?}
}
