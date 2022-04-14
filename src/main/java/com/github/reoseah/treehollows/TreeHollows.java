package com.github.reoseah.treehollows;

import com.github.reoseah.treehollows.block.TreeHollowBlock;
import com.github.reoseah.treehollows.block.entity.TreeHollowBlockEntity;
import com.github.reoseah.treehollows.world.TreeHollowTreeDecorator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod(TreeHollows.MOD_ID)
public class TreeHollows {
    public static final String MOD_ID = "treehollows";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation LOOT_TABLE_ID = new ResourceLocation(MOD_ID, "chests/tree_hollow");

    public TreeHollows() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TreeHollowsConfig.COMMON_CONFIG);

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        TREE_DECORATOR_TYPES.register(bus);
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, MOD_ID);

    public static final RegistryObject<Block> OAK_HOLLOW = registerBlock("oak_hollow", () -> new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> SPRUCE_HOLLOW = registerBlock("spruce_hollow", () -> new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> BIRCH_HOLLOW = registerBlock("birch_hollow", () -> new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.QUARTZ).strength(2.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> JUNGLE_HOLLOW = registerBlock("jungle_hollow", () -> new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.DIRT).strength(2.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> ACACIA_HOLLOW = registerBlock("acacia_hollow", () -> new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> DARK_OAK_HOLLOW = registerBlock("dark_oak_hollow", () -> new TreeHollowBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0f).sound(SoundType.WOOD)));

    public static final Map<Block, Block> TREE_HOLLOWS_MAP = new HashMap<>();

    private static void fillMap() {
        TREE_HOLLOWS_MAP.put(Blocks.OAK_LOG, TreeHollows.OAK_HOLLOW.get());
        TREE_HOLLOWS_MAP.put(Blocks.SPRUCE_LOG, TreeHollows.SPRUCE_HOLLOW.get());
        TREE_HOLLOWS_MAP.put(Blocks.BIRCH_LOG, TreeHollows.BIRCH_HOLLOW.get());
        TREE_HOLLOWS_MAP.put(Blocks.JUNGLE_LOG, TreeHollows.JUNGLE_HOLLOW.get());
        TREE_HOLLOWS_MAP.put(Blocks.ACACIA_LOG, TreeHollows.ACACIA_HOLLOW.get());
        TREE_HOLLOWS_MAP.put(Blocks.DARK_OAK_LOG, TreeHollows.DARK_OAK_HOLLOW.get());
    }

    public static final RegistryObject<BlockEntityType<TreeHollowBlockEntity>> BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("tree_hollow", () -> BlockEntityType.Builder.of(TreeHollowBlockEntity::new, TREE_HOLLOWS_MAP.values().toArray(new Block[0])).build(null));

    public static final TreeDecoratorType<TreeHollowTreeDecorator> TREE_DECORATOR_TYPE = new TreeDecoratorType<>(TreeHollowTreeDecorator.CODEC);

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        return toReturn;
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(TreeHollows::fillMap);
    }
}
