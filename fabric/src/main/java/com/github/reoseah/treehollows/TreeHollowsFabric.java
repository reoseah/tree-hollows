package com.github.reoseah.treehollows;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.BiFunction;

public class TreeHollowsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Platform.instance = new Platform() {
            @Override
            public <T extends Block> T register(String name, T block) {
                return Registry.register(Registry.BLOCK, new ResourceLocation("treehollows", name), block);
            }

            @Override
            public <T extends Item> T register(String name, T item) {
                return Registry.register(Registry.ITEM, new ResourceLocation("treehollows", name), item);
            }

            @Override
            public <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
                BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory::apply, blocks).build();
                return Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation("treehollows", name), type);
            }

            @Override
            public <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Codec<T> codec) {
                TreeDecoratorType<T> type = new TreeDecoratorType<>(codec);
                return Registry.register(Registry.TREE_DECORATOR_TYPES, new ResourceLocation("treehollows", name), type);
            }
        };
        TreeHollows.initialize();

        insertTreeHollowsGeneration();
    }

    private static void insertTreeHollowsGeneration() {
        for (ConfiguredFeature<?, ?> feature : BuiltinRegistries.CONFIGURED_FEATURE) {
            TreeHollows.tryInsertTreeDecorator(feature);
        }
        RegistryEntryAddedCallback.event(BuiltinRegistries.CONFIGURED_FEATURE).register((rawId, id, feature) -> {
            TreeHollows.tryInsertTreeDecorator(feature);
        });
        DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
            Registry<ConfiguredFeature<?, ?>> registry = registryManager.ownedRegistryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            RegistryEntryAddedCallback.event(registry).register((rawId, id, feature) -> {
                TreeHollows.tryInsertTreeDecorator(feature);
            });
        });
    }
}
