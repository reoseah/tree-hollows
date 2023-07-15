package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.TreeHollows;
import com.github.reoseah.treehollows.impl.TreeHollowDecorator;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
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
import java.util.Optional;

@Mixin(TreeConfiguration.class)
public class TreeConfigurationMixin {

    @Mutable
    @Shadow
    @Final
    public List<TreeDecorator> decorators;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onConstructor(BlockStateProvider trunkProvider,
                               TrunkPlacer trunkPlacer,
                               BlockStateProvider foliageProvider,
                               FoliagePlacer foliagePlacer,
                               Optional<RootPlacer> rootPlacer,
                               BlockStateProvider dirtProvider,
                               FeatureSize featureSize,
                               List<TreeDecorator> decorators,
                               boolean ignoreVines,
                               boolean placeDirt,
                               CallbackInfo ci) {
        // skip tree if it already has tree hollow
        for (TreeDecorator decorator : this.decorators) {
            if (decorator instanceof TreeHollowDecorator) {
                return;
            }
        }
        // or if it has complicated logs
        if (!(trunkProvider instanceof SimpleStateProvider)) {
            return;
        }
        Block log = trunkProvider.getState(new SingleThreadedRandomSource(0), BlockPos.ZERO).getBlock();
        if (!TreeHollows.LOG_TO_HOLLOW_LOG.containsKey(log)) {
            return;
        }
        Block block = TreeHollows.LOG_TO_HOLLOW_LOG.get(log);
        TreeDecorator decorator = new TreeHollowDecorator(block);

        this.decorators = ImmutableList.<TreeDecorator>builder()
                .addAll(this.decorators)
                .add(decorator)
                .build();
    }
}
