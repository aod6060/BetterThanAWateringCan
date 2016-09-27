package com.derf.btawc.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlloyRecipe {
	// Input Stuff
	private ItemStack input1 = null;
	private ItemStack input2 = null;
	private ItemStack input3 = null;
	private ItemStack input4 = null;
	
	public AlloyRecipe(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4) {	
		this.input1 = input1;
		this.input2 = input2;
		this.input3 = input3;
		this.input4 = input4;
	}
	
	public ItemStack getInput1() {
		return input1;
	}
	
	public ItemStack getInput2() {
		return input2;
	}
	
	public ItemStack getInput3() {
		return input3;
	}
	
	public ItemStack getInput4() {
		return input4;
	}
	
	public List<ItemStack> getInputs() {
		List<ItemStack> temp = new ArrayList<ItemStack>();
		temp.add(input1);
		temp.add(input2);
		temp.add(input3);
		temp.add(input4);
		return temp;
	}
	
	public boolean isRecipe(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4) {
		if(input1 == null && input2 == null && input3 == null && input4 == null) {
			return false;
		} else if(input1 != null && input2 != null && input3 == null && input4 == null) {
			return this.input1.getItem() == input1.getItem() && 
				   this.input2.getItem() == input2.getItem() &&
				   this.input3 == null &&
				   this.input4 == null;
		} else if(input1 != null && input2 != null && input3 != null && input4 == null) {
			return this.input1.getItem() == input1.getItem() && 
				   this.input2.getItem() == input2.getItem() &&
				   this.input3.getItem() == input3.getItem() &&
				   this.input4 == null;
		} else if(input1 != null && input2 != null && input3 != null && input4 != null) {
			return this.input1.getItem() == input1.getItem() && 
				   this.input2.getItem() == input2.getItem() &&
				   this.input3.getItem() == input3.getItem() &&
				   this.input4.getItem() == input4.getItem();
		} else {
			return false;
		}
	}
}
