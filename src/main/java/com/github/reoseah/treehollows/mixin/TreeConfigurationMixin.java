package com.github.reoseah.treehollows.mixin;

import com.github.reoseah.treehollows.impl.MutableTreeConfiguration;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

@Mixin(TreeConfiguration.class)
public class TreeConfigurationMixin implements MutableTreeConfiguration {
    @Shadow
    @Final
    @Mutable
    public List<TreeDecorator> decorators;
    @Shadow
    @Final
    public BlockStateProvider trunkProvider;

    @Override
    public void addDecorator(TreeDecorator decorator) {
        this.decorators = new ImmutableList.Builder<TreeDecorator>().addAll(this.decorators).add(decorator).build();
    }

    @Inject(at = @At("HEAD"), method = "build")
    public void build(CallbackInfoReturnable<TreeConfiguration> callback) {
        System.out.println("building " + this.trunkProvider + " tree");
    }
}
