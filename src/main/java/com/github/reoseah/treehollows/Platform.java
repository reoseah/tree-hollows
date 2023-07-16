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
    public static final Platform instance = createInstance();

    static Platform createInstance() {
        try {
            return (Platform) Platform.class.getClassLoader() //
                    .loadClass("com.github.reoseah.treehollows.FabricPlatform") //
                    .getDeclaredConstructor() //
                    .newInstance();
        } catch (Exception nofabric) {
            try {
                return (Platform) Platform.class.getClassLoader() //
                        .loadClass("com.github.reoseah.treehollows.ForgePlatform") //
                        .getDeclaredConstructor() //
                        .newInstance();
            } catch (Exception noforge) {
                RuntimeException error = new RuntimeException("Couldn't find loader-specific implementation");
                error.addSuppressed(nofabric);
                error.addSuppressed(noforge);
                throw error;
            }
        }
    }

    public abstract <T extends Block> T register(String name, T block);

    public abstract <T extends Item> T register(String name, T item);

    public abstract <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> factory, Block... blocks);

    public abstract <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Codec<T> codec);
}
