package com.codex.composer.internal.data.loader;

import com.codex.composer.api.v1.multiblock.MultiblockRegistry;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.data.loader.json.MultiblockJsonLoader;
import com.codex.composer.internal.multiblock.JsonBackedMultiblock;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

//? if minecraft: >=26.2 {
/*import net.minecraft.resource.SynchronousResourceReloader;
*///? } else {
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
//? }

public class MultiblockLoader implements /*? if minecraft: >=26.2 { *//*SynchronousResourceReloader*//*? } else { */SimpleSynchronousResourceReloadListener/*? }*/ {
    public static final Identifier ID = Composer.identify("multiblock");

    //? if minecraft: <26.2 {
    @Override
    public Identifier getFabricId() {
        return ID;
    }
    //? }

    @Override
    public void /*? if minecraft: >=26.2 { *//*scheduleReload*//*? } else { */reload/*? }*/(ResourceManager manager) {
        MultiblockRegistry registry = MultiblockRegistry.getInstance();

        registry.clear();
        registry.setLoadingFiles();

        manager.findResources("multiblocks", path -> path.getPath().endsWith(".json")).forEach((id, res) -> {
            MultiblockJsonLoader.ParsedMultiblock parsed = MultiblockJsonLoader.parse(id, res);

            if (parsed == null) Composer.LOGGER.warn("Failed to read multiblock '{}'!", id.toString());
            else registry.register(parsed.getId(), JsonBackedMultiblock.ofParsed(parsed));
        });

        registry.finishedLoadingFiles();
    }
}
