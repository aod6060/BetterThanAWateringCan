package com.derf.btawc.blocks.inventory.container;

import com.derf.btawc.blocks.tileentity.TileEntityAlloyFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAlloyFurnace extends ContainerBasic {

	private static final int COOKING_SPEED = 400;
	
	// Stuff for the container...
	private final InventoryPlayer player;
	private TileEntityAlloyFurnace furnace;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;
	
	public ContainerAlloyFurnace(InventoryPlayer player, TileEntityAlloyFurnace furnace) {
		// Set Instance Variables
		this.player = player;
		this.furnace = furnace;
		// Create Input Slots 0-3
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 2; x++) {
				this.addSlotToContainer(new Slot(furnace, y * 2 + x, x * 18 + 16, y * 18 + 16));
			}
		}
		// Create Output Slots 4 [139, 22]
		this.addSlotToContainer(new Slot(furnace, 4, 139, 21) {
			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return false;
			}
			
		});
		// Create Fuel Slots 5 [26, 69]
		this.addSlotToContainer(new Slot(furnace, 5, 26, 69));
		// Create Player Inventory 9 - 35 [8, 92] delta 18
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(player, (y*9+x) + 9, x * 18 + 8, y * 18 + 92));
			}
		}
		// Create Player Inventory 0 - 8 [8, 150] delta 18
		for(int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(player, x, x * 18 + 8, 150));
		}
		
	}
	
	
	@Override
	public void addCraftingToCrafters(ICrafting handler) {
		super.addCraftingToCrafters(handler);
		handler.sendProgressBarUpdate(this, 0, this.furnace.cookTime);
		handler.sendProgressBarUpdate(this, 1, this.furnace.burnTime);
		handler.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
	}

	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting handler = (ICrafting)this.crafters.get(i);
			if(this.lastCookTime != this.furnace.cookTime) {
				handler.sendProgressBarUpdate(this, 0, this.furnace.cookTime);
			}
			if(this.lastBurnTime != this.furnace.burnTime) {
				handler.sendProgressBarUpdate(this, 1, this.furnace.burnTime);
			}
			if(this.lastItemBurnTime != this.furnace.currentItemBurnTime) {
				handler.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
			}
		}
		this.lastCookTime = this.furnace.cookTime;
		this.lastBurnTime = this.furnace.burnTime;
		this.lastItemBurnTime = this.furnace.currentItemBurnTime;
	}
	
	
	@Override
	public void updateProgressBar(int index, int value) {
		super.updateProgressBar(index, value);
		switch(index) {
		case 0:
			this.furnace.cookTime = value;
			break;
		case 1:
			this.furnace.burnTime = value;
			break;
		case 2:
			this.furnace.currentItemBurnTime = value;
			break;
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return furnace.isUseableByPlayer(player);
	}


	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(this.isOutput(index)) {
				if(!this.mergeItemStack(stack1, 6, 42, true)) {
					return null;
				}
				
				slot.onSlotChange(stack1, stack);
			} else if(!this.isFuel(index) && !this.isInput(index)) {
				if(TileEntityAlloyFurnace.isItemFuel(stack1)) {
					if(!this.mergeItemStack(stack1, 5, 6, true)) {
						return null;
					}
				} else if(this.isPlayerHiddenInventory(index)) {
					if(!this.mergeItemStack(stack1, 33, 42, false)) {
						return null;
					}
				} else if(this.isPlayerInventory(index)) {
					if(!this.mergeItemStack(stack1, 6, 33, false)) {
						return null;
					}
				} else {
					if(!this.mergeItemStack(stack1, 0, 4, false)) {
						return null;
					}
				}
			} else if(!this.mergeItemStack(stack1, 6, 42, false)) {
				return null;
			}
			
			if(stack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			
			if(stack1.stackSize == stack.stackSize) {
				return null;
			}
			
			slot.onPickupFromSlot(player, stack1);
		}
		return stack;
	}
	
	/**
	 * slot 0 - 3
	 * @param index
	 * @return boolean
	 */
	private boolean isInput(int index) {
		return index >= 0 && index < 4;
	}
	
	/**
	 * slot 4
	 * @param index
	 * @return boolean
	 */
	private boolean isOutput(int index) {
		return index == 4;
	}
	
	/**
	 * slot 5
	 * @param index
	 * @return boolean
	 */
	private boolean isFuel(int index) {
		return index == 5;
	}
	
	/**
	 * Slots 6 - 31
	 * @param this index of the slot
	 * @return boolean
	 */
	private boolean isPlayerHiddenInventory(int index) {
		return index >= 6 && index < 33;
	}
	
	/**
	 * slot 33 - 41
	 * @param this index of the slot
	 * @return boolean
	 */
	private boolean isPlayerInventory(int index) {
		return index  >= 33 && index < 42;
	}
}