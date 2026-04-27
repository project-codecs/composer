package com.codex.composer.nbt;

import net.minecraft.nbt.NbtCompound;
import org.junit.jupiter.api.Test;
import com.codex.composer.api.v1.nbt.ComposerCompound;
import com.codex.composer.api.v1.nbt.NbtSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComposerCompoundTest {

    static class TestSerializable implements NbtSerializable<TestSerializable> {
        final String value;

        TestSerializable(String value) {
            this.value = value;
        }

        TestSerializable(NbtCompound tag) {
            this.value = tag.getString("value")/*? if minecraft: >=1.21.5 {*//*.orElse("")*//*? }*/;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound tag) {
            tag.putString("value", value);
            return tag;
        }
    }

    @Test
    void testPutAndGetSerializable() {
        ComposerCompound compound = new ComposerCompound();
        TestSerializable obj = new TestSerializable("hello");

        compound.putSerializable("test", obj);
        TestSerializable retrieved = compound.getSerializable("test", TestSerializable::new);

        assertEquals("hello", retrieved.value);
    }

    @Test
    void testPutAndGetList() {
        ComposerCompound compound = new ComposerCompound();
        List<TestSerializable> list = List.of(new TestSerializable("a"), new TestSerializable("b"));

        compound.putList("list", list);
        List<TestSerializable> retrieved = compound.getList("list", TestSerializable::new);

        assertEquals(2, retrieved.size());
        assertEquals("a", retrieved.get(0).value);
        assertEquals("b", retrieved.get(1).value);
    }

    @Test
    void testPutAndGetMap() {
        ComposerCompound compound = new ComposerCompound();
        Map<String, TestSerializable> map = new HashMap<>();
        map.put("one", new TestSerializable("1"));
        map.put("two", new TestSerializable("2"));

        compound.putMap("map", map);
        Map<String, TestSerializable> retrieved = compound.getMap("map", TestSerializable::new);

        assertEquals(2, retrieved.size());
        assertEquals("1", retrieved.get("one").value);
        assertEquals("2", retrieved.get("two").value);
    }

    @Test
    void testCloneAndCopy() {
        ComposerCompound compound = new ComposerCompound();
        compound.asVanilla().putString("key", "value");

        ComposerCompound clone = compound.clone();
        ComposerCompound copy = ComposerCompound.copy(compound.asVanilla());

        assertEquals("value", clone.asVanilla().getString("key")/*? if minecraft: >=1.21.5 {*//*.orElse("")*//*? }*/);
        assertEquals("value", copy.asVanilla().getString("key")/*? if minecraft: >=1.21.5 {*//*.orElse("")*//*? }*/);
    }

    @Test
    void testGetListOrDefault() {
        ComposerCompound compound = new ComposerCompound();

        // Key doesn't exist, should return empty list
        List<TestSerializable> list = compound.getListOrDefault("missing", TestSerializable::new);
        assertTrue(list.isEmpty());
    }
}
