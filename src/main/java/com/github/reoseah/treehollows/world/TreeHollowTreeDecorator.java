package com.github.reoseah.treehollows.world;

import com.github.reoseah.treehollows.TreeHollows;
import com.github.reoseah.treehollows.block.TreeHollowBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class TreeHollowTreeDecorator extends TreeDecorator {
    public static final Codec<TreeHollowTreeDecorator> CODEC = RecordCodecBuilder.create( //
            instance -> instance.group( //
                            Registry.BLOCK.byNameCodec().fieldOf("block").forGetter(config -> config.block),
                            Codec.floatRange(0.0f, 1.0f).fieldOf("world_generation_chance").forGetter(config -> config.worldGenChance),
                            Codec.floatRange(0.0f, 1.0f).fieldOf("growth_chance").forGetter(config -> config.worldGenChance))
                    .apply(instance, TreeHollowTreeDecorator::new));

    protected final Block block;
    protected final float worldGenChance;
    protected final float growthChance;

    public TreeHollowTreeDecorator(Block block, float worldGenChance, float growthChance) {
        this.block = block;
        this.worldGenChance = worldGenChance;
        this.growthChance = growthChance;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeHollows.TREE_DECORATOR_TYPE;
    }

    @Override
    public void place(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, List<BlockPos> pLogPositions, List<BlockPos> pLeafPositions) {
        boolean isWorldGen = pLevel instanceof WorldGenRegion;
        if (pRandom.nextFloat() <= (isWorldGen ? this.worldGenChance : this.growthChance)) {
            int height = 2 + pRandom.nextInt(1);
            if (pLogPositions.size() <= height) {
                return;
            }
            // log positions are sorted by Y coordinate
            BlockPos pos = pLogPositions.get(height);
            Direction facing = Direction.from2DDataValue(pRandom.nextInt(4));

            pBlockSetter.accept(pos, this.block.defaultBlockState().setValue(TreeHollowBlock.FACING, facing));

            if (isWorldGen) {
                RandomizableContainerBlockEntity.setLootTable((WorldGenRegion) pLevel, pRandom, pos, TreeHollows.LOOT_TABLE_ID);
            }
        }
    }
}
