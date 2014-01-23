package com.github.ar7ific1al.gravespawn.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.ar7ific1al.gravespawn.Grave;
import com.github.ar7ific1al.gravespawn.Plugin;

public class RespawnListener implements Listener {

	Plugin plugin;

	public RespawnListener(Plugin instance) {
		plugin = instance;
		Plugin.console.info("[GraveSpawn] Death Listener registered.");
	}

	@EventHandler
	public void playerCommand(PlayerCommandPreprocessEvent event) {
		String commandArgs[] = event.getMessage().split(" ");
		if (commandArgs.length == 6) {
			
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void respawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		try {
			PlayerSettingsFile psf = new PlayerSettingsFile(player.getName(),
					Plugin.PlayersDir);
			SettingsFile pluginSettings = new SettingsFile("settings.yml",
					new File("plugins/GraveSpawn/"));
			if (psf.GetEntry("Enabled") == Boolean.TRUE
					&& player.hasPermission("gravespawn.respawn")) {
				Grave grave = FindGrave();
				Location spawnLocation = new Location(Bukkit.getServer().getWorld(grave.getWorld()),
						grave.getX(), grave.getY(), grave.getZ(),
						grave.getYaw(), grave.getPitch());
				event.setRespawnLocation(spawnLocation);
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						plugin,
						new SoundScheduler(event.getRespawnLocation()
								.getWorld(), event.getRespawnLocation(), grave
								.getSound(), grave.getPitchVariation()), 1L);
				if (pluginSettings.GetEntry("Settings.Debug.Verbose") == Boolean.TRUE) {
					Bukkit.getServer()
							.getLogger()
							.log(Level.INFO,
									"[GraveSpawn] Player " + player.getName()
											+ " spawned at grave "
											+ grave.getTitle());
				}

			}
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Grave FindGrave() {
		int index = Plugin.random.nextInt(Plugin.Graves.size());
		Grave grave = Plugin.Graves.get(index);
		return grave;
	}
}
