package com.github.ar7ific1al.gravespawn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ar7ific1al.gravespawn.commands.GraveSpawnCommandExecutor;
import com.github.ar7ific1al.gravespawn.utils.LoginListener;
import com.github.ar7ific1al.gravespawn.utils.RespawnListener;

public class Plugin extends JavaPlugin {

	public static Logger console = Logger.getLogger("Minecraft");
	public String version;

	public static File PlayersDir = new File("plugins/GraveSpawn/Players/");
	public static File GravesDir = new File("plugins/GraveSpawn/Graves");

	public static List<Grave> Graves = new ArrayList<Grave>();

	public static Random random = new Random();

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		PluginDescriptionFile pdFile = this.getDescription();
		String ver = pdFile.getVersion();
		version = ver;
		console.info("[GraveSpawn] GraveSpawn version " + version + " enabled.");

		LoginListener loginlistener = new LoginListener(this);
		RespawnListener respawnListener = new RespawnListener(this);

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if (!PlayersDir.exists()) {
			PlayersDir.mkdir();
		}
		if (!GravesDir.exists()) {
			GravesDir.mkdir();
		}

		try {
			CreateSettingsFile();
		} catch (IOException | InvalidConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pm.registerEvents(loginlistener, this);
		pm.registerEvents(respawnListener, this);
		GraveSpawnCommandExecutor commands = new GraveSpawnCommandExecutor(this);
		getCommand("setgrave").setExecutor(commands);
		getCommand("gravespawn").setExecutor(commands);
		getCommand("graves").setExecutor(commands);
		getCommand("tpgrave").setExecutor(commands);
		getCommand("grave").setExecutor(commands);

		try {
			Graves = Grave.RefreshGraveList();
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		console.info("[GraveSpawn] GraveSpawn version " + version
				+ " disabled.");
	}

	public void CreateSettingsFile() throws IOException,
			InvalidConfigurationException {
		File settings = new File(getDataFolder(), "settings.yml");
		if (!settings.exists()) {
			saveResource("settings.yml", false);
		}
	}

}