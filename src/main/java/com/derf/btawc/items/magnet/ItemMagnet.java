package com.derf.btawc.items.magnet;

import java.util.ArrayList;
import java.util.List;

import com.derf.btawc.items.ItemBasic;
import com.derf.btawc.util.Vec3;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public abstract class ItemMagnet extends ItemBasic {

	private List<Class<? extends Entity>> entities;
	private double range;
	private double strength;
	
	public ItemMagnet(String name, double range, double strength) {
		super(name);
		this.entities = new ArrayList<Class<? extends Entity>>();
		for(Class<? extends Entity> entity : entities) {
			this.entities.add(entity);
		}
		this.range = range;
		this.strength = strength;
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		// TODO Auto-generated method stub
		
		if(!world.isRemote) {
			if(stack.getTagCompound() == null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("toggle", false);
				stack.setTagCompound(tag);
			}
			
			if(!player.isSneaking()) {
				this.toggle(stack);
			}
			
			String s = String.format("%s: is %s", stack.getDisplayName(), ((isToggle(stack))? "on" : "off"));
			player.addChatMessage(new ChatComponentText(s));
		}
		
		return super.onItemRightClick(stack, world, player);
	}
	
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int meta, boolean b) {
		// TODO Auto-generated method stub
		super.onUpdate(stack, world, entity, meta, b);
		
		if(stack.getTagCompound() == null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("toogle", false);
			stack.setTagCompound(tag);
		}
		
		if(this.isToggle(stack)) {
			this.onMagnetUpdate(stack, world, entity);
		}
	}

	private void onMagnetUpdate(ItemStack stack, World world, Entity entity) {
		AxisAlignedBB box = this.createBox(entity);
		Vec3 pp = new Vec3(entity.posX, entity.posY, entity.posZ);
		for(Class<? extends Entity> e : this.entities) {
			List<Entity> list = this.getEntitesWithinAABB(world, e, box);
			for(Entity item : list) {
				Vec3 v = new Vec3(item.posX, item.posY, item.posZ);
				Vec3 delta = Vec3.sub(pp, v);
				delta = Vec3.unit(delta);
				delta = Vec3.mul(delta, this.strength);
				item.motionX += delta.getX();
				item.motionY += delta.getY();
				item.motionZ += delta.getZ();
			}
		}
	}

	private void toggle(ItemStack stack) {
		this.setToggle(stack, !this.isToggle(stack));
	}
	
	private boolean isToggle(ItemStack stack) {
		return stack.getTagCompound().getBoolean("toggle");
	}
	
	private void setToggle(ItemStack stack, boolean value) {
		stack.getTagCompound().setBoolean("toggle", value);
	}
	
	private AxisAlignedBB createBox(Entity player) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
				player.posX - range,
				player.posY - range,
				player.posZ - range,
				player.posX + range,
				player.posY + range,
				player.posZ + range
		);
		
		return box;
	}
	
	private List<Entity> getEntitesWithinAABB(World world, Class clz, AxisAlignedBB box) {
		List<Entity> temp;
		temp = world.getEntitiesWithinAABB(clz, box);
		return temp;
	}
	
	protected void addEntity(Class<? extends Entity> entity) {
		this.entities.add(entity);
	}
}