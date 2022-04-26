package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.ExtendedTreeFeatureConfig;
import com.github.reoseah.treehollows.TreeHollowTreeDecorator;
import com.github.reoseah.treehollows.TreeHollows;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(TreeFeatureConfig.class)
public class TreeFeatureConfigMixin implements ExtendedTreeFeatureConfig {
    @Shadow
    @Final
    @Mutable
    public List<TreeDecorator> decorators;

    @Override
    public void addDecorator(TreeDecorator decorator) {
        this.decorators = new ImmutableList.Builder<TreeDecorator>().addAll(this.decorators).add(decorator).build();
    }
}
