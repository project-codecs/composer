package com.codex.composer.api.v1.multiblock.ingame;

import com.codex.composer.api.v1.block.entity.MultiblockExtensionBlockEntity;
import com.codex.composer.api.v1.multiblock.Multiblock;
import com.codex.composer.api.v1.multiblock.MultiblockMatch;
import com.codex.composer.api.v1.multiblock.MultiblockRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MultiblockController {
    public static final int SEARCH_RADIUS = 16;

    private static final Set<MultiblockController> PENDING = new LinkedHashSet<>();

    private final BlockEntity entity;
    private final MultiblockHost host;

    private boolean formed;
    private @Nullable Direction lastOrientation;
    private Map<Identifier, List<BlockPos>> extensions = Map.of();
    private Set<BlockPos> attached = Set.of();

    public <T extends BlockEntity & MultiblockHost> MultiblockController(T host) {
        this.entity = host;
        this.host = host;
    }

    public static Optional<MultiblockController> at(BlockView world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof MultiblockHost h ? Optional.of(h.getMultiblockController()) : Optional.empty();
    }

    public boolean isFormed() {
        return formed;
    }

    public void restoreFormed(boolean formed) {
        this.formed = formed;
    }

    @Nullable
    public Multiblock getMultiblock() {
        return MultiblockRegistry.getInstance().get(host.multiblock());
    }

    public List<BlockPos> getExtensions(Identifier id) {
        return extensions.getOrDefault(id, List.of());
    }

    public Map<Identifier, List<BlockPos>> getExtensions() {
        return extensions;
    }

    public Optional<BlockPos> getFirstExtension(Identifier id) {
        List<BlockPos> list = getExtensions(id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public int countExtensions(Identifier id) {
        return getExtensions(id).size();
    }

    public <T extends BlockEntity> List<T> getExtensionBlockEntities(Identifier id, Class<T> type) {
        World world = entity.getWorld();
        if (world == null) return List.of();
        List<T> out = new ArrayList<>();
        for (BlockPos p : getExtensions(id)) {
            BlockEntity be = world.getBlockEntity(p);
            if (type.isInstance(be)) out.add(type.cast(be));
        }
        return out;
    }

    @Nullable
    public BlockBox getStructureBounds() {
        Multiblock mb = getMultiblock();
        return mb == null ? null : mb.bounds(entity.getPos(), host.getOrientation(entity.getCachedState()));
    }

    public void requestValidation() {
        World world = entity.getWorld();
        if (world != null && !world.isClient()) PENDING.add(this);
    }

    public void handleRemoved() {
        PENDING.remove(this);
        World world = entity.getWorld();
        if (world == null || world.isClient()) return;

        for (BlockPos p : attached) detach(p);
        attached = Set.of();
        extensions = Map.of();

        if (formed) {
            formed = false;
            host.onMultiblockBroken();
        }
    }

    public static void notifyBlockChanged(World world, BlockPos changed) {
        if (world.isClient()) return;
        ChunkManager manager = world.getChunkManager();

        int minX = (changed.getX() - SEARCH_RADIUS) >> 4, maxX = (changed.getX() + SEARCH_RADIUS) >> 4;
        int minZ = (changed.getZ() - SEARCH_RADIUS) >> 4, maxZ = (changed.getZ() + SEARCH_RADIUS) >> 4;

        for (int cx = minX; cx <= maxX; cx++) {
            for (int cz = minZ; cz <= maxZ; cz++) {
                WorldChunk chunk = manager.getWorldChunk(cx, cz);
                if (chunk == null) continue;

                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (be instanceof MultiblockHost h) h.getMultiblockController().onBlockChanged(changed);
                }
            }
        }
    }

    private void onBlockChanged(BlockPos changed) {
        BlockBox box = getStructureBounds();
        if (box == null || !box.contains(changed)) return;

        if (changed.equals(entity.getPos()) && host.getOrientation(entity.getCachedState()) == lastOrientation) return;
        requestValidation();
    }

    @ApiStatus.Internal
    public static void processPending(ServerWorld world) {
        if (PENDING.isEmpty()) return;

        List<MultiblockController> batch = new ArrayList<>();
        for (Iterator<MultiblockController> it = PENDING.iterator(); it.hasNext(); ) {
            MultiblockController c = it.next();
            if (c.entity.getWorld() == world) {
                it.remove();
                batch.add(c);
            }
        }

        for (MultiblockController c : batch) if (!c.entity.isRemoved()) c.validate();
    }

    private void validate() {
        World world = entity.getWorld();
        Multiblock multiblock = getMultiblock();
        Direction orientation = host.getOrientation(entity.getCachedState());
        BlockBox box = multiblock == null ? null : multiblock.bounds(entity.getPos(), orientation);

        if (world == null || multiblock == null || box == null || !isFullyLoaded(world, box)) {
            requestValidation();
            return;
        }

        lastOrientation = orientation;
        MultiblockMatch match = multiblock.match(world, entity.getPos(), orientation);
        boolean wasFormed = formed;

        Map<Identifier, List<BlockPos>> newExtensions = match.complete() ? match.extensions() : Map.of();
        Set<BlockPos> newAttached = new HashSet<>();
        newExtensions.values().forEach(newAttached::addAll);

        boolean extensionsChanged = !newExtensions.equals(extensions);
        Set<BlockPos> previous = attached;

        for (BlockPos p : previous) if (!newAttached.contains(p)) detach(p);

        this.extensions = newExtensions;
        this.attached = newAttached;
        this.formed = match.complete();

        newExtensions.forEach((id, list) -> {
            for (BlockPos p : list) if (!previous.contains(p)) attach(p, id);
        });

        if (formed != wasFormed) {
            entity.markDirty();
            if (formed) host.onMultiblockFormed();
            else host.onMultiblockBroken();
        } else if (extensionsChanged) {
            host.onExtensionsChanged();
        }
    }

    private static boolean isFullyLoaded(World world, BlockBox box) {
        ChunkManager manager = world.getChunkManager();
        for (int cx = box.getMinX() >> 4; cx <= box.getMaxX() >> 4; cx++) {
            for (int cz = box.getMinZ() >> 4; cz <= box.getMaxZ() >> 4; cz++) {
                if (!manager.isChunkLoaded(cx, cz)) return false;
            }
        }
        return true;
    }

    private void attach(BlockPos p, Identifier id) {
        World world = entity.getWorld();
        if (world != null && world.getBlockEntity(p) instanceof MultiblockExtensionBlockEntity e) e.onAttached(entity.getPos(), id);
    }

    private void detach(BlockPos p) {
        World world = entity.getWorld();
        if (world != null && world.getBlockEntity(p) instanceof MultiblockExtensionBlockEntity e) e.onDetached(entity.getPos());
    }

    @ApiStatus.Internal public static void onLoad(BlockEntity be, ServerWorld world) { if (be instanceof MultiblockHost h) h.getMultiblockController().requestValidation(); }
    @ApiStatus.Internal public static void onUnload(BlockEntity be, ServerWorld world) { if (be instanceof MultiblockHost h) PENDING.remove(h.getMultiblockController()); }
}