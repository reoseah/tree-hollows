package com.github.reoseah.treehollows;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TreeHollows implements ModInitializer {
    public static final String MOD_ID = "treehollows";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier LOOT_TABLE_ID = new Identifier(MOD_ID, "chests/tree_hollow");
    public static final Map<Block, Block> TREE_HOLLOWS_MAP = new HashMap<>();

    public static final Block OAK_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final Block SPRUCE_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final Block BIRCH_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.PALE_YELLOW).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final Block JUNGLE_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.DIRT_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final Block ACACIA_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.ORANGE).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_OAK_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD));

    // TODO create only if Architects' Palette loaded
    public static final Block TWISTED_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MapColor.PURPLE).strength(2.0f, 3.0F).sounds(BlockSoundGroup.WOOD));

    public static final BlockEntityType<TreeHollowBlockEntity> BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(TreeHollowBlockEntity::new, OAK_HOLLOW, SPRUCE_HOLLOW, BIRCH_HOLLOW, JUNGLE_HOLLOW, ACACIA_HOLLOW, DARK_OAK_HOLLOW, TWISTED_HOLLOW).build();

    public static final TreeDecoratorType<TreeHollowTreeDecorator> TREE_DECORATOR_TYPE = new TreeDecoratorType<>(TreeHollowTreeDecorator.CODEC);

    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(MOD_ID, name), entry);
    }

    @Override
    public void onInitialize() {
        TreeHollowsConfig.reload();

        register(Registry.BLOCK, "oak_hollow", OAK_HOLLOW);
        register(Registry.BLOCK, "spruce_hollow", SPRUCE_HOLLOW);
        register(Registry.BLOCK, "birch_hollow", BIRCH_HOLLOW);
        register(Registry.BLOCK, "jungle_hollow", JUNGLE_HOLLOW);
        register(Registry.BLOCK, "acacia_hollow", ACACIA_HOLLOW);
        register(Registry.BLOCK, "dark_oak_hollow", DARK_OAK_HOLLOW);
        if (FabricLoader.getInstance().isModLoaded("architects_palette")) {
            register(Registry.BLOCK, "architects_palette/twisted_hollow", TWISTED_HOLLOW);
        }
        register(Registry.BLOCK_ENTITY_TYPE, "tree_hollow", BLOCK_ENTITY_TYPE);

        register(Registry.TREE_DECORATOR_TYPE, "tree_hollow", TREE_DECORATOR_TYPE);

        register(Registry.ITEM, "oak_hollow", new BlockItem(OAK_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        register(Registry.ITEM, "spruce_hollow", new BlockItem(SPRUCE_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        register(Registry.ITEM, "birch_hollow", new BlockItem(BIRCH_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        register(Registry.ITEM, "jungle_hollow", new BlockItem(JUNGLE_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        register(Registry.ITEM, "acacia_hollow", new BlockItem(ACACIA_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        register(Registry.ITEM, "dark_oak_hollow", new BlockItem(DARK_OAK_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        if (FabricLoader.getInstance().isModLoaded("architects_palette")) {
            register(Registry.ITEM, "architects_palette/twisted_hollow", new BlockItem(TWISTED_HOLLOW, new Item.Settings().group(ItemGroup.DECORATIONS)));
        }
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            TREE_HOLLOWS_MAP.put(Blocks.OAK_LOG, OAK_HOLLOW);
            TREE_HOLLOWS_MAP.put(Blocks.SPRUCE_LOG, SPRUCE_HOLLOW);
            TREE_HOLLOWS_MAP.put(Blocks.BIRCH_LOG, BIRCH_HOLLOW);
            TREE_HOLLOWS_MAP.put(Blocks.JUNGLE_LOG, JUNGLE_HOLLOW);
            TREE_HOLLOWS_MAP.put(Blocks.ACACIA_LOG, ACACIA_HOLLOW);
            TREE_HOLLOWS_MAP.put(Blocks.DARK_OAK_LOG, DARK_OAK_HOLLOW);
            if (Registry.BLOCK.containsId(new Identifier("architects_palette:twisted_log"))) {
                TREE_HOLLOWS_MAP.put(Registry.BLOCK.get(new Identifier("architects_palette:twisted_log")), TWISTED_HOLLOW);
            }
        });
    }
}
