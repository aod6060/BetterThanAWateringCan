package com.derf.btawc.blocks.generators;

import com.derf.btawc.Loader;
import com.derf.btawc.blocks.basic.BlockContainerMultiTextureBasic;
import com.derf.btawc.blocks.basic.MultiTextureType;
import com.derf.btawc.blocks.tileentity.generators.TileEntityCreativeGenerator;
import com.derf.btawc.energy.IEnergyLevelPrintable;
import com.derf.btawc.util.BlockPos;
import com.derf.btawc.util.WorldUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCreativeGenerator extends BlockContainerMultiTextureBasic {
	
	public BlockCreativeGenerator() {
		super("creative_generator", Material.iron, 2.0f, 2.0f, 0, "pickaxe", 0, Block.soundTypeMetal);
		this.setTextureName(MultiTextureType.BOTTOM, Loader.MODID + ":creative_generator_bottom_top");
		this.setTextureName(MultiTextureType.TOP, Loader.MODID + ":creative_generator_bottom_top");
		this.setTextureName(MultiTextureType.SIDES, Loader.MODID + ":creative_generator_sides");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		// TODO Auto-generated method stub
		return new TileEntityCreativeGenerator();
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
			BlockPos pos = new BlockPos(x, y, z);
			
			TileEntity entity = WorldUtils.getTileEntity(world, pos);
			
			if(entity instanceof IEnergyLevelPrintable) {
				IEnergyLevelPrintable handler = (IEnergyLevelPrintable)entity;
				handler.printEnergyValue(player);
				b = true;
			}
		}
		return b;
	}
	
	
}