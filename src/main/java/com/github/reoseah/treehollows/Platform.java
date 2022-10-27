package com.github.reoseah.treehollows;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.BiFunction;

public abstract class Platform {
    public static Platform instance;

    public abstract <T extends Block> T register(String name, T block);

    public abstract <T extends Item> T register(String name, T item);

    public abstract <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> factory, Block... blocks);

    public abstract <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Codec<T> codec);
}
