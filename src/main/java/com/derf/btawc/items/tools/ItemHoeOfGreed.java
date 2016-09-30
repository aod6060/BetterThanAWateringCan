package com.derf.btawc.items.tools;

import java.util.List;

import com.derf.btawc.items.ItemBasic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoeOfGreed extends ItemBasic {

	protected int size;
	protected int count;
	
	public ItemHoeOfGreed(String name, int maxDamage, int size, int count) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxDamage);
		this.size = size;
		this.count = count;
	}

	/*
	@Override
	public boolean onItemUse(
			ItemStack stack, 
			EntityPlayer player, 
			World world, 
			int x,
			int y, 
			int z, 
			int side, 
			float tx, 
			float ty, 
			float tz) {
		
		
		boolean b = false;
		
		if(!player.canPlayerEdit(x, y, z, side, stack)) {
			b = false;
		} else {
			
			List<BlockPos> ps = new ArrayList<BlockPos>();
			
			this.createPosition(ps, new BlockPos(x, y, z));
			
			for(BlockPos p : ps) {
				onHoeUsed(stack, player, world, p.getX(), p.getY(), p.getZ(), side);
			}
			
			b = true;
		}
		return b;
	}
	*/
	protected void onHoeUsed(
			ItemStack stack, 
			EntityPlayer player, 
			World world, 
			int x,
			int y, 
			int z, 
			int side) {
		/*Block block = world.getBlock(x, y, z);
		
		if(side != 0 && world.getBlock(x, y+1, z).isAir(world, x, y+1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
			Block temp = Blocks.farmland;
			world.playSoundEffect(
					x+0.5f, 
					y+1, 
					z, 
					temp.stepSound.getStepResourcePath(), 
					(temp.stepSound.getVolume() + 1) * 0.5f, 
					temp.stepSound.getPitch() * 0.8f);
			if(!world.isRemote) {
				world.setBlock(x, y, z, temp);
				stack.damageItem(1, player);
			}
		}
		*/
	}
	
	/*
	@Override
	public boolean onBlockDestroyed(
			ItemStack stack, 
			World world, 
			Block block, 
			int x,
			int y, 
			int z, 
			EntityLivingBase entity) {
		
		
		
		if(block instanceof BlockCrops) {
			// Execute
			
			if(!world.isRemote) {
				
				for(int i = 0; i < this.count - 1; i++) {
					
					if(world.getBlockMetadata(x, y, z) == 7) {
						List<ItemStack> drops = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
						
						for(ItemStack d : drops) {
							world.spawnEntityInWorld(new EntityItem(world, x, y, z, d));
						}
					} else {
						continue;
					}
				}
			}
		}
		
		return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
	}
	*/
	
	@Override
	public boolean isFull3D() {
		return true;
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
}
