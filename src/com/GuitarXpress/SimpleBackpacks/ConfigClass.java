package com.GuitarXpress.SimpleBackpacks;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigClass {

	private static File dataFile;
	private static FileConfiguration dataCfg;

	public static void setup() {
		dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("SimpleBackpacks").getDataFolder(), "data.yml");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage("[SimpleBackpacks] Failed to create data.yml\n" + "-> " + e);
			}
		}

		dataCfg = YamlConfiguration.loadConfiguration(dataFile);
	}

	public static FileConfiguration getDataCfg() {
		return dataCfg;
	}

	public static void saveData() {
		try {
			dataCfg.save(dataFile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("[SimpleBackpacks] Failed to save data.yml");
		}
	}
}
