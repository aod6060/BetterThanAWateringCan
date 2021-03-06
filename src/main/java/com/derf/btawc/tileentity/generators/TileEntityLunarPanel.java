package com.derf.btawc.tileentity.generators;

import com.derf.btawc.energy.EnergyStorage;
import com.derf.btawc.items.ItemsManager;
import com.derf.btawc.util.InventoryUtils;
import com.derf.btawc.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;

public class TileEntityLunarPanel extends TileEntityGenerator implements IInventory {
	// static stuff
	public static final int LUNAR_PANE_SLOT = 0;
	public static final int SPEED_UPGRADE_SLOT = 1;
	public static final int MAX_SLOT_SIZE = 2;
	public static final int MAX_CAPACITY = 500000;
	// Caculated Insantity
	public static final int INSANTITY_MIN = 1;
	public static final int INSANTITY_MAX = 4096;
	public static float[] moonPhaseBonus = {
		2.0f,
		1.5f,
		1.25f,
		1f,
		0.5f,
		1f,
		1.25f,
		1.5f
	};
	
	// Inventory
	private ItemStack[] inventory = new ItemStack[MAX_SLOT_SIZE];
	// Custom Name
	private String name;
	// Defualt RF/t
	private int energyTicks = 128;
	// Insantity
	private int insantity = 1;
	// Current RF/t
	private int currentEnergyTicks = 0;
	// Efficency
	private int efficency = 1;
	
	public TileEntityLunarPanel() {
		super();
		this.storage = new EnergyStorage(MAX_CAPACITY);
		this.storage.setMaxTransfer(0);
	}
	
	@Override
	public void update() {
		if(!worldObj.isRemote) {
			
			this.efficency = this.getGeneratorEfficency();
			
			if(!this.isLessThanZero()) {
				this.onEnergyUpdate();
				this.getStorage().setMaxTransfer(this.currentEnergyTicks);
			} else {
				if(!this.getStorage().isEmpty()) {
					this.currentEnergyTicks = this.energyTicks * this.caculateSpeedUpgrades();
					this.getStorage().setMaxTransfer(this.currentEnergyTicks);
					this.outputAllSides();
					this.sendToClient();
				}
			}
			
			this.markDirty();
		}
	}

	private boolean isLessThanZero() {
		return 0 == this.getGeneratorEfficency();
	}

	@Override
	protected void caculateRFTicks() {
		int baseRF = this.caculateBaseRF();
		int speedUpgrades = this.caculateSpeedUpgrades();
		this.insantity = this.caculateInsantityFromPanes();
		float lunarPhasesBonus = this.caculateLunarPhasesBonus();
		this.currentEnergyTicks = (int) (baseRF * speedUpgrades * insantity * lunarPhasesBonus);
	}
	private float caculateLunarPhasesBonus() {
		return this.moonPhaseBonus[this.worldObj.getMoonPhase()];
	}

	private int caculateInsantityFromPanes() {
		int temp = 1;
		if(this.getStackInSlot(LUNAR_PANE_SLOT) != null) {
			int stackSize = this.getStackInSlot(LUNAR_PANE_SLOT).stackSize;
			float rep_stackSize = (float) stackSize / 64f;
			int modifier = (int) Utils.lerp(rep_stackSize, INSANTITY_MIN, INSANTITY_MAX);
			temp *= modifier;
		}
		return temp;
	}

	private int caculateSpeedUpgrades() {
		int temp = 1;
		if(this.getStackInSlot(SPEED_UPGRADE_SLOT) != null) {
			for(int i = 0; i < this.getStackInSlot(SPEED_UPGRADE_SLOT).stackSize; i++) {
				temp *= 2;
			}
		}
		return temp;
	}

	private int caculateBaseRF() {
		int efficency = this.getGeneratorEfficency();
		return (int) (energyTicks * ((float)efficency / 5.0f));
	}

	public int getGeneratorEfficency() {
		// Caculate Light Delta
		int delta = Math.abs(worldObj.getLightFor(EnumSkyBlock.SKY, pos) - worldObj.getSkylightSubtracted());
		// Recupricle
		float rep_i = (float)delta / (float)11;
		// Some Probablility matic
		float flip = 1.0f - rep_i;
		// Reset it to be 0 to 11 
		int j = (int) (flip * 11);
		j = -j;
		int value = 5;
		int efficency = value + j;
		efficency = MathHelper.clamp_int(efficency, 0, 5);
		return efficency;
	}
	@Override
	public String getName() {
		return this.hasCustomName()? this.name : "container.lunar_panel";
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean hasCustomName() {
		return this.name != null && !this.name.isEmpty();
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory[index] = stack;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(pos) != this? false : player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= 64.0;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		if(stack != null) {
			if(index == SPEED_UPGRADE_SLOT && stack.getItem() == ItemsManager.speedUpgradeChip) {
				return true;
			} else if(index == LUNAR_PANE_SLOT && stack.getItem() == ItemsManager.lunarPane) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getField(int id) {
		int value = 0;
		switch(id) {
		case 0:
			value = this.efficency;
			break;
		}
		return value;
	}

	@Override
	public void setField(int id, int value) {
		switch(id) {
		case 0:
			this.efficency = value;
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public void clear() {
		this.inventory = new ItemStack[MAX_SLOT_SIZE];
	}

	@Override
	public String printEnergyValue() {
		String s = String.format("[%d/%d] %d RF/t", this.storage.getEnergyStored(), this.storage.getMaxEnergyStored(), this.storage.getMaxTransfer());
		return s;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.currentEnergyTicks = compound.getInteger("CurrentEnergyTicks");
		this.insantity = compound.getInteger("Insantity");
		// Inventory
		InventoryUtils.loadInventory(this, compound);
		// Custom Name
		if(compound.hasKey("CustomName")) {
			this.name = compound.getString("CustomName");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("CurrentEnergyTicks", this.currentEnergyTicks);
		compound.setInteger("Insantity", this.insantity);
		// Inventory
		InventoryUtils.saveInventory(this, compound);
		// Custom Name
		if(this.hasCustomName()) {
			compound.setString("CustomName", this.name);
		}
		return super.writeToNBT(compound);
	}

	public int getEfficency() {
		return this.efficency;
	}
}
