package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.TreeHollowTreeDecorator;
import com.github.reoseah.treehollows.TreeHollows;
import com.github.reoseah.treehollows.compatibility.ArchitectsPaletteHelper;
import com.google.common.collect.ImmutableList;
import com.slomaxonical.architectspalette.registry.APBlocks;
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
public class TreeFeatureConfigMixin {
    @Shadow
    @Final
    public BlockStateProvider trunkProvider;
    @Shadow
    @Final
    @Mutable
    public List<TreeDecorator> decorators;

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/gen/stateprovider/BlockStateProvider;Lnet/minecraft/world/gen/trunk/TrunkPlacer;Lnet/minecraft/world/gen/stateprovider/BlockStateProvider;Lnet/minecraft/world/gen/foliage/FoliagePlacer;Lnet/minecraft/world/gen/stateprovider/BlockStateProvider;Lnet/minecraft/world/gen/feature/size/FeatureSize;Ljava/util/List;ZZ)V")
    private void addTreeHollowsDecorators(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, BlockStateProvider dirtProvider, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, boolean forceDirt, CallbackInfo ci) {
        if (this.decorators.stream().anyMatch(decorator -> decorator instanceof TreeHollowTreeDecorator) // skip anything if it already has tree hollow
                || !(this.trunkProvider instanceof SimpleBlockStateProvider)) { // skip anything with fancy logs
            return;
        }
        Block log = this.trunkProvider.getBlockState(new Random(), BlockPos.ORIGIN).getBlock();
        if (TreeHollows.TREE_HOLLOWS_MAP.containsKey(log)) {
            TreeDecorator treeHollow = new TreeHollowTreeDecorator(TreeHollows.TREE_HOLLOWS_MAP.get(log), TreeHollows.config.getWorldGenerationChance(), TreeHollows.config.getGrowthChance());
            this.decorators = new ImmutableList.Builder<TreeDecorator>().addAll(this.decorators).add(treeHollow).build();
        } else if (ArchitectsPaletteHelper.isTwistedLog(log)) {
            TreeDecorator treeHollow = new TreeHollowTreeDecorator(TreeHollows.TWISTED_HOLLOW, TreeHollows.config.getWorldGenerationChance(), TreeHollows.config.getGrowthChance());
            this.decorators = new ImmutableList.Builder<TreeDecorator>().addAll(this.decorators).add(treeHollow).build();
        }
    }
}
