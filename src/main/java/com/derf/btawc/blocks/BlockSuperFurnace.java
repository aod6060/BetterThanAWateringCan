package com.derf.btawc.blocks;

import java.util.Random;

import com.derf.btawc.Loader;
import com.derf.btawc.blocks.basic.BlockContainerBasic;
import com.derf.btawc.blocks.tileentity.TileEntitySuperFurnace;
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

public class BlockSuperFurnace extends BlockContainerBasic {

	private Random rand = new Random();
	
	private static boolean isChanging;
	
	
	private boolean on;
	@SideOnly(Side.CLIENT)
	private IIcon bottom_top;
	@SideOnly(Side.CLIENT)
	private IIcon front;
	
	public BlockSuperFurnace(int lightLevel, boolean on) {
		super("super_furnace", Material.rock, 2.0f, 2.0f, lightLevel, "pickaxe", 0, Block.soundTypeStone);
		this.on = on;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySuperFurnace();
	}

	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		
		if(!world.isRemote) {
			Block north = world.getBlock(x, y, z - 1);
			Block south = world.getBlock(x, y, z + 1);
			Block west = world.getBlock(x - 1, y, z);
			Block east = world.getBlock(x + 1, y, z);
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
			TileEntitySuperFurnace furnace = (TileEntitySuperFurnace)world.getTileEntity(x, y, z);
			furnace.setName(stack.getDisplayName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
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
		this.blockIcon = reg.registerIcon(Loader.MODID + ":super_furnace_sides");
		this.front = reg.registerIcon((this.on)? Loader.MODID + ":super_furnace_front_on" : Loader.MODID + ":super_furnace_front");
		this.bottom_top = reg.registerIcon(Loader.MODID + ":super_furnace_bottom_top");
	}

	public static void updateFurnaceBlockState(boolean isWorking, World world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		TileEntity entity = world.getTileEntity(x, y, z);
		isChanging = true;
		if(isWorking) {
			world.setBlock(x, y, z, BlockManager.superFurnaceOn);
		} else {
			world.setBlock(x, y, z, BlockManager.superFurnace);
		}
		isChanging = false;
		world.setBlockMetadataWithNotify(x, y, z, l, 2);
		if(entity != null) {
			entity.validate();
			world.setTileEntity(x, y, z, entity);
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int value) {
		// TODO Auto-generated method stub
		if(!isChanging) {
			TileEntitySuperFurnace entity = (TileEntitySuperFurnace)world.getTileEntity(x, y, z);
			
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
			
			world.func_147453_f(x, y, z, block);
		}
		
		super.breakBlock(world, x, y, z, block, value);
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
		
		if(!world.isRemote) {
			player.openGui(Loader.INSTANCE, GuiHandler.SUPER_FURNACE_GUI, world, x, y, z);
		}
		
		return true;
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		// TODO Auto-generated method stub
		return Item.getItemFromBlock(this);
	}

	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		// TODO Auto-generated method stub
		return Item.getItemFromBlock(this);
	}
}
