package com.codex.composer.api.v1.datafix.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.datafix.DataFixerRegistry;
import com.codex.composer.api.v1.util.wildcard.IdentifierWildcards;

import java.util.Optional;

//? if minecraft: <=1.21.4
//import net.minecraft.nbt.NbtElement;

public record SimpleItemFixer(String idPattern, Identifier replacement,
                              boolean copyNbt) implements DataFixerRegistry.Item {

    @Override
    public Optional<ItemStack> process(String id, NbtCompound tag) {
        String[] parts = idPattern.split(":");
        if (IdentifierWildcards.matches(id, parts[0], parts[1])) {
            ItemStack stack = new ItemStack(Registries.ITEM.get(replacement), tag.contains("Count"/*? if minecraft: <=1.21.4 {*//*, NbtElement.INT_TYPE*//*? }*/) ? tag.getInt("Count")/*? if minecraft: >=1.21.5 {*/.orElse(0)/*? }*/ : 1);
            tag.remove("id");
            //? if minecraft: <=1.20.1
            //if (copyNbt) stack.setNbt(tag);
            return Optional.of(stack);
        }

        return Optional.empty();
    }
}
