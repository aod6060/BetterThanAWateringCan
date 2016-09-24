package com.derf.btawc.items.growthdevice;

import java.util.ArrayList;
import java.util.List;

import com.derf.btawc.items.ItemBasic;
import com.derf.btawc.util.BlockPos;
import com.derf.btawc.util.Timer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockReed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemGrowthDevice extends ItemBasic {
	
	
	protected int size;
	//protected int speed;
	protected Timer timer;
	
	public ItemGrowthDevice(String name, int size, int speed) {
		super(name);
		this.size = size;
		this.timer = new Timer(speed);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(
			ItemStack stack, 
			EntityPlayer player, 
			World world, 
			int x,
			int y, 
			int z, 
			int side, 
			float px, 
			float py, 
			float pz) {
		
		boolean b = false;
		
		if(!world.isRemote) {
			BlockPos pos = new BlockPos(x, y, z);
			List<BlockPos> bp = this.getPosition(pos);
			List<Block> blocks = this.getBlocks(world, bp);
			
			
			if(this.timer.isTime()) {
				for(int i = 0; i < bp.size(); i++) {
					
					if(blocks.get(i) instanceof BlockCrops) {
						this.updateCrop(bp.get(i), blocks.get(i), world);
					} else if(
						blocks.get(i) instanceof BlockBush || 
						blocks.get(i) instanceof BlockReed ||
						blocks.get(i) instanceof BlockCactus) {
						int nx = bp.get(i).getX();
						int ny = bp.get(i).getY();
						int nz = bp.get(i).getZ();
						blocks.get(i).updateTick(world, nx, ny, nz, world.rand);
					}
				}
				
				timer.reset();
			} else {
				timer.update();
			}
			
			b = true;
		} else {
			b = true;
		}
		
		return b;
	}
	
	private void updateCrop(BlockPos pos, Block block, World world) {
		int l = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
		if(l < 7) {
			if(world.rand.nextInt(2) == 0) {
				++l;
				world.setBlockMetadataWithNotify(pos.getX(), pos.getY(), pos.getZ(), l, 2);
			}
		}
	}
	
	private List<BlockPos> getPosition(BlockPos pos) {
		List<BlockPos> list = new ArrayList<BlockPos>();
		this.createPosition(list, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
		this.createPosition(list, pos);
		this.createPosition(list, new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
		return list;
	}
	
	private void createPosition(List<BlockPos> list, BlockPos c) {
		float halfSize = size / 2;
		
		for(int z = 0; z < this.size; z++) {
			for(int x = 0; x < this.size; x++) {
				int nx = (int) ((x - halfSize) + c.getX());
				int nz = (int) ((z - halfSize) + c.getZ());
				list.add(new BlockPos(nx, c.getY(), nz));
			}
		}
	}
	
	private List<Block> getBlocks(World world, List<BlockPos> pos) {
		List<Block> temp = new ArrayList<Block>();
		
		for(BlockPos p : pos) {
			temp.add(world.getBlock(p.getX(), p.getY(), p.getZ()));
		}
		
		return temp;
	}
}