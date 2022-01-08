package com.GuitarXpress.SimpleBackpacks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {

	public static ItemStack backpack;
	public static ItemStack invFiller;
	
	public static void init() {
		createBackpack();
		createInvFiller();
	}
	
	private static void createInvFiller() {
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("");
		List<String> lore = new ArrayList<String>();
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		invFiller = item;
	}

	private static void createBackpack() {
		ItemStack item = new ItemStack(Material.TRAPPED_CHEST, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Backpack");
		List<String> lore = new ArrayList<String>();
		lore.add("Backpack");
		lore.add("§7ID: ");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		backpack = item;
	}
}
