package com.derf.btawc.tileentity.tank;

import java.util.ArrayList;
import java.util.List;

import com.derf.btawc.fluid.FluidTank;
import com.derf.btawc.fluid.FluidTankChecksAdapter;
import com.derf.btawc.fluid.IFluidTankChecks;
import com.derf.btawc.network.PacketHandler;
import com.derf.btawc.network.data.FactoryPacketData;
import com.derf.btawc.network.data.client.PacketDataFluidUpdate;
import com.derf.btawc.tileentity.EnumSixSided;
import com.derf.btawc.tileentity.ISixSidedFluidInventory;
import com.derf.btawc.tileentity.TileEntityBasic;
import com.derf.btawc.util.InventoryUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class TileEntityTank extends TileEntityBasic implements ITickable, IInventory, ISixSidedFluidInventory {

	public static final int MB_TICK = 100;
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int SLOT_SIZE = 2;
	
	private FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * 16);
	private String name = null;
	private ItemStack[] inventory = new ItemStack[SLOT_SIZE];
	private EnumFacing[] faces = new EnumFacing[6];
	private EnumSixSided[] sided = new EnumSixSided[6];
	
	public TileEntityTank() {
		for(int i = 0; i < sided.length; i++) {
			sided[i] = EnumSixSided.OFF;
			faces[i] = EnumFacing.values()[i];
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			//net.minecraftforge.fluids.FluidTank tank = new net.minecraftforge.fluids.FluidTank(this.tank.getFluid(), this.tank.getCapacity());
			return (T) tank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
		// Read Tank
		this.tank.readFromNBT(compound);
		// Read Inventory
		InventoryUtils.loadInventory(this, compound);
		if(compound.hasKey("CustomName")) {
			this.name = compound.getString("CustomName");
		}
		// Read Six Sided Fluid Inventory
		NBTTagList list = compound.getTagList("Sided", Constants.NBT.TAG_COMPOUND);
		this.sided = new EnumSixSided[6];
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound comp = list.getCompoundTagAt(i);
			this.sided[i] = EnumSixSided.values()[comp.getInteger("Value")];
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		// Tank
		this.tank.writeToNBT(compound);
		// Inventory
		InventoryUtils.saveInventory(this, compound);
		// Six Sided Fluid Inventory
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < this.sided.length; i++) {
			NBTTagCompound comp = new NBTTagCompound();
			comp.setInteger("Value", this.sided[i].ordinal());
			list.appendTag(comp);
		}
		compound.setTag("Sided", list);
		return super.writeToNBT(compound);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.hasCustomName()? this.name : "container.tank";
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return this.name != null && !this.name.isEmpty();
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return this.worldObj.getTileEntity(pos) != this? false : player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= 64.0;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		if(index == 0 && (stack.getItem() instanceof ItemBucket || stack.getItem() instanceof ItemFluidContainer || stack.getItem() instanceof UniversalBucket)) {
			return true;
		}
		
		return false;
	}

	@Override
	public int getField(int id) {
		return this.sided[id].ordinal();
	}

	@Override
	public void setField(int id, int value) {
		this.sided[id] = EnumSixSided.values()[value];
	}

	@Override
	public int getFieldCount() {
		return this.sided.length;
	}

	@Override
	public void clear() {
		this.inventory = new ItemStack[2];
	}

	@Override
	public void setType(EnumFacing side, EnumSixSided type) {
		sided[side.ordinal()] = type;
	}

	@Override
	public EnumSixSided getType(EnumFacing side) {
		// TODO Auto-generated method stub
		return sided[side.ordinal()];
	}

	@Override
	public boolean isTypeOff(EnumFacing side) {
		// TODO Auto-generated method stub
		return sided[side.ordinal()] == EnumSixSided.OFF;
	}

	@Override
	public boolean isTypePull(EnumFacing side) {
		// TODO Auto-generated method stub
		return sided[side.ordinal()] == EnumSixSided.PULL;
	}

	@Override
	public boolean isTypePush(EnumFacing side) {
		// TODO Auto-generated method stub
		return sided[side.ordinal()] == EnumSixSided.PUSH;
	}

	@Override
	public boolean isTypeDisabled(EnumFacing side) {
		// TODO Auto-generated method stub
		return sided[side.ordinal()] == EnumSixSided.DISABLED;
	}

	@Override
	public boolean isSixSidedFluidInventory(TileEntity entity) {
		// TODO Auto-generated method stub
		return entity instanceof ISixSidedFluidInventory;
	}

	@Override
	public List<EnumSixSided> getAllTypes() {
		// TODO Auto-generated method stub
		List<EnumSixSided> temp = new ArrayList<EnumSixSided>();
		
		for(EnumSixSided type : sided) {
			temp.add(type);
		}
		return temp;
	}

	// Push
	@Override
	public void drain(EnumFacing face) {
		EnumFacing opposite = face.getOpposite();
		BlockPos otherEntityPos = pos.add(face.getDirectionVec());
		TileEntity entity = worldObj.getTileEntity(otherEntityPos);
		
		if(entity != null) {
			if(this.isSixSidedFluidInventory(entity)) {
				ISixSidedFluidInventory other = (ISixSidedFluidInventory)entity;

				if(!other.isTypePush(opposite) && !other.isTypeDisabled(opposite)) {
					FluidTank tank = other.getTank();
					if(this.tank.isFluidTankEmpty()) {
						// Return because its empty
						return;
					}
					if(tank.isFluidTankEmpty()) {
						// Drain Fluid from my tank and push it to this inventory
						handleTank(tank, this.tank, MB_TICK);
					} else {
						if(!tank.isFluidTankFull()) {
							handleTank(tank, this.tank, MB_TICK);
						}
					}
				}
			} else {
				if(entity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, opposite)) {
					Object obj = entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, opposite);
					
					if(obj instanceof FluidTank) {
						FluidTank tank = (FluidTank)obj;
						
						if(this.tank.isFluidTankEmpty()) {
							return;
						}
						
						if(tank.isFluidTankEmpty()) {
							handleTank(tank, this.tank, MB_TICK);
						} else {
							if(!tank.isFluidTankFull()) {
								handleTank(tank, this.tank, MB_TICK);
							}
						}
					} else {
						// Not supported for the moment
					}
				}
			}
		}
	}

	// Pull
	@Override
	public void fill(EnumFacing face) {
		EnumFacing opposite = face.getOpposite();
		BlockPos otherEntityPos = pos.add(face.getDirectionVec());
		TileEntity entity = worldObj.getTileEntity(otherEntityPos);
		
		if(entity != null) {
			if(this.isSixSidedFluidInventory(entity)) {
				ISixSidedFluidInventory other = (ISixSidedFluidInventory)entity;
				
				if(!other.isTypeDisabled(opposite) && !other.isTypePull(opposite)) {
					FluidTank tank = other.getTank();
					if(tank.isFluidTankEmpty()) {
						// Return because its empty
						return;
					}
					if(this.tank.isFluidTankEmpty()) {
						// Drain Fluid from my tank and push it to this inventory
						handleTank(this.tank, tank, MB_TICK);
					} else {
						if(!this.tank.isFluidTankFull()) {
							handleTank(this.tank, tank, MB_TICK);
						}
					}
				}
			} else {
				
				if(entity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, opposite)) {
					Object obj = entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, opposite);
					
					if(obj instanceof FluidTank) {
						FluidTank tank = (FluidTank)obj;
						
						if(tank.isFluidTankEmpty()) {
							return;
						}
						
						if(this.tank.isFluidTankEmpty()) {
							handleTank(this.tank, tank, MB_TICK);
						} else {
							if(!this.tank.isFluidTankFull()) {
								handleTank(this.tank, tank, MB_TICK);
							}
						}
					} else {
						// Not supported for the moment
					}
				}
			}
		}
	}
	
	private void handleFluidTank(FluidTank output, net.minecraftforge.fluids.FluidTank input, int buckets) {
		FluidStack input_stack = input.drain(buckets, false);
		if(output.isFluidTankEmpty() || (!output.isFluidTankFull() && output.getFluid().containsFluid(input_stack))) {
			int filled = output.fill(input_stack, true);
			input.drain(filled, true);
		}
	}

	private void handleTank(FluidTank output, FluidTank input, int buckets) {
		// Test to see if I can do a fake drain on the input tank
		FluidStack input_stack = input.drain(buckets, false);
		// Check and see if the fluid in the tanks are the same...
		if(output.isFluidTankEmpty() || (!output.isFluidTankFull() && output.getFluid().containsFluid(input_stack))) {
			// Fill output tank
			int filled = output.fill(input_stack, true);
			// drain input tank
			input.drain(filled, true);
		}
	}

	
	@Override
	public FluidTank getTank() {
		return this.tank;
	}
	
	@Override
	public List<FluidTank> getTanks() {
		List<FluidTank> temp = new ArrayList<FluidTank>();
		temp.add(this.tank);
		return temp;
	}
	
	@Override
	public void update() {
		if(!worldObj.isRemote) {
			// This is the six sided inventory
			for(EnumFacing face : faces) {
				if(this.isTypeOff(face) || this.isTypeDisabled(face)) {
					continue;
				} else {
					if(this.isTypePull(face)) {
						this.fill(face);
					} else if(this.isTypePush(face)) {
						this.drain(face);
					}
				}
			}
			
			// Fill and Drain bucket
			if(this.getStackInSlot(INPUT_SLOT) != null && this.getStackInSlot(OUTPUT_SLOT) == null) {
				ItemStack stack = this.getStackInSlot(INPUT_SLOT);
				ItemStack output = null;
				boolean handle = false;
				
				if(stack.getItem() instanceof ItemBucket || stack.getItem() instanceof UniversalBucket) {
					
					ICapabilityProvider provider = stack.getItem().initCapabilities(stack, null);
					
					if(provider instanceof FluidBucketWrapper) {
						FluidBucketWrapper wrapper = (FluidBucketWrapper)provider;
						
						if(wrapper.getFluid() == null) {
							// filling
							if(this.tank.canDrain() && this.tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
								FluidStack fluid = this.tank.drain(Fluid.BUCKET_VOLUME, true);
								
								if(fluid != null) {
									output = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid.getFluid());
									handle = true;
								}
							}
						} else {
							// draining
							if(this.tank.canFillFluidType(wrapper.getFluid()) && this.tank.getFluidAmount() < this.tank.getCapacity()) {
								this.tank.fill(wrapper.getFluid(), true);
								output = new ItemStack(Items.BUCKET);
								handle = true;
							}
						}
					}
				}
				
				if(handle) {	
					this.decrStackSize(INPUT_SLOT, 1);
					this.setInventorySlotContents(OUTPUT_SLOT, output);
					this.sendToClient();
				}
			}
			
			// Mark dirty for saving...
			this.sendToClient();
			this.markDirty();
		}
	}
	
	public void setTank(FluidTank tank) {	
		if(tank != null) {
			if(tank.getFluid() != null) {
				this.tank.setFluid(tank.getFluid().copy());
			} else {
				this.tank.setFluid(null);
			}
		}
	}
	
	private void sendToClient() {
		PacketHandler.sendPacketToClient(FactoryPacketData.createPacketData("fluid_update", PacketDataFluidUpdate.createCallback(pos, tank)));
	}
	
	public String grabString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.tank);
		if(this.getTank().getFluid() != null) {
			builder.append(" Name: " + this.tank.getFluid().getLocalizedName());
		}
		return builder.toString();
	}
}
