package com.github.reoseah.treehollows.impl;

import com.github.reoseah.treehollows.TreeHollows;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TreeHollowBlockEntity extends RandomizableContainerBlockEntity {
    protected final NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);

    public TreeHollowBlockEntity(BlockPos pos, BlockState state) {
        super(TreeHollows.BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        for (int i = 0; i < this.stacks.size(); i++) {
            this.setItem(i, list.get(i));
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.treehollows.tree_hollow");
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new DispenserMenu(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.stacks.clear();
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.stacks);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.stacks);
        }
    }
}
