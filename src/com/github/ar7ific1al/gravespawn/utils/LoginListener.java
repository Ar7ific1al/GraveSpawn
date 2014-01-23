package com.github.ar7ific1al.gravespawn.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.github.ar7ific1al.gravespawn.Plugin;

public class LoginListener implements Listener {

	Plugin plugin;

	public LoginListener(Plugin instance) {
		plugin = instance;
		Plugin.console.info("[GraveSpawn] Player Login Listener registered.");
	}

	@EventHandler
	public void login(PlayerLoginEvent event) {
		Player player = event.getPlayer();

		File f = new File(Plugin.PlayersDir, player.getName() + ".yml");
		if (!f.exists()) {
			try {
				PlayerSettingsFile psf = new PlayerSettingsFile(
						player.getName(), f);
				psf.AddEntry("Enabled", true);
			} catch (IOException | InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
