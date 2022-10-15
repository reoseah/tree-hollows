package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.ExtendedTreeFeatureConfig;
import com.github.reoseah.treehollows.TreeHollowTreeDecorator;
import com.github.reoseah.treehollows.TreeHollows;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

@Mixin(TreeConfiguration.class)
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
