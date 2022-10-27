package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.mixined.MutableTreeFeatureConfig;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

@Mixin(TreeConfiguration.class)
public class TreeFeatureConfigMixin implements MutableTreeFeatureConfig {
    @Shadow
    @Final
    @Mutable
    public List<TreeDecorator> decorators;

    @Override
    public void addDecorator(TreeDecorator decorator) {
        this.decorators = new ImmutableList.Builder<TreeDecorator>().addAll(this.decorators).add(decorator).build();
    }
}
