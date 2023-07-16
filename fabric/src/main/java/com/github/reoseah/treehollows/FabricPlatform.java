package com.github.reoseah.treehollows;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class FabricPlatform extends Platform {
    public List<ItemStack> creativeStacks = new ArrayList<>();

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
}
