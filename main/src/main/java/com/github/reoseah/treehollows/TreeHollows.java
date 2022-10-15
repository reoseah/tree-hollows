package com.github.reoseah.treehollows;

import com.google.common.collect.Sets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class TreeHollows implements ModInitializer {
    public static final String MOD_ID = "treehollows";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TreeHollowsConfig config = TreeHollowsConfig.reload();

    public static final Map<Block, Block> TREE_HOLLOWS_MAP = new HashMap<>();

    public static final Block OAK_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0f).sound(SoundType.WOOD));
    public static final Block SPRUCE_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0f).sound(SoundType.WOOD));
    public static final Block BIRCH_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.SAND).strength(2.0f).sound(SoundType.WOOD));
    public static final Block JUNGLE_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.DIRT).strength(2.0f).sound(SoundType.WOOD));
    public static final Block ACACIA_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0f).sound(SoundType.WOOD));
    public static final Block DARK_OAK_HOLLOW = new TreeHollowBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0f).sound(SoundType.WOOD));

    public static final Set<Block> TREE_HOLLOWS = Sets.newHashSet(OAK_HOLLOW, SPRUCE_HOLLOW, BIRCH_HOLLOW, JUNGLE_HOLLOW, ACACIA_HOLLOW, DARK_OAK_HOLLOW);
    public static final BlockEntityType<TreeHollowBlockEntity> BLOCK_ENTITY_TYPE = new BlockEntityType<>(TreeHollowBlockEntity::new, TREE_HOLLOWS, null);

    public static final TreeDecoratorType<TreeHollowTreeDecorator> TREE_DECORATOR_TYPE = new TreeDecoratorType<>(TreeHollowTreeDecorator.CODEC);


    public static <T> void register(Registry<? super T> registry, String name, T entry) {
        Registry.register(registry, new ResourceLocation(MOD_ID, name), entry);
    }

    public static ResourceLocation getLootTableId(Block block) {
        ResourceLocation blockId = Registry.BLOCK.getKey(block);
        return new ResourceLocation(blockId.getNamespace(), "tree_hollows/" + blockId.getPath());
    }

    @Override
    public void onInitialize() {
        register(Registry.BLOCK, "oak_hollow", OAK_HOLLOW);
        register(Registry.BLOCK, "spruce_hollow", SPRUCE_HOLLOW);
        register(Registry.BLOCK, "birch_hollow", BIRCH_HOLLOW);
        register(Registry.BLOCK, "jungle_hollow", JUNGLE_HOLLOW);
        register(Registry.BLOCK, "acacia_hollow", ACACIA_HOLLOW);
        register(Registry.BLOCK, "dark_oak_hollow", DARK_OAK_HOLLOW);

        register(Registry.ITEM, "oak_hollow", new BlockItem(OAK_HOLLOW, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        register(Registry.ITEM, "spruce_hollow", new BlockItem(SPRUCE_HOLLOW, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        register(Registry.ITEM, "birch_hollow", new BlockItem(BIRCH_HOLLOW, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        register(Registry.ITEM, "jungle_hollow", new BlockItem(JUNGLE_HOLLOW, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        register(Registry.ITEM, "acacia_hollow", new BlockItem(ACACIA_HOLLOW, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        register(Registry.ITEM, "dark_oak_hollow", new BlockItem(DARK_OAK_HOLLOW, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

        register(Registry.BLOCK_ENTITY_TYPE, "tree_hollow", BLOCK_ENTITY_TYPE);

        register(Registry.TREE_DECORATOR_TYPES, "tree_hollow", TREE_DECORATOR_TYPE);

        TREE_HOLLOWS_MAP.put(Blocks.OAK_LOG, OAK_HOLLOW);
        TREE_HOLLOWS_MAP.put(Blocks.SPRUCE_LOG, SPRUCE_HOLLOW);
        TREE_HOLLOWS_MAP.put(Blocks.BIRCH_LOG, BIRCH_HOLLOW);
        TREE_HOLLOWS_MAP.put(Blocks.JUNGLE_LOG, JUNGLE_HOLLOW);
        TREE_HOLLOWS_MAP.put(Blocks.ACACIA_LOG, ACACIA_HOLLOW);
        TREE_HOLLOWS_MAP.put(Blocks.DARK_OAK_LOG, DARK_OAK_HOLLOW);

        for (ConfiguredFeature<?, ?> feature : BuiltinRegistries.CONFIGURED_FEATURE) {
            addTreeHollows(feature);
        }
        RegistryEntryAddedCallback.event(BuiltinRegistries.CONFIGURED_FEATURE).register((rawId, id, object) -> {
            addTreeHollows(object);
        });
        DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
            Registry<ConfiguredFeature<?, ?>> registry = registryManager.ownedRegistryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            RegistryEntryAddedCallback.event(registry).register((rawId, id, object) -> {
                addTreeHollows(object);
            });
        });
    }

    private static void addTreeHollows(ConfiguredFeature<?, ?> object) {
        if (object.feature() == Feature.TREE && object.config() instanceof TreeConfiguration cfg) {
            // skip anything if it already has tree hollow
            if (cfg.decorators.stream().anyMatch(decorator -> decorator instanceof TreeHollowTreeDecorator)
                    // and anything with "fancy" logs
                    || !(cfg.trunkProvider instanceof SimpleStateProvider)) {
                return;
            }
            Block log = cfg.trunkProvider.getState(new SingleThreadedRandomSource(0), BlockPos.ZERO).getBlock();
            if (TreeHollows.TREE_HOLLOWS_MAP.containsKey(log)) {
                TreeDecorator decorator = new TreeHollowTreeDecorator(TreeHollows.TREE_HOLLOWS_MAP.get(log));

                ((ExtendedTreeFeatureConfig) cfg).addDecorator(decorator);
            }
        }
    }
}
