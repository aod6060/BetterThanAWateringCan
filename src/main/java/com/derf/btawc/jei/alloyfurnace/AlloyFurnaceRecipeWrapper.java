package com.derf.btawc.jei.alloyfurnace;

import java.util.Collections;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class AlloyFurnaceRecipeWrapper extends BlankRecipeWrapper {

	private final List<List<ItemStack>> inputs;
	private final ItemStack output;
	
	public AlloyFurnaceRecipeWrapper(List<ItemStack> inputs, ItemStack output) {
		this.inputs = Collections.singletonList(inputs);
		this.output = output;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}
	
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}
	
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(output);
	}
}
