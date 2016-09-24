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

	public void readFromNBT(NBTTagCompound comp) {
		this.energy = comp.getInteger("energy");
		
		if(energy > this.capacity) {
			energy = this.capacity;
		}
	}
	
	public void writeToNBT(NBTTagCompound comp) {
		if(energy < 0) {
			energy = 0;
		}
		comp.setInteger("energy", energy);
	}

	public int getMaxReceive() {
		return maxReceive;
	}

	public int getMaxExtract() {
		return maxExtract;
	}
	
	
	
}