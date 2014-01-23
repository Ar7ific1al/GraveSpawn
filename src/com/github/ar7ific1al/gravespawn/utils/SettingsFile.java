package com.github.ar7ific1al.gravespawn.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsFile {

	private String FileName;
	private File FilePath;
	private File SettingsFile;

	public SettingsFile(String fileName, File filePath) throws IOException {
		FileName = fileName;
		FilePath = filePath;
		SettingsFile = new File(FilePath, FileName);
		if (!SettingsFile.exists()) {
			CreateFile();
		}
	}

	public void CreateFile() throws IOException {
		SettingsFile.createNewFile();
	}

	public void SaveFile(FileConfiguration f) throws IOException,
			InvalidConfigurationException {
		if (!SettingsFile.exists()) {
			CreateFile();
		}
		f.save(SettingsFile);
	}

	public void AddEntry(String path, Object value) throws IOException,
			InvalidConfigurationException {
		FileConfiguration tmpfc = new YamlConfiguration();
		tmpfc.load(SettingsFile);
		tmpfc.set(path, value);
		tmpfc.save(SettingsFile);
	}

	public Object GetEntry(String path) throws FileNotFoundException,
			IOException, InvalidConfigurationException {
		FileConfiguration tmpfc = new YamlConfiguration();
		tmpfc.load(SettingsFile);
		return tmpfc.get(path);
	}

	public void DeleteFile() {
		SettingsFile.delete();
	}
}
