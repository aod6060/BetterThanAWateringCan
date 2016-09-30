package com.derf.btawc.items.magnet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMagnetAnimals extends ItemMagnet {

	public ItemMagnetAnimals() {
		super("magnet_animals", 32.0, 4.0, 0.3);
		this.addEntity(EntityAnimal.class);
	}

	@Override
	public void onMagnetEntityUpdate(ItemStack stack, World world, Entity entity, Entity item) {
		// TODO Auto-generated method stub
		super.onMagnetEntityUpdate(stack, world, entity, item);
		
		item.fallDistance = 0;
	}
	
	
}
