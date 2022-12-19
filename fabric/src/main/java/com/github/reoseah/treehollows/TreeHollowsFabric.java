package com.github.reoseah.treehollows;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class TreeHollowsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        List<ItemStack> creativeStacks = new ArrayList<>();
        Platform.instance = new Platform() {
            @Override
            public <T extends Block> T register(String name, T block) {
                return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("treehollows", name), block);
            }

            @Override
            public <T extends Item> T register(String name, T item) {
                creativeStacks.add(new ItemStack(item));
                return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("treehollows", name), item);
            }

            @Override
            public <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
                BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory::apply, blocks).build();
                return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation("treehollows", name), type);
            }

            @Override
            public <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Codec<T> codec) {
                TreeDecoratorType<T> type = new TreeDecoratorType<>(codec);
                return Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, new ResourceLocation("treehollows", name), type);
            }
        };
        TreeHollows.initialize();

        insertTreeHollowsGeneration();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(content -> {
            content.acceptAll(creativeStacks);
        });
    }

    private static void insertTreeHollowsGeneration() {
        DynamicRegistrySetupCallback.EVENT.register(registryView -> {
            Optional<Registry<ConfiguredFeature<?, ?>>> optional = registryView.getOptional(Registries.CONFIGURED_FEATURE);
            if (optional.isPresent()) {
                Registry<ConfiguredFeature<?, ?>> configuredFeatures = optional.get();
                configuredFeatures.forEach(TreeHollows::tryInsertTreeDecorator);

                RegistryEntryAddedCallback.event(configuredFeatures).register((rawId, id, feature) -> {
                    TreeHollows.tryInsertTreeDecorator(feature);
                });
            }
        });
    }
}
