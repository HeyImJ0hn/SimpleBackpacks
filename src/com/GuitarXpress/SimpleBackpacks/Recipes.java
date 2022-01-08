package com.GuitarXpress.SimpleBackpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Recipes {

	public static void init() {
		try {
			createBackpackRecipe();
		} catch(IllegalStateException e) {
//			Bukkit.getServer().getLogger().info("Duplicate Recipe: Backpack");
		}
		
	}

	private static void createBackpackRecipe() {
		ItemStack item = ItemManager.backpack;
		NamespacedKey key = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin("SimpleBackpacks"), "backpack");
		
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		
		recipe.shape("LCL", "LIL", "L L");
		
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('I', Material.IRON_INGOT);
		
		Bukkit.addRecipe(recipe);
	}
	
}
