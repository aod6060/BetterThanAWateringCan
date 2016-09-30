package com.derf.btawc.blocks.inventory.container.furnace;

import com.derf.btawc.blocks.inventory.container.ContainerBasic;
import com.derf.btawc.blocks.tileentity.furnace.TileEntitySuperFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerSuperFurnace extends ContainerBasic {
	private final InventoryPlayer player;
	private static final int COOKING_SPEED = 200;
	
	private TileEntitySuperFurnace superFurnace;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;
	
	public ContainerSuperFurnace(InventoryPlayer player, TileEntitySuperFurnace superFurnace) {
		this.superFurnace = superFurnace;
		this.player = player;
		
		// Create Input Slots for Super Furnace
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				this.addSlotToContainer(new Slot(superFurnace, y * 3 + x, x * 18 + 8, y * 18 + 16));
			}
		}
		// Create Output Slots for Super Furnace
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				this.addSlotToContainer(new Slot(superFurnace, (y*3+x)+9, x * 18 + 114, y * 18 + 16) {
					@Override
					public boolean isItemValid(ItemStack p_75214_1_) {
						return false;
					}
					
				});
			}
		}
		// Create Fuel Slot for Super Furnace
		this.addSlotToContainer(new Slot(superFurnace, 18, 26, 88));
		// Create Player Inventory
		this.createPlayerInventory(player, 6, 124, 6, 182);
	}

	/*
	@Override
	public void addCraftingToCrafters(ICrafting handler) {
		super.addCraftingToCrafters(handler);
		handler.sendProgressBarUpdate(this, 0, this.superFurnace.furnaceCookTime);
		handler.sendProgressBarUpdate(this, 1, this.superFurnace.furnaceBurnTime);
		handler.sendProgressBarUpdate(this, 2, this.superFurnace.currentItemBurnTime);
	}
	*/

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		/*
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting handler = (ICrafting) this.crafters.get(i);
			
			if(this.lastCookTime != this.superFurnace.furnaceCookTime) {
				handler.sendProgressBarUpdate(this, 0, this.superFurnace.furnaceCookTime);
			}
			
			if(this.lastBurnTime != this.superFurnace.furnaceBurnTime) {
				handler.sendProgressBarUpdate(this, 1, this.superFurnace.furnaceBurnTime);
			}
			
			if(this.lastItemBurnTime != this.superFurnace.currentItemBurnTime) {
				handler.sendProgressBarUpdate(this, 2, this.superFurnace.currentItemBurnTime);
			}
		}
		*/
		this.lastCookTime = this.superFurnace.furnaceCookTime;
		this.lastBurnTime = this.superFurnace.furnaceBurnTime;
		this.lastItemBurnTime = this.superFurnace.currentItemBurnTime;
	}


	@Override
	public void updateProgressBar(int index, int value) {
		switch(index) {
		case 0:
			this.superFurnace.furnaceCookTime = value;
			break;
		case 1:
			this.superFurnace.furnaceBurnTime = value;
			break;
		case 2:
			this.superFurnace.currentItemBurnTime = value;
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return superFurnace.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(this.isOutput(index)) {
				if(!this.mergeItemStack(stack1, 19, 55, true)) {
					return null;
				}
				
				slot.onSlotChange(stack1,  stack);
			} else if(!this.isFuel(index) && !this.isInput(index)) {
				if(FurnaceRecipes.instance().getSmeltingResult(stack1) != null) {
					if(!this.mergeItemStack(stack1, 0, 9, false)) {
						return null;
					}
				} else if(TileEntityFurnace.isItemFuel(stack1)) {
					if(!this.mergeItemStack(stack1, 18, 19, false)) {
						return null;
					}
				} else if(index >= 19 && index < 46) {
					if(!this.mergeItemStack(stack1, 46, 55, false)) {
						return null;
					}
				} else if(index >= 46 && index < 55) {
					if(!this.mergeItemStack(stack1, 19, 46, false)) {
						return null;
					}
				}
			} else if(!this.mergeItemStack(stack1, 19, 55, false)) {
				return null;
			}
			
			if(stack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
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
	
	private boolean isInput(int index) {
		return index >= 0 && index < 9;
	}
	
	private boolean isOutput(int index) {
		return index >= 9 && index < 18;
	}
	
	private boolean isFuel(int index) {
		return index == 18;
	}
}
