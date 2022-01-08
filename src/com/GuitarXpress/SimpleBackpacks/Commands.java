package com.GuitarXpress.SimpleBackpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("backpack")) {
			player.openInventory(createRecipeUI());
			return true;
		}
		
		return false;
	}
	
	private Inventory createRecipeUI() {
		Inventory inv = Bukkit.createInventory(null, 27, "§0Crafting Recipe §7- §6Backpack");
		for (int i = 0; i < 27; i++) {
			inv.setItem(i, ItemManager.invFiller);
		}
		inv.setItem(3, new ItemStack(Material.LEATHER, 1));
		inv.setItem(12, new ItemStack(Material.LEATHER, 1));
		inv.setItem(21, new ItemStack(Material.LEATHER, 1));
		inv.setItem(4, new ItemStack(Material.CHEST, 1));
		inv.setItem(13, new ItemStack(Material.IRON_INGOT, 1));
		inv.setItem(22, null);
		inv.setItem(5, new ItemStack(Material.LEATHER, 1));
		inv.setItem(14, new ItemStack(Material.LEATHER, 1));
		inv.setItem(23, new ItemStack(Material.LEATHER, 1));

		return inv;
	}

}
