package com.derf.btawc.blocks.tileentity;

import com.derf.btawc.blocks.BlockAlloyFurnace;
import com.derf.btawc.recipe.AlloyRecipeManager;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class TileEntityAlloyFurnace extends TileEntityBasic implements IInventory{
	// Static Stuff
	public static final int COOKING_SPEED = 400;
	public static final int INPUT_1 = 0;
	public static final int INPUT_2 = 1;
	public static final int INPUT_3 = 2;
	public static final int INPUT_4 = 3;
	public static final int OUTPUT = 4;
	public static final int FUEL_SLOT = 5;
	
	// Alloy Furnace Inventory
	// 0-3 Input, 4 Output and 5 Fuel Slot
	private ItemStack[] inventory = new ItemStack[6];
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	private String name;
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList list = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.inventory = new ItemStack[this.getSizeInventory()];
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound comp = list.getCompoundTagAt(i);
			int index = comp.getInteger("Slot");
			if(index >= 0 && index < this.getSizeInventory()) {
				this.inventory[index] = ItemStack.loadItemStackFromNBT(comp);
			}
		}
		this.burnTime = tag.getInteger("BurnTime");
		this.cookTime = tag.getInteger("CookTime");
		this.currentItemBurnTime = this.getItemBurnTime(this.inventory[FUEL_SLOT]);
		if(tag.hasKey("CustomName")) {
			this.name = tag.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("BurnTime", this.burnTime);
		tag.setInteger("CookTime", this.cookTime);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < this.inventory.length; i++) {
			if(this.inventory[i] != null) {
				NBTTagCompound comp = new NBTTagCompound();
				comp.setInteger("Slot", i);
				inventory[i].writeToNBT(comp);
				list.appendTag(comp);
			}
		}
		tag.setTag("Items", list);
		if(this.hasCustomInventoryName()) {
			tag.setString("CustomName", this.name);
		}
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
	public ItemStack decrStackSize(int slot, int amount) {
		if(this.inventory[slot] != null) {
			ItemStack stack = null;
			if(this.inventory[slot].stackSize <= amount) {
				stack = this.inventory[slot];
				this.inventory[slot] = null;
				return stack;
			} else {
				stack = this.inventory[slot].splitStack(amount);
				if(this.inventory[slot].stackSize == 0) {
					this.inventory[slot] = null;
				}
				return stack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(this.inventory[slot] != null) {
			ItemStack stack = this.inventory[slot];
			this.inventory[slot] = null;
			return stack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
		
		if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName()? this.name : "container.alloy_furnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.name != null && this.name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this? false : player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// 0-3 input, 4 output, fuel 5
		boolean b = false;
		
		switch(slot) {
		case 0:
		case 1:
		case 2:
		case 3:
			b = true;
			break;
		case 4:
			b = false;
			break;
		case FUEL_SLOT:
			b = isItemFuel(stack);
			break;
		}
		
		return b;
	}
	
	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}
	
	public static int getItemBurnTime(ItemStack stack) {
		int burnTime = 0;
		
		if(stack == null) {
			burnTime = 0;
		} else {
			Item item = stack.getItem();
			
			if(item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);
				
				if(block == Blocks.wooden_slab) {
					burnTime = 150;
				} else if(block.getMaterial() == Material.wood) {
					burnTime = 300;
				} else if(block == Blocks.coal_block) {
					burnTime = 16000;
				}
			} else if(item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) {
				burnTime = 200;
			} else if(item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) {
				burnTime = 200;
			} else if(item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) {
				burnTime = 200;
			} else if(item == Items.stick) {
				burnTime = 100;
			} else if(item == Items.coal) {
				burnTime = 1600;
			} else if(item == Items.lava_bucket) {
				burnTime = 20000;
			} else if(item == Item.getItemFromBlock(Blocks.sapling)) {
				burnTime = 100;
			} else if(item == Items.blaze_rod) {
				burnTime = 2400;
			} else {
				burnTime = GameRegistry.getFuelValue(stack);
			}
		}
		
		return burnTime;
	}
	
	private boolean canSmelt() {
		// Input is 0 - 3, Output 4
		ItemStack stack = AlloyRecipeManager.getResult(inventory[INPUT_1], inventory[INPUT_2], inventory[INPUT_3], inventory[INPUT_4]);
		
		if(stack == null) {
			return false;
		}
		
		if(this.inventory[OUTPUT] == null) {
			return true;
		}
		
		if(!this.inventory[OUTPUT].isItemEqual(stack)) {
			return false;
		}
		
		int result = inventory[OUTPUT].stackSize + stack.stackSize;
		return result <= this.getInventoryStackLimit() && result <= this.inventory[OUTPUT].getMaxStackSize();
	}
	
	private void smeltItem() {
		if(this.canSmelt()) {
			ItemStack stack = AlloyRecipeManager.getResult(inventory[INPUT_1], inventory[INPUT_2], inventory[INPUT_3], inventory[INPUT_4]);
			
			if(this.inventory[OUTPUT] == null) {
				this.inventory[OUTPUT] = stack.copy();
			} else if(this.inventory[OUTPUT].getItem() == stack.getItem()) {
				this.inventory[OUTPUT].stackSize += stack.stackSize;
			}
			
			this.updateSlot(INPUT_1);
			this.updateSlot(INPUT_2);
			this.updateSlot(INPUT_3);
			this.updateSlot(INPUT_4);
		}
	}
	
	private void updateSlot(int slot) {
		if(this.inventory[slot] != null) {
			--this.inventory[slot].stackSize;
			if(this.inventory[slot].stackSize <= 0) {
				this.inventory[slot] = null;
			}
		}
	}
	
	private boolean checkIfInputAreAllNull() {
		boolean b = false;
		for(int i = 0; i < 4; i++) {
			b = b || this.inventory[i] != null;
		}
		return b;
	}
	
	@Override
	public void updateEntity() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;
		
		// Decrement Burning time...
		if(this.burnTime > 0) {
			--this.burnTime;
		}
		
		if(!this.worldObj.isRemote) {
			// Using Fuel
			if(this.burnTime != 0 || this.inventory[FUEL_SLOT] != null && this.checkIfInputAreAllNull()) {
				if(this.burnTime == 0 && this.canSmelt()) {
					this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.inventory[FUEL_SLOT]);
					if(this.isBurning()) {
						flag1 = true;
						if(this.inventory[FUEL_SLOT] != null) {
							--this.inventory[FUEL_SLOT].stackSize;
							if(this.inventory[FUEL_SLOT].stackSize <= 0) {
								this.inventory[FUEL_SLOT] = this.inventory[FUEL_SLOT].getItem().getContainerItem(this.inventory[FUEL_SLOT]);
							}
						}
					}
				}
				
				if(this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if(this.cookTime >= COOKING_SPEED) {
						this.cookTime = 0;
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			}
			
			if(flag != this.burnTime > 0) {
				flag1 = true;
				BlockAlloyFurnace.updateAlloyFurnaceBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
		
		if(flag1) {
			this.markDirty();
		}
	}
	
	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	public int getBurnTimeRemainingScaled(int scale) {
		if(this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = COOKING_SPEED;
		}
		return this.burnTime * scale / this.currentItemBurnTime;
	}
	
	public int getCookProgressScaled(int scale) {
		return this.cookTime * scale / COOKING_SPEED;
	}
}
