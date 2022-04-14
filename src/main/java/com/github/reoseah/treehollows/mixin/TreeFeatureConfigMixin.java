package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.TreeHollowsConfig;
import com.github.reoseah.treehollows.TreeHollows;
import com.github.reoseah.treehollows.world.TreeHollowTreeDecorator;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(TreeConfiguration.class)
public class TreeFeatureConfigMixin {
    @Shadow
    @Final
    public BlockStateProvider trunkProvider;
    @Shadow
    @Final
    @Mutable
    public List<TreeDecorator> decorators;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void addTreeHollowsDecorators(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, BlockStateProvider dirtProvider, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, boolean forceDirt, CallbackInfo ci) {
        if (this.decorators.stream().anyMatch(decorator -> decorator instanceof TreeHollowTreeDecorator) // skip anything if it already has tree hollow
                || !(this.trunkProvider instanceof SimpleStateProvider)) { // skip anything with fancy logs
            return;
        }

        Block log = this.trunkProvider.getState(new Random(), BlockPos.ZERO).getBlock();
        if (TreeHollows.TREE_HOLLOWS_MAP.containsKey(log)) {
            TreeDecorator treeHollow = new TreeHollowTreeDecorator(TreeHollows.TREE_HOLLOWS_MAP.get(log), TreeHollowsConfig.WORLD_GENERATION_CHANCE.get().floatValue(), TreeHollowsConfig.GROWTH_CHANCE.get().floatValue());
            this.decorators = new ImmutableList.Builder<TreeDecorator>().addAll(this.decorators).add(treeHollow).build();
        }
    }
}
