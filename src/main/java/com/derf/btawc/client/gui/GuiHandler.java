package com.derf.btawc.client.gui;

import com.derf.btawc.blocks.inventory.container.furnace.ContainerAlloyFurnace;
import com.derf.btawc.blocks.inventory.container.furnace.ContainerSuperFurnace;
import com.derf.btawc.blocks.inventory.container.generator.ContainerCreativeGenerator;
import com.derf.btawc.blocks.tileentity.furnace.TileEntityAlloyFurnace;
import com.derf.btawc.blocks.tileentity.furnace.TileEntitySuperFurnace;
import com.derf.btawc.blocks.tileentity.generators.TileEntityCreativeGenerator;
import com.derf.btawc.client.gui.furnace.GuiContainerAlloyFurnace;
import com.derf.btawc.client.gui.furnace.GuiContainerSuperFurnace;
import com.derf.btawc.client.gui.generators.GuiContainerCreativeGenerator;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
	
	// Super Furnace
	public final static int SUPER_FURNACE_GUI = 0;
	// Alloy Furnace
	public final static int ALLOY_FURNACE_GUI = 1;
	// Creative Generator
	public final static int CREATIVE_GENERATOR_GUI = 2;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case SUPER_FURNACE_GUI:
			return new ContainerSuperFurnace(player.inventory, (TileEntitySuperFurnace) world.getTileEntity(x, y, z));
		case ALLOY_FURNACE_GUI:
			return new ContainerAlloyFurnace(player.inventory, (TileEntityAlloyFurnace)world.getTileEntity(x, y, z));
		case CREATIVE_GENERATOR_GUI:
			return new ContainerCreativeGenerator(player.inventory, (TileEntityCreativeGenerator)world.getTileEntity(x, y, z));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case SUPER_FURNACE_GUI:
			return new GuiContainerSuperFurnace(player.inventory, (TileEntitySuperFurnace)world.getTileEntity(x, y, z));
		case ALLOY_FURNACE_GUI:
			return new GuiContainerAlloyFurnace(player.inventory, (TileEntityAlloyFurnace)world.getTileEntity(x, y, z));
		case CREATIVE_GENERATOR_GUI:
			return new GuiContainerCreativeGenerator(player.inventory, (TileEntityCreativeGenerator)world.getTileEntity(x, y, z));
		}
		
		return null;
	}

}
