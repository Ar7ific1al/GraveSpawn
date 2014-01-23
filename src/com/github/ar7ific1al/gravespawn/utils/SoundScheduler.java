package com.github.ar7ific1al.gravespawn.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class SoundScheduler implements Runnable {

	private World world = null;
	private Location respawnLocation = null;
	private Sound sound = null;
	float Pitch = 1;

	public SoundScheduler(World w, Location l, Sound s, boolean varyPitch) {
		world = w;
		respawnLocation = l;
		sound = s;
		if (varyPitch) {
			Pitch = com.github.ar7ific1al.gravespawn.Plugin.random.nextFloat() + 0.5f;
		}
	}

	public void run() {
		world.playSound(respawnLocation, sound, 10, Pitch);
	}

}
