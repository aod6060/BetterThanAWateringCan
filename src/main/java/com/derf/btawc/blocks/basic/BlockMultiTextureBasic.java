package com.derf.btawc.blocks.basic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockMultiTextureBasic extends Block {
	
	private String[] textureNames = new String[6];
	private IIcon[] icons = new IIcon[6];
	
	
	public BlockMultiTextureBasic(
			String name, 
			Material material,
			float hardness,
			float resistance,
			float lightLevel,
			String toolClass,
			int level,
			SoundType sound) {
		super(material);
		this.setBlockName(name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightLevel(lightLevel);
		this.setHarvestLevel(toolClass, level);
		this.setStepSound(sound);
	}
	
	protected void setTextureName(MultiTextureType type, String location) {
		if(type == MultiTextureType.SIDES) {
			this.textureNames[MultiTextureType.NORTH.ordinal()] = location;
			this.textureNames[MultiTextureType.SOUTH.ordinal()] = location;
			this.textureNames[MultiTextureType.EAST.ordinal()] = location;
			this.textureNames[MultiTextureType.WEST.ordinal()] = location;
		} else {
			this.textureNames[type.ordinal()] = location;
		}
	}
	
	protected String getTextureName(MultiTextureType type) {
		
		StringBuilder builder = new StringBuilder();
		
		if(type == MultiTextureType.SIDES) {
			builder.append(this.textureNames[MultiTextureType.NORTH.ordinal()] + "\n");
			builder.append(this.textureNames[MultiTextureType.SOUTH.ordinal()] + "\n");
			builder.append(this.textureNames[MultiTextureType.EAST.ordinal()] + "\n");
			builder.append(this.textureNames[MultiTextureType.WEST.ordinal()] + "\n");
		} else {
			builder.append(this.textureNames[type.ordinal()]);
		}
		
		return builder.toString();
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for(int i = 0; i < 6; i++) {
			this.icons[i] = reg.registerIcon(textureNames[i]);
		}
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		// TODO Auto-generated method stub
		return this.icons[side];
	}
	
	
}