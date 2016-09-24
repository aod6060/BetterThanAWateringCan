package com.derf.btawc.client.gui;

import org.lwjgl.opengl.GL11;

import com.derf.btawc.Loader;
import com.derf.btawc.blocks.inventory.container.ContainerSuperFurnace;
import com.derf.btawc.blocks.tileentity.TileEntitySuperFurnace;
import com.derf.btawc.client.Color;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiContainerSuperFurnace extends GuiContainer {
	private static final ResourceLocation superFurnaceGUI = new ResourceLocation(Loader.MODID + ":textures/gui/container/super_furnace_gui.png");
	
	private final InventoryPlayer playerInventory;
	private TileEntitySuperFurnace entity;
	
	public GuiContainerSuperFurnace(InventoryPlayer playerInventory, TileEntitySuperFurnace entity) {
		super(new ContainerSuperFurnace(playerInventory, entity));
		this.playerInventory = playerInventory;
		this.entity = entity;
		
		this.xSize = 175;
		this.ySize = 207;
	}

	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String s = this.entity.hasCustomInventoryName()? this.entity.getInventoryName() : I18n.format(this.entity.getInventoryName(), new Object[0]);
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, Color.BLACK.toColor16());
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 6, 111, Color.BLACK.toColor16());
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(superFurnaceGUI);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
		if(this.entity.isBurning()) {
			int i1 = this.entity.getBurnTimeRemainingScaled(14);
			this.drawTexturedModalRect(k + 26, l + 71 + 14 - i1, 175, 14 - i1, 14, i1 + 1);
			i1 = this.entity.getCookProgressScaled(24);
			this.drawTexturedModalRect(k + 75, l + 30, 175, 15, i1 + 1, 17);
		}
	}

}
