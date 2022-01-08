package com.GuitarXpress.SimpleBackpacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static int currentBpId;
	public static List<Backpack> backpacks = new ArrayList<Backpack>();
	public static Map<Player, Backpack> openBackpack = new HashMap<Player, Backpack>();
	public static Map<Player, Boolean> hasBackpack = new HashMap<Player, Boolean>();
	public static boolean applySlowness;
	private static FileConfiguration cfg;
	
	@Override
	public void onEnable() {
		ItemManager.init();
		Recipes.init();
		ConfigClass.setup();
		cfg = ConfigClass.getDataCfg();
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		applySlowness = getConfig().getBoolean("applySlowness");
		
		loadBackpacks();
		getServer().getPluginManager().registerEvents(new Events(this), this);
		getServer().getPluginCommand("backpack").setExecutor(new Commands(this));
		getServer().getLogger().info("§8[§eSimpleBackpacks§8] §aEnabled");
	}
	
	@Override
	public void onDisable() {
		saveBackpacks();
		getServer().getLogger().info("§8[§eSimpleBackpacks§8] §4Disabled");
	}
	
	public static void saveBackpacks() {
		for (Backpack backpack : backpacks) {
			cfg.set("Backpack." + backpack.getId(), backpack.getContents());
		}
		cfg.set("currentBpId", currentBpId);
		ConfigClass.saveData();
	}
	
	public static void loadBackpacks() {
		if (cfg.get("Backpack") == null)
			return;
		
		cfg.getConfigurationSection("Backpack").getKeys(false).forEach(key -> {
			@SuppressWarnings("unchecked")
			ItemStack[] contents = ((List<ItemStack>) cfg.get("Backpack." + key)).toArray(new ItemStack[0]);
			Backpack bp = new Backpack(Integer.valueOf(key));
			bp.setContents(contents);
			backpacks.add(bp);
		});
		
		currentBpId = cfg.getInt("currentBpId");
	}
	
}
