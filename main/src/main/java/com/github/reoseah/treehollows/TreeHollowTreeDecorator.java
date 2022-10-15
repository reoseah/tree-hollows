package com.github.reoseah.treehollows;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class TreeHollowTreeDecorator extends TreeDecorator {
    public static final Codec<TreeHollowTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Registry.BLOCK.byNameCodec().fieldOf("block").forGetter(config -> config.block)).apply(instance, TreeHollowTreeDecorator::new));

    protected final Block block;

    public TreeHollowTreeDecorator(Block block) {
        this.block = block;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeHollows.TREE_DECORATOR_TYPE;
    }

    @Override
    public void place(Context generator) {
        LevelSimulatedReader world = generator.level();
        RandomSource random = generator.random();

        boolean isWorldGen = world instanceof WorldGenRegion;
        if (random.nextFloat() <= (isWorldGen ? TreeHollows.config.getWorldGenerationChance() : TreeHollows.config.getGrowthChance())) {
            int height = 2 + random.nextInt(1);
            if (generator.logs().size() <= height) {
                return;
            }
            // log positions are sorted by Y coordinate
            BlockPos pos = generator.logs().get(height);
            Direction facing = Direction.from2DDataValue(random.nextInt(4));

            generator.setBlock(pos, this.block.defaultBlockState().setValue(TreeHollowBlock.FACING, facing));

            if (isWorldGen) {
                RandomizableContainerBlockEntity.setLootTable((WorldGenRegion) world, random, pos, TreeHollows.getLootTableId(this.block));
            }
        }
    }
}
