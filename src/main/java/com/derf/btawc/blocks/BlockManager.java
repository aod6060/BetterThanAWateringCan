package com.derf.btawc.blocks;

import com.derf.btawc.blocks.energystorage.BlockEnergyStorageBasic;
import com.derf.btawc.blocks.generators.BlockCreativeGenerator;
import com.derf.btawc.blocks.tileentity.TileEntitySuperFurnace;
import com.derf.btawc.blocks.tileentity.energystorage.TileEntityEnergyStorageBasic;
import com.derf.btawc.blocks.tileentity.generators.TileEntityCreativeGenerator;
import com.derf.btawc.creativetabs.CreativeTabsManager;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public final class BlockManager {
	
	// Generators
	public static Block creativeGenerator; // Simple Creative Generator...
	// Storage
	public static Block energyStorageBasic; // Basic Energy Storage cap 10000 transfer rate 100
	// Wireless Energy Access
	// Super Furnace
	public static Block superFurnace; // 9x9 furnace...
	public static Block superFurnaceOn;
	// Misc
	
	public static final void create() {
		creativeGenerator = new BlockCreativeGenerator();
		//energyStorageBasic = new BlockEnergyStorageBasic();
		superFurnace = new BlockSuperFurnace(0, false);
		superFurnaceOn = new BlockSuperFurnace(15, true);
	}
	
	public static final void register() {
		GameRegistry.registerBlock(creativeGenerator, "creative_generator");
		//GameRegistry.registerBlock(energyStorageBasic, "energy_storage_basic");
		GameRegistry.registerBlock(superFurnace, "super_furnace");
		GameRegistry.registerBlock(superFurnaceOn, "super_furnace_on");
	}
	
	public static final void registerTileEntities() {
		// Generators
		GameRegistry.registerTileEntity(TileEntityCreativeGenerator.class, "creative_generator");
		// Storage
		//GameRegistry.registerTileEntity(TileEntityEnergyStorageBasic.class, "energy_storage_basic");
		// Super Furnace
		GameRegistry.registerTileEntity(TileEntitySuperFurnace.class, "super_furnace");
	}
	
	public static final void creativeTabs() {
		creativeGenerator.setCreativeTab(CreativeTabsManager.tabBTAWC);
		//energyStorageBasic.setCreativeTab(CreativeTabsManager.tabBTAWC);
		superFurnace.setCreativeTab(CreativeTabsManager.tabBTAWC);
	}
	
	public static final void crafting() {
		// Super Furnace
		GameRegistry.addShapedRecipe(
				new ItemStack(superFurnace), 
				"fff",
				"fff",
				"fff",
				'f', Blocks.furnace);
	}
}