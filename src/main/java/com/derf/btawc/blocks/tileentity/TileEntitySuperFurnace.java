package com.derf.btawc.blocks.tileentity;

import com.derf.btawc.blocks.BlockSuperFurnace;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class TileEntitySuperFurnace extends TileEntityBasic implements IInventory {
	
	public static final int FUEL_SLOT = 18;
	public static final int COOKING_SPEED = 200;
	
	// Furnace Inventory
	// 0-8 input, 9-17 output, 18 fuel...
	private ItemStack[] furnaceItemStack = new ItemStack[19];
	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;
	private String name;
	
	@Override
	public int getSizeInventory() {
		return furnaceItemStack.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.furnaceItemStack[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(this.furnaceItemStack[slot] != null) {
			ItemStack stack;
			if(this.furnaceItemStack[slot].stackSize <= amount) {
				stack = this.furnaceItemStack[slot];
				this.furnaceItemStack[slot] = null;
				return stack;
			} else {
				stack = this.furnaceItemStack[slot].splitStack(amount);
				
				if(this.furnaceItemStack[slot].stackSize == 0) {
					this.furnaceItemStack[slot] = null;
				}
				
				return stack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(this.furnaceItemStack[slot] != null) {
			ItemStack stack = this.furnaceItemStack[slot];
			this.furnaceItemStack[slot] = null;
			return stack;
		} else {
			return null;
		}
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.furnaceItemStack[slot] = stack;
		
		if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName()? this.name : "container.super_furnace";
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
		// 0-8 input, 9-17 output, 19 fuel.
		boolean b = false;
		
		switch(slot) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			b = true;
			break;
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
			b = false;
			break;
		case FUEL_SLOT:
			b = isItemFuel(stack);
			break;
		}
		return b;
	}
	
	private boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}

	private int getItemBurnTime(ItemStack stack) {
		// TODO Auto-generated method stub
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

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.readFromNBT(tag);
		NBTTagList list = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.furnaceItemStack = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound comp = list.getCompoundTagAt(i);
			int index = comp.getInteger("Slot");
			if(index >= 0 && index < this.getSizeInventory()) {
				this.furnaceItemStack[index] = ItemStack.loadItemStackFromNBT(comp);
			}
		}
		this.furnaceBurnTime = tag.getInteger("BurnTime");
		this.furnaceCookTime = tag.getInteger("CookTime");
		this.currentItemBurnTime = this.getItemBurnTime(this.furnaceItemStack[FUEL_SLOT]);
		
		if(tag.hasKey("CustomName")) {
			this.name = tag.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.writeToNBT(tag);
		tag.setInteger("BurnTime", this.furnaceBurnTime);
		tag.setInteger("CookTime", this.furnaceCookTime);
		NBTTagList list = new NBTTagList();
		
		for(int i =0; i < this.furnaceItemStack.length; i++) {
			
			if(this.furnaceItemStack[i] != null) {
				NBTTagCompound comp = new NBTTagCompound();
				comp.setInteger("Slot", i);
				this.furnaceItemStack[i].writeToNBT(comp);
				list.appendTag(comp);
			}
			
		}
		
		tag.setTag("Items", list);
		
		if(this.hasCustomInventoryName()) {
			tag.setString("CustomName", this.name);
		}
	}
	
	private boolean canSmelt() {
		boolean b = false;
		
		for(int i = 0; i < 9; i++) {
			b = b || this.canSmelt(i, i+9);
		}
		
		return b;
	}
	
	private boolean canSmelt(int inputSlot, int outputSlot) {
		if(this.furnaceItemStack[inputSlot] == null) {
			return false;
		} else {
			ItemStack stack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStack[inputSlot]);
			if(stack == null) return false;
			if(this.furnaceItemStack[outputSlot] == null) return true;
			if(!this.furnaceItemStack[outputSlot].isItemEqual(stack)) return false;
			int result = furnaceItemStack[outputSlot].stackSize + stack.stackSize;
			return result <= this.getInventoryStackLimit() && result <= this.furnaceItemStack[outputSlot].getMaxStackSize();
		}
	}
	
	private void smeltItems() {
		for(int i = 0; i < 9; i++) {
			this.smeltItem(i, i+9);
		}
	}
	
	private void smeltItem(int inputSlot, int outputSlot) {
		if(this.canSmelt(inputSlot, outputSlot)) {
			ItemStack stack = FurnaceRecipes.smelting().getSmeltingResult(furnaceItemStack[inputSlot]);
			
			if(this.furnaceItemStack[outputSlot] == null) {
				this.furnaceItemStack[outputSlot] = stack.copy();
			} else if(this.furnaceItemStack[outputSlot].getItem() == stack.getItem()) {
				this.furnaceItemStack[outputSlot].stackSize += stack.stackSize;
			}
			
			--this.furnaceItemStack[inputSlot].stackSize;
			
			if(this.furnaceItemStack[inputSlot].stackSize <= 0) {
				this.furnaceItemStack[inputSlot] = null;
			}
		}
	}
	
	private boolean checkIfInputAreAllNull() {
		boolean b = false;
		
		for(int i = 0; i < 9; i++) {
			b = b || this.furnaceItemStack[i] != null;
		}
		
		return b;
	}
	
	@Override
	public void updateEntity() {
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		// Decrement Burning time...
		if(this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}
		
		if(!this.worldObj.isRemote) {
			// Using Fuel
			if(this.furnaceBurnTime != 0 || this.furnaceItemStack[FUEL_SLOT] != null && this.checkIfInputAreAllNull()) {
				if(this.furnaceBurnTime == 0 && this.canSmelt()) {
					this.currentItemBurnTime = this.furnaceBurnTime = this.getItemBurnTime(this.furnaceItemStack[FUEL_SLOT]);
					
					if(this.furnaceBurnTime > 0) {
						flag1 = true;
						if(this.furnaceItemStack[FUEL_SLOT] != null) {
							--this.furnaceItemStack[FUEL_SLOT].stackSize;
							if(this.furnaceItemStack[FUEL_SLOT].stackSize == 0) {
								this.furnaceItemStack[FUEL_SLOT] = this.furnaceItemStack[FUEL_SLOT].getItem().getContainerItem(this.furnaceItemStack[FUEL_SLOT]);
							}
						}
					}
				}
				
				if(this.isBurning() && this.canSmelt()) {
					++this.furnaceCookTime;
					if(this.furnaceCookTime >= COOKING_SPEED) {
						this.furnaceCookTime = 0;
						this.smeltItems();
						flag1 = true;
					}
				} else {
					this.furnaceCookTime = 0;
				}
				
			}
			
			if(flag != this.furnaceBurnTime > 0) {
				flag1 = true;
				// Update Furnace State
				BlockSuperFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
		
		if(flag1) {
			this.markDirty();
		}
	}

	public boolean isBurning() {
		// TODO Auto-generated method stub
		return this.furnaceBurnTime > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int scale) {
		if(this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = COOKING_SPEED;
		}
		
		return this.furnaceBurnTime * scale / this.currentItemBurnTime;
	}
	
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int scale) {
		return this.furnaceCookTime * scale / COOKING_SPEED;
	}
}
