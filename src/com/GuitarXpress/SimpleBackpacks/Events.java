package com.GuitarXpress.SimpleBackpacks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {
	Main main;

	public Events(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = event.getItem();

			if (isBackpack(item)) {
				if (!event.getPlayer().hasPermission("backpacks.use")) {
					event.getPlayer().sendMessage("§cYou don't have permission to open backpacks.");
					return;
				}
				int id = getBackpackId(item);
				for (Backpack bp : Main.backpacks) {
					if (bp.getId() == id) {
						openBackpack(bp, event.getPlayer());
					}
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		int backpacks = countBackpacks(event.getPlayer().getInventory());
		if (backpacks > 0)
			Main.hasBackpack.put((Player) event.getPlayer(), true);
		else
			Main.hasBackpack.put((Player) event.getPlayer(), false);
	}

	@EventHandler
	public void onIventoryClose(InventoryCloseEvent event) {
		if (Main.openBackpack.get((Player) event.getPlayer()) != null) {
			Main.openBackpack.get((Player) event.getPlayer())
					.setContents(event.getPlayer().getOpenInventory().getTopInventory().getContents());
			Main.openBackpack.put((Player) event.getPlayer(), null);
			Main.saveBackpacks();
		}

		if (!Main.applySlowness)
			return;

		Inventory inv = event.getPlayer().getInventory();
		int backpacks = countBackpacks(inv);

		if (Main.hasBackpack.containsKey((Player) event.getPlayer())
				&& Main.hasBackpack.get((Player) event.getPlayer()))
			event.getPlayer().removePotionEffect(PotionEffectType.SLOW);

		if (backpacks > 1) {
			event.getPlayer().addPotionEffect(
					new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, backpacks, false, false));
		} else if (backpacks == 0) {
			Main.hasBackpack.put((Player) event.getPlayer(), false);
			event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
		}
	}

	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		if (!Main.applySlowness)
			return;

		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		if (isBackpack(event.getItem().getItemStack())) {
			int backpacks = countBackpacks(player.getInventory());
			player.removePotionEffect(PotionEffectType.SLOW);
			if (backpacks + 1 > 1)
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, backpacks, false, false));
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (!Main.applySlowness)
			return;

		if (isBackpack(event.getItemDrop().getItemStack())) {
			int backpacks = countBackpacks(event.getPlayer().getInventory());
			event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
			if (backpacks > 1)
				event.getPlayer().addPotionEffect(
						new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, backpacks, false, false));
		}
	}

	@EventHandler
	public void onPrepareCraft(PrepareItemCraftEvent event) {
		if (event.getInventory().getResult() == null)
			return;

		if (isBackpack(event.getInventory().getResult())) {
			event.getInventory().setResult(getBackpack());
		}
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		boolean removeResult = false;
		if (isBackpack(event.getCurrentItem())) {
			if (event.isShiftClick()) {
				event.setCancelled(true);
				if (event.getWhoClicked().getInventory().firstEmpty() != -1) {
					for (int i = 1; i < event.getInventory().getSize(); i++) {
						if (event.getInventory().getItem(i) != null) {
							event.getInventory().getItem(i).setAmount(event.getInventory().getItem(i).getAmount() - 1);
							if (event.getInventory().getItem(i) == null)
								removeResult = true;
						}
					}
					event.getWhoClicked().getInventory().addItem(getBackpack());
					Backpack bp = new Backpack(Main.currentBpId++);
					Main.backpacks.add(bp);
					if (!removeResult)
						event.setCurrentItem(getBackpack());
					else
						event.setCurrentItem(null);
				} else {
					return;
				}
			} else {
				Backpack bp = new Backpack(Main.currentBpId++);
				Main.backpacks.add(bp);
			}
		}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		if (event.getView().getTitle().equals("§0Crafting Recipe §7- §6Backpack"))
			event.setCancelled(true);

		if (Main.openBackpack.get(((Player) event.getWhoClicked())) != null) {
			if (isBackpack(event.getCurrentItem())) {
				((Player) event.getWhoClicked())
						.sendMessage("§cHey! You can't put a backpack inside another backpack!");
				event.setCancelled(true);
			}
		}
	}

	private int countBackpacks(Inventory inv) {
		int bpCounter = 0;

		for (int i = 0; i < inv.getContents().length; i++) {
			if (inv.getContents()[i] != null) {
				if (isBackpack(inv.getContents()[i])) {
					bpCounter++;
				}
			}
		}
		return bpCounter;
	}

	private boolean isBackpack(ItemStack item) {
		if (item != null) {
			if (item.hasItemMeta()) {
				if (item.getItemMeta().hasLore()) {
					if (item.getItemMeta().getLore().size() > 0) {
						if (item.getItemMeta().getLore().get(0).equals("Backpack")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private ItemStack getBackpack() {
		ItemStack item = ItemManager.backpack;
		List<String> lore = new ArrayList<String>();
		lore.add("Backpack");
		lore.add("§7ID: " + Main.currentBpId);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private int getBackpackId(ItemStack bp) {
		int id = -1;

		String s = bp.getItemMeta().getLore().get(1).substring(2);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				sb.append(s.charAt(i));
			}
		}
		id = Integer.valueOf(sb.toString());
		return id;
	}

	private void openBackpack(Backpack bp, Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Backpack");

		Main.openBackpack.put(player, bp);
		inv.setContents(bp.getContents());

		player.openInventory(inv);
	}
}
