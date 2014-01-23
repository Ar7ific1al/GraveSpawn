package com.github.ar7ific1al.gravespawn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.util.org.apache.commons.lang3.EnumUtils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;

import com.github.ar7ific1al.gravespawn.utils.SettingsFile;

public class Grave {
	
	private String Title;
	private double X, Y, Z;
	private float Yaw, Pitch;
	private Sound GraveSound;
	private boolean SoundPitchVariation;
	private String GraveWorld;
	
	public String getTitle(){
		return Title;
	}
	public double getX(){
		return X;
	}
	public double getY(){
		return Y;
	}
	public double getZ(){
		return Z;
	}
	public float getYaw(){
		return Yaw;
	}
	public float getPitch(){
		return Pitch;
	}
	public Sound getSound(){
		return GraveSound;
	}
	public boolean getPitchVariation(){
		return SoundPitchVariation;
	}
	public String getWorld(){
		return GraveWorld;
	}
	
	public Grave(String title, double x, double y, double z, float yaw, float pitch, Sound sound, boolean variation, String world){
		Title = title;
		X = x;
		Y = y;
		Z = z;
		Yaw = yaw;
		Pitch = pitch;
		GraveSound = sound;
		SoundPitchVariation = variation;
		GraveWorld = world;
	}
	
	public static List<Grave> RefreshGraveList() throws IOException, InvalidConfigurationException{
		List<Grave> graves = new ArrayList<Grave>();
		
		File[] gravesDir = (new File("plugins/GraveSpawn/Graves/")).listFiles();
		for(File graveFile : gravesDir){
			SettingsFile s = new SettingsFile(graveFile.getName(), new File("plugins/GraveSpawn/Graves/"));
			SettingsFile pluginSettings = new SettingsFile("settings.yml", new File("plugins/GraveSpawn/"));
			Sound snd = null;
			if (EnumUtils.isValidEnum(Sound.class, s.GetEntry("Grave.Sound").toString())){
				snd = Sound.valueOf(s.GetEntry("Grave.Sound").toString());
			}
			Grave newGrave = new Grave((String) s.GetEntry("Grave.Title"), (double) s.GetEntry("Grave.X"), (double) s.GetEntry("Grave.Y"),
					   (double) s.GetEntry("Grave.Z"), Float.parseFloat(s.GetEntry("Grave.Yaw").toString()), Float.parseFloat(s.GetEntry("Grave.Pitch").toString()),
					   snd, Boolean.parseBoolean(s.GetEntry("Grave.Sound Pitch Varies").toString()),
					   (String) pluginSettings.GetEntry("Settings.World"));
			
			graves.add(newGrave);
			if (pluginSettings.GetEntry("Settings.Debug.Verbose") == Boolean.TRUE){
				Bukkit.getServer().getLogger().log(Level.INFO, "[GraveSpawn] Registered Grave " + graveFile.getName() + " with the settings:");
				Bukkit.getServer().getLogger().log(Level.INFO, "		Title: " + s.GetEntry("Grave.Title"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		X: " + s.GetEntry("Grave.X"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Y: " + s.GetEntry("Grave.Y"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Z: " + s.GetEntry("Grave.Z"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Yaw: " + s.GetEntry("Grave.Yaw"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Pitch: " + s.GetEntry("Grave.Pitch"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Sound: " + s.GetEntry("Grave.Sound"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Varying Pitch: " + s.GetEntry("Grave.Sound Pitch Varies"));
				Bukkit.getServer().getLogger().log(Level.INFO, "		Grave World: " + newGrave.getWorld());
			}
		}
		
		return graves;
	}
	
}
