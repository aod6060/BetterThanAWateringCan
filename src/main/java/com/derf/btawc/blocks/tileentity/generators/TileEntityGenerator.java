package com.derf.btawc.blocks.tileentity.generators;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import com.derf.btawc.blocks.tileentity.TileEntityBasic;
import com.derf.btawc.energy.EnergyStorage;
import com.derf.btawc.energy.IEnergyLevelPrintable;

import cofh.api.energy.IEnergyProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityGenerator extends TileEntityBasic implements IEnergyProvider, IEnergyLevelPrintable {
	
	protected EnergyStorage storage = null;
	
	
	@Override
	public void updateEntity() {
		// TODO Auto-generated method stub
		super.updateEntity();
		// Update Generator per tick
		updateGeneratorInternalStorage();
		// Update TileEntities with the internal storage
		updateTileEntities();
	}

	protected abstract void updateTileEntities();

	protected abstract void updateGeneratorInternalStorage();

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		storage.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		storage.writeToNBT(tag);
	}
	
	@Override
	public String printEnergyValue() {
		String s = String.format("[%d/%d] %d RF/t", this.storage.getEnergyStored(), this.storage.getMaxEnergyStored(), this.storage.getMaxReceive());
		return s;
	}
	
	@SideOnly(Side.CLIENT)
	public int getEnergyLevelScaled(int scale) {
		if(this.storage.getEnergyStored() == 0) {
			return 0;
		} else {
			return this.storage.getEnergyStored() * scale / this.storage.getMaxEnergyStored();
		}
	}
	
	public EnergyStorage getStorage() {
		return this.storage;
	}
}
