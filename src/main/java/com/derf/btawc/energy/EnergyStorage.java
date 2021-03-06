package com.derf.btawc.energy;

import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorage {
	
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	
	public EnergyStorage(int energy, int capacity, int maxReceive, int maxExtract) {
		this.energy = energy;
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	public EnergyStorage(int capacity, int maxReceive, int maxExtract) {
		this(0, capacity, maxReceive, maxExtract);
	}
	
	public EnergyStorage(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer);
	}
	
	public EnergyStorage(int capacity) {
		this(capacity, capacity);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int er = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if(!simulate) {
			this.energy += er;
			
			if(energy > this.capacity) {
				energy = capacity;
			}
		}
		return er;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int ee = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if(!simulate) {
			energy -= ee;
			if(energy < 0) {
				energy = 0;
				ee = 0;
			}
		}
		return ee;
	}

	@Override
	public int getEnergyStored() {
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.capacity;
	}

	public void readFromNBT(NBTTagCompound compound) {
		/*
		 * 	protected int energy;
			protected int capacity;
			protected int maxReceive;
			protected int maxExtract;
		 */
		NBTTagCompound energyStorage = compound.getCompoundTag("energy_storage");
		this.energy = energyStorage.getInteger("energy");
		this.capacity = energyStorage.getInteger("capacity");
		this.maxExtract = energyStorage.getInteger("max_extract");
		this.maxReceive = energyStorage.getInteger("energy_storage");
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound energyStorage = new NBTTagCompound();
		energyStorage.setInteger("energy", this.energy);
		energyStorage.setInteger("capacity", this.capacity);
		energyStorage.setInteger("max_extract", this.maxExtract);
		energyStorage.setInteger("max_receive", this.maxReceive);
		compound.setTag("energy_storage", energyStorage);
		return compound;
	}

	public int getMaxReceive() {
		return maxReceive;
	}

	public int getMaxExtract() {
		return maxExtract;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
	}
	
	public boolean isFull() {
		return this.energy >= this.capacity;
	}
	
	public boolean isEmpty() {
		return this.energy <= 0;
	}
	
	public void setMaxTransfer(int transfer) {
		this.setMaxExtract(transfer);
		this.setMaxReceive(transfer);
	}
	
	public int getMaxTransfer() {
		return this.getMaxExtract();
	}
}
