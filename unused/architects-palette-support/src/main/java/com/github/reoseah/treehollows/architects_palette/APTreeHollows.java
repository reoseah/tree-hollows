package com.github.reoseah.treehollows.architects_palette;

import com.github.reoseah.treehollows.TreeHollowBlock;
import com.github.reoseah.treehollows.TreeHollows;
import com.slomaxonical.architectspalette.registry.APBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class APTreeHollows implements ModInitializer {
    public static final @Nullable Block TWISTED_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.PURPLE).strength(2.0f, 3.0F).sounds(BlockSoundGroup.WOOD));

    @Override
    public void onInitialize() {
        TreeHollows.register(Registry.BLOCK, "architects_palette/twisted_hollow", TWISTED_HOLLOW);
        TreeHollows.register(Registry.ITEM, "architects_palette/twisted_hollow", new BlockItem(TWISTED_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));

        TreeHollows.TREE_HOLLOWS.add(TWISTED_HOLLOW);
        TreeHollows.TREE_HOLLOWS_MAP.put(APBlocks.TWISTED_LOG, TWISTED_HOLLOW);
    }
}
