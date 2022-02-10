package com.github.reoseah.treehollows;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TreeHollows implements ModInitializer {
	public static final String MOD_ID = "treehollows";

	public static final Block OAK_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
	public static final Block SPRUCE_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
	public static final Block BIRCH_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.PALE_YELLOW).strength(2.0f).sounds(BlockSoundGroup.WOOD));
	public static final Block JUNGLE_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.DIRT_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
	public static final Block ACACIA_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.ORANGE).strength(2.0f).sounds(BlockSoundGroup.WOOD));
	public static final Block DARK_OAK_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));

	public static final BlockEntityType<TreeHollowBlockEntity> TREE_HOLLOW = FabricBlockEntityTypeBuilder.create(TreeHollowBlockEntity::new, OAK_HOLLOW, SPRUCE_HOLLOW, BIRCH_HOLLOW, JUNGLE_HOLLOW, ACACIA_HOLLOW, DARK_OAK_HOLLOW).build();

	@Override
	public void onInitialize() {
		register(Registry.BLOCK, "oak_hollow", OAK_HOLLOW);
		register(Registry.BLOCK, "spruce_hollow", SPRUCE_HOLLOW);
		register(Registry.BLOCK, "birch_hollow", BIRCH_HOLLOW);
		register(Registry.BLOCK, "jungle_hollow", JUNGLE_HOLLOW);
		register(Registry.BLOCK, "acacia_hollow", ACACIA_HOLLOW);
		register(Registry.BLOCK, "dark_oak_hollow", DARK_OAK_HOLLOW);

		register(Registry.BLOCK_ENTITY_TYPE, "tree_hollow", TREE_HOLLOW);

		register(Registry.ITEM, "oak_hollow", new BlockItem(OAK_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
		register(Registry.ITEM, "spruce_hollow", new BlockItem(SPRUCE_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
		register(Registry.ITEM, "birch_hollow", new BlockItem(BIRCH_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
		register(Registry.ITEM, "jungle_hollow", new BlockItem(JUNGLE_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
		register(Registry.ITEM, "acacia_hollow", new BlockItem(ACACIA_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
		register(Registry.ITEM, "dark_oak_hollow", new BlockItem(DARK_OAK_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
	}

	public static <T> T register(Registry<? super T> registry, String id, T entry) {
		return Registry.register(registry, new Identifier(MOD_ID, id), entry);
	}
}
