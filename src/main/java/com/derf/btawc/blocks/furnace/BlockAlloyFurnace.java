package com.derf.btawc.blocks.furnace;

import java.util.Random;

import com.derf.btawc.Loader;
import com.derf.btawc.blocks.BlockManager;
import com.derf.btawc.blocks.basic.BlockContainerBasic;
import com.derf.btawc.blocks.tileentity.furnace.TileEntityAlloyFurnace;
import com.derf.btawc.client.gui.GuiHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockAlloyFurnace extends BlockContainerBasic {
	
	private final Random rand = new Random();
	private static boolean isChanging = false;
	
	private boolean on;
	@SideOnly(Side.CLIENT)
	private IIcon bottom_top;
	@SideOnly(Side.CLIENT)
	private IIcon front;
	
	public BlockAlloyFurnace(int lightLevel, boolean on) {
		super("alloy_furnace", Material.rock, 2.0f, 2.0f, lightLevel, "pickaxe", 0, Block.soundTypeStone);
		this.on = on;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityAlloyFurnace();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		// TODO Auto-generated method stub
		IIcon temp = null;
		
		if(side == 1 || side == 0) {
			temp = this.bottom_top;
		} else if(side != meta) {
			temp = this.blockIcon;
		} else {
			temp = this.front;
		}
		
		return temp;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon(Loader.MODID + ":alloy_furnace_side");
		String f = (this.on)? ":alloy_furnace_front_on" : ":alloy_furnace_front";
		this.front = reg.registerIcon(Loader.MODID + f);
		this.bottom_top = reg.registerIcon(Loader.MODID + ":alloy_furnace_bottom_top");
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		super.onBlockAdded(world, x, y, z);
		
		if(!world.isRemote) {
			Block north = world.getBlock(x, y, z-1);
			Block south = world.getBlock(x, y, z+1);
			Block west = world.getBlock(x-1, y, z);
			Block east = world.getBlock(x+1, y, z);
			byte meta = 3;
			
			if(north.func_149730_j() && !south.func_149730_j()) {
				meta = 3;
			}
			
			if(south.func_149730_j() && !north.func_149730_j()) {
				meta = 2;
			}
			
			if(west.func_149730_j() && !east.func_149730_j()) {
				meta = 5;
			}
			
			if(east.func_149730_j() && !west.func_149730_j()) {
				meta = 4;
			}
			
			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
	}

	@Override
	public void onBlockPlacedBy(
			World world, 
			int x, 
			int y, 
			int z,
			EntityLivingBase entity, 
			ItemStack stack) {
		// TODO Auto-generated method stub
		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
		
		if(l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		
		if(l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		
		if(l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		
		if(l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		
		if(stack.hasDisplayName()) {
			TileEntityAlloyFurnace furnace = (TileEntityAlloyFurnace)world.getTileEntity(x, y, z);
			furnace.setName(stack.getDisplayName());
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemFromBlock(this);
	}

	public static void updateAlloyFurnaceBlockState(boolean isWorking, World world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		TileEntity entity = world.getTileEntity(x, y, z);
		isChanging = true;
		if(isWorking) {
			world.setBlock(x, y, z, BlockManager.alloyFurnaceOn);
		} else {
			world.setBlock(x, y, z, BlockManager.alloyFurnace);
		}
		isChanging = false;
		world.setBlockMetadataWithNotify(x, y, z, l, 2);
		if(entity != null) {
			entity.validate();
			world.setTileEntity(x, y, z, entity);
		}
	}

	@Override
	public void breakBlock(
			World world, 
			int x, 
			int y, 
			int z, 
			Block block, 
			int side) {
		if(!isChanging) {
			TileEntityAlloyFurnace entity = (TileEntityAlloyFurnace)world.getTileEntity(x, y, z);
			if(entity != null) {
				for(int i = 0; i < entity.getSizeInventory(); i++) {
					ItemStack stack = entity.getStackInSlot(i);
					if(stack != null) {
						float fx = this.rand.nextFloat() * 0.8f + 0.1f;
						float fy = this.rand.nextFloat() * 0.8f + 0.1f;
						float fz = this.rand.nextFloat() * 0.8f + 0.1f;
						EntityItem items = new EntityItem(world, x + fx, y + fy, z + fz, stack);
						if(stack.hasTagCompound()) {
							items.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
						}
						world.spawnEntityInWorld(items);
					}
				}
			}
		}
		super.breakBlock(world, x, y, z, block, side);
	}

	@Override
	public boolean onBlockActivated(
			World world, 
			int x, 
			int y, 
			int z,
			EntityPlayer player, 
			int side, 
			float p_149727_7_, 
			float p_149727_8_, 
			float p_149727_9_) {
		
		if(!world.isRemote) {
			player.openGui(Loader.INSTANCE, GuiHandler.ALLOY_FURNACE_GUI, world, x, y, z);
		}
		
		return true;
	}
	
	
	
}
