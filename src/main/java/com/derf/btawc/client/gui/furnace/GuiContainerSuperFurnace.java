package com.derf.btawc.client.gui.furnace;

import org.lwjgl.opengl.GL11;

import com.derf.btawc.Loader;
import com.derf.btawc.client.Color;
import com.derf.btawc.client.gui.GuiContainerBasic;
import com.derf.btawc.inventory.container.furnace.ContainerSuperFurnace;
import com.derf.btawc.tileentity.furnace.TileEntitySuperFurnace;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiContainerSuperFurnace extends GuiContainerBasic {
	private static final ResourceLocation superFurnaceGUI = new ResourceLocation(Loader.MODID + ":textures/gui/container/super_furnace_gui.png");
	
	private final InventoryPlayer playerInventory;
	private TileEntitySuperFurnace entity;
	
	public GuiContainerSuperFurnace(InventoryPlayer playerInventory, TileEntitySuperFurnace entity) {
		super(new ContainerSuperFurnace(playerInventory, entity));
		this.playerInventory = playerInventory;
		this.entity = entity;
		
		System.out.println(this.width + ": " + this.height);
		this.xSize = 175;
		this.ySize = 207;
	}

	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String s = this.entity.hasCustomName()? this.entity.getName() : this.getLangString(this.entity.getName());
		this.renderString(s, this.xSize / 2 - this.stringWidth(s) / 2, 6, Color.BLACK);
		this.renderFormatedString("container.inventory", 6, 111, Color.BLACK);
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		this.renderBackgroundImage(superFurnaceGUI);
		int k = this.getK();
		int l = this.getL();
		
		if(this.entity.isBurning()) {
			int i1 = this.entity.getBurnTimeRemainingScaled(14);
			this.drawTexturedModalRect(k + 26, l + 71 + 14 - i1, 175, 14 - i1, 14, i1 + 1);
			i1 = this.entity.getCookProgressScaled(24);
			this.drawTexturedModalRect(k + 75, l + 30, 175, 15, i1 + 1, 17);
		}
	}

}
