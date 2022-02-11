package com.github.reoseah.treehollows;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class TreeHollowBlockEntity extends LootableContainerBlockEntity {
	protected final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(9, ItemStack.EMPTY);

	public TreeHollowBlockEntity(BlockPos pos, BlockState state) {
		super(TreeHollows.BLOCK_ENTITY_TYPE, pos, state);
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.stacks;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		for (int i = 0; i < this.stacks.size(); i++) {
			this.setStack(i, list.get(i));
		}
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText(this.getCachedState().getBlock().getTranslationKey());
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public int size() {
		return 9;
	}
}
