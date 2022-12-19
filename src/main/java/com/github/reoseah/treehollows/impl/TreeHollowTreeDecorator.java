package com.github.reoseah.treehollows.impl;

import com.github.reoseah.treehollows.TreeHollows;
import com.github.reoseah.treehollows.TreeHollowsConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class TreeHollowTreeDecorator extends TreeDecorator {
    public static final Codec<TreeHollowTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(config -> config.block)).apply(instance, TreeHollowTreeDecorator::new));

    protected final Block block;

    public TreeHollowTreeDecorator(Block block) {
        this.block = block;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeHollows.TREE_DECORATOR_TYPE;
    }

    @Override
    public void place(Context ctx) {
        LevelSimulatedReader world = ctx.level();
        RandomSource random = ctx.random();

        boolean isWorldGen = world instanceof WorldGenRegion;
        if (random.nextFloat() <= (isWorldGen ? TreeHollowsConfig.getWorldGenerationChance() : TreeHollowsConfig.getGrowthChance())) {
            int height = 2 + random.nextInt(1);
            if (ctx.logs().size() <= height) {
                return;
            }
            // log positions are sorted by Y coordinate
            BlockPos pos = ctx.logs().get(height);
            Direction facing = Direction.from2DDataValue(random.nextInt(4));

            ctx.setBlock(pos, this.block.defaultBlockState().setValue(TreeHollowBlock.FACING, facing));

            if (isWorldGen) {
                RandomizableContainerBlockEntity.setLootTable((WorldGenRegion) world, random, pos, getTreeHollowLootTable(this.block));
            }
        }
    }

    private static ResourceLocation getTreeHollowLootTable(Block log) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(log);
        return new ResourceLocation(blockId.getNamespace(), "tree_hollows/" + blockId.getPath());
    }
}
