package com.GuitarXpress.SimpleBackpacks;

import org.bukkit.inventory.ItemStack;

public class Backpack {

	private static final int MAX_SLOTS = 27;
	private int id;
	private ItemStack[] contents;
	
	public Backpack(int id) {
		this.id = id;
		contents = new ItemStack[MAX_SLOTS];
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemStack[] getContents() {
		return contents;
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}
	
	
	
}
