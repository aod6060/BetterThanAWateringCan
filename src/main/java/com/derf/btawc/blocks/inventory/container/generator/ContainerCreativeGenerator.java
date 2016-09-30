package com.derf.btawc.blocks.inventory.container.generator;

import com.derf.btawc.blocks.inventory.container.ContainerBasic;
import com.derf.btawc.blocks.tileentity.generators.TileEntityCreativeGenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCreativeGenerator extends ContainerBasic {

	// Stuff for the container...
	private final InventoryPlayer player;
	private TileEntityCreativeGenerator generator;
	private int currentEnergy;
	private int maxEnergy;
	private int maxExtract;
	private int maxReceive;
	
	public ContainerCreativeGenerator(InventoryPlayer player, TileEntityCreativeGenerator generator) {
		this.player = player;
		this.generator = generator;
		// Create Player Inventory [8, 92], [8, 150]
		this.createPlayerInventory(player, 8, 92, 8, 150);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	@Override
	public void addCraftingToCrafters(ICrafting handler) {
		// TODO Auto-generated method stub
		super.addCraftingToCrafters(handler);
		handler.sendProgressBarUpdate(this, 0, this.generator.getEnergyStored(ForgeDirection.UP));
		handler.sendProgressBarUpdate(this, 1, this.generator.getMaxEnergyStored(ForgeDirection.UP));
		handler.sendProgressBarUpdate(this, 2, this.generator.getStorage().getMaxReceive());
		handler.sendProgressBarUpdate(this, 3, this.generator.getStorage().getMaxExtract());
	}
	*/
	@Override
	public void detectAndSendChanges() {
		// TODO Auto-generated method stub
		super.detectAndSendChanges();
		/*
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting handler = (ICrafting)this.crafters.get(i);
			if(this.currentEnergy != this.generator.getEnergyStored(ForgeDirection.UP)) {
				handler.sendProgressBarUpdate(this, 0, this.generator.getEnergyStored(ForgeDirection.UP));
			}
			
			if(this.maxEnergy != this.generator.getMaxEnergyStored(ForgeDirection.UP)) {
				handler.sendProgressBarUpdate(this, 1, this.generator.getMaxEnergyStored(ForgeDirection.UP));
			}
			
			if(this.maxReceive != this.generator.getStorage().getMaxReceive()) {
				handler.sendProgressBarUpdate(this, 2, this.generator.getStorage().getMaxReceive());
			}
			
			if(this.maxExtract != this.generator.getStorage().getMaxExtract()) {
				handler.sendProgressBarUpdate(this, 3, this.generator.getStorage().getMaxExtract());
			}
		}
		*/
		/*
		this.currentEnergy = this.generator.getEnergyStored(ForgeDirection.UP);
		this.maxEnergy = this.generator.getMaxEnergyStored(ForgeDirection.UP);
		*/
		this.maxReceive = this.generator.getStorage().getMaxReceive();
		this.maxExtract = this.generator.getStorage().getMaxExtract();
	}
	
	
	@Override
	public void updateProgressBar(int index, int value) {
		// TODO Auto-generated method stub
		super.updateProgressBar(index, value);
		switch(index) {
		case 0:
			this.generator.getStorage().setEnergy(value);
			break;
		case 1:
			this.generator.getStorage().setCapacity(value);
			break;
		case 2:
			this.generator.getStorage().setMaxReceive(value);
			break;
		case 3:
			this.generator.getStorage().setMaxExtract(value);
			break;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(this.isPlayerHiddenInventory(index)) {
				if(!this.mergeItemStack(stack1, 27, 36, false)) {
					return null;
				}
			} else if(this.isPlayerInventory(index)) {
				if(!this.mergeItemStack(stack1, 0, 27, false)) {
					return null;
				}
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
	 * Slots 0 - 26
	 * @param this index of the slot
	 * @return boolean
	 */
	private boolean isPlayerHiddenInventory(int index) {
		return index >= 0 && index < 27;
	}
	
	/**
	 * slot 27 - 35
	 * @param this index of the slot
	 * @return boolean
	 */
	private boolean isPlayerInventory(int index) {
		return index  >= 27 && index < 36;
	}
}
