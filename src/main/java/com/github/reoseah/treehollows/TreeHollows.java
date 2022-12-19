package com.github.reoseah.treehollows;

import com.github.reoseah.treehollows.impl.TreeHollowBlock;
import com.github.reoseah.treehollows.impl.TreeHollowBlockEntity;
import com.github.reoseah.treehollows.impl.TreeHollowTreeDecorator;
import com.github.reoseah.treehollows.mixined.MutableTreeFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.HashMap;
import java.util.Map;

public class TreeHollows {
    public static final Block OAK_HOLLOW = Platform.instance.register("oak_hollow", new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0f).sound(SoundType.WOOD)));
    public static final Block SPRUCE_HOLLOW = Platform.instance.register("spruce_hollow", new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0f).sound(SoundType.WOOD)));
    public static final Block BIRCH_HOLLOW = Platform.instance.register("birch_hollow", new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0f).sound(SoundType.WOOD)));
    public static final Block JUNGLE_HOLLOW = Platform.instance.register("jungle_hollow", new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.DIRT).strength(2.0f).sound(SoundType.WOOD)));
    public static final Block ACACIA_HOLLOW = Platform.instance.register("acacia_hollow", new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0f).sound(SoundType.WOOD)));
    public static final Block DARK_OAK_HOLLOW = Platform.instance.register("dark_oak_hollow", new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0f).sound(SoundType.WOOD)));

    public static final Item ITEM_OAK = Platform.instance.register("oak_hollow", new BlockItem(OAK_HOLLOW, new Item.Properties()));
    public static final Item ITEM_SPRUCE = Platform.instance.register("spruce_hollow", new BlockItem(SPRUCE_HOLLOW, new Item.Properties()));
    public static final Item ITEM_BIRCH = Platform.instance.register("birch_hollow", new BlockItem(BIRCH_HOLLOW, new Item.Properties()));
    public static final Item ITEM_JUNGLE = Platform.instance.register("jungle_hollow", new BlockItem(JUNGLE_HOLLOW, new Item.Properties()));
    public static final Item ITEM_ACACIA = Platform.instance.register("acacia_hollow", new BlockItem(ACACIA_HOLLOW, new Item.Properties()));
    public static final Item ITEM_DARK_OAK = Platform.instance.register("dark_oak_hollow", new BlockItem(DARK_OAK_HOLLOW, new Item.Properties()));

    public static final BlockEntityType<?> BLOCK_ENTITY_TYPE = Platform.instance.registerBlockEntity("tree_hollow", TreeHollowBlockEntity::new, OAK_HOLLOW, SPRUCE_HOLLOW, BIRCH_HOLLOW, JUNGLE_HOLLOW, ACACIA_HOLLOW, DARK_OAK_HOLLOW);
    public static final TreeDecoratorType<?> TREE_DECORATOR_TYPE = Platform.instance.registerTreeDecorator("tree_hollow", TreeHollowTreeDecorator.CODEC);

    public static final Map<Block, Block> LOG_TO_HOLLOW_LOG = new HashMap<>();

    static {
        LOG_TO_HOLLOW_LOG.put(Blocks.OAK_LOG, OAK_HOLLOW);
        LOG_TO_HOLLOW_LOG.put(Blocks.SPRUCE_LOG, SPRUCE_HOLLOW);
        LOG_TO_HOLLOW_LOG.put(Blocks.BIRCH_LOG, BIRCH_HOLLOW);
        LOG_TO_HOLLOW_LOG.put(Blocks.JUNGLE_LOG, JUNGLE_HOLLOW);
        LOG_TO_HOLLOW_LOG.put(Blocks.ACACIA_LOG, ACACIA_HOLLOW);
        LOG_TO_HOLLOW_LOG.put(Blocks.DARK_OAK_LOG, DARK_OAK_HOLLOW);
    }

    public static void initialize() {

    }

    public static void tryInsertTreeDecorator(ConfiguredFeature<?, ?> object) {
        if (object.feature() == Feature.TREE && object.config() instanceof TreeConfiguration cfg) {
            // skip tree if it already has tree hollow
            for (TreeDecorator decorator : cfg.decorators) {
                if (decorator instanceof TreeHollowTreeDecorator) {
                    return;
                }
            }
            // or if it has complicated logs
            if (!(cfg.trunkProvider instanceof SimpleStateProvider)) {
                return;
            }
            Block log = cfg.trunkProvider.getState(new SingleThreadedRandomSource(0), BlockPos.ZERO).getBlock();
            if (TreeHollows.LOG_TO_HOLLOW_LOG.containsKey(log)) {
                ((MutableTreeFeatureConfig) cfg).addDecorator(new TreeHollowTreeDecorator(TreeHollows.LOG_TO_HOLLOW_LOG.get(log)));
            }
        }
    }
}
