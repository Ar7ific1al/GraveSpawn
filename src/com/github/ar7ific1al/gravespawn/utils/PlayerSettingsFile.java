package com.github.ar7ific1al.gravespawn.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

public class PlayerSettingsFile extends SettingsFile {

	public PlayerSettingsFile(String playerName, File filePath)
			throws IOException, InvalidConfigurationException {
		super(playerName + ".yml", new File("plugins/GraveSpawn/Players"));
	}
}