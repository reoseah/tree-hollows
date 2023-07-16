package com.github.reoseah.treehollows;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class TreeHollowsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TreeHollows.loadClass();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(content -> {
            content.acceptAll(((FabricPlatform)FabricPlatform.instance).creativeStacks);
        });
    }
}
