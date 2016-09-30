package com.derf.btawc.blocks.energystorage;

import com.derf.btawc.blocks.basic.BlockContainerBasic;
import com.derf.btawc.energy.IEnergyLevelPrintable;
import com.derf.btawc.util.BlockPos;
import com.derf.btawc.util.WorldUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockEnergyStorage extends BlockContainerBasic {

	public BlockEnergyStorage(String name) {
		super(name, Material.iron, 2.0f, 2.0f, 0, "pickaxe", 0, Block.soundTypeMetal);
	}

	@Override
	public boolean onBlockActivated(
			World world, 
			int x, 
			int y, 
			int z,
			EntityPlayer player, 
			int side, 
			float tx, 
			float ty, 
			float tz) {
		// TODO Auto-generated method stub
		
		boolean b = false;
		
		if(!world.isRemote) {
			/*
			BlockPos pos = new BlockPos(x, y, z);
			
			TileEntity entity = WorldUtils.getTileEntity(world, pos);
			
			if(entity != null && entity instanceof IEnergyLevelPrintable) {
				IEnergyLevelPrintable handler = (IEnergyLevelPrintable)entity;
				handler.printEnergyValue(player);
				b = true;
			}
			*/
		}
		
		return b;
	}
	
	
}
