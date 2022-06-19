package com.github.reoseah.treehollows;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class TreeHollowTreeDecorator extends TreeDecorator {
    public static final Codec<TreeHollowTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Registry.BLOCK.getCodec().fieldOf("block").forGetter(config -> config.block)).apply(instance, TreeHollowTreeDecorator::new));

    protected final Block block;

    public TreeHollowTreeDecorator(Block block) {
        this.block = block;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return TreeHollows.TREE_DECORATOR_TYPE;
    }

    @Override
    public void generate(Generator generator) {
        TestableWorld world = generator.getWorld();
        Random random = generator.getRandom();

        boolean isWorldGen = world instanceof ChunkRegion;
        if (random.nextFloat() <= (isWorldGen ? TreeHollows.config.getWorldGenerationChance() : TreeHollows.config.getGrowthChance())) {
            int height = 2 + random.nextInt(1);
            if (generator.getLogPositions().size() <= height) {
                return;
            }
            // log positions are sorted by Y coordinate
            BlockPos pos = generator.getLogPositions().get(height);
            Direction facing = Direction.fromHorizontal(random.nextInt(4));

            generator.replace(pos, this.block.getDefaultState().with(TreeHollowBlock.FACING, facing));

            if (isWorldGen) {
                LootableContainerBlockEntity.setLootTable((ChunkRegion) world, random, pos, TreeHollows.LOOT_TABLE_ID);
            }
        }
    }
}
