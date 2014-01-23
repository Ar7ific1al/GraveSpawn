package com.github.ar7ific1al.gravespawn.commands;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.util.org.apache.commons.lang3.EnumUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.github.ar7ific1al.gravespawn.Grave;
import com.github.ar7ific1al.gravespawn.Plugin;
import com.github.ar7ific1al.gravespawn.utils.PlayerSettingsFile;
import com.github.ar7ific1al.gravespawn.utils.SettingsFile;

public class GraveSpawnCommandExecutor implements CommandExecutor {

	Plugin plugin;

	public GraveSpawnCommandExecutor(Plugin instance) {
		plugin = instance;
		Plugin.console.log(Level.INFO,
				"[Gravespawn] Command executor registered.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (sender instanceof Player) {
			if (label.equalsIgnoreCase("setgrave")
					&& sender.hasPermission("worldgrave.admin")) {
				try {
					if (args.length < 5) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawn] "
								+ ChatColor.YELLOW
								+ "Proper usage is as follows.\n"
								+ ChatColor.RED
								+ "/newgrave GraveName UseYaw(true/false) UsePitch(true/false) PitchVaries(true/false) SoundName\n"
								+ ChatColor.YELLOW
								+ "For example: "
								+ ChatColor.RED
								+ "/newgrave CoolGrave true false true wither_spawn");
					} else if (args.length == 5) {
						String graveTitle = args[0];
						boolean useYaw = Boolean.parseBoolean(args[1]);
						boolean usePitch = Boolean.parseBoolean(args[2]);
						float yaw = 0;
						float pitch = 0;
						Sound sound = null;
						boolean varyPitch = false;

						SettingsFile graveFile = new SettingsFile(graveTitle
								+ ".yml", Plugin.GravesDir);
						if (args[3].equalsIgnoreCase("true")) {
							varyPitch = true;
						} else if (args[3].equalsIgnoreCase("false")) {
							varyPitch = false;
						} else {
							sender.sendMessage(ChatColor.DARK_RED
									+ "[GraveSpawn] "
									+ ChatColor.YELLOW
									+ "The fourth argument must be either true or false.");
							return true;
						}
						if (EnumUtils.isValidEnum(Sound.class,
								args[4].toUpperCase())) {
							sound = Sound.valueOf(args[4].toUpperCase());
						} else if (args[4].equalsIgnoreCase("null")) {
							sound = null;
						} else {
							sender.sendMessage(ChatColor.DARK_RED
									+ "[GraveSpawn] "
									+ ChatColor.YELLOW
									+ "The sound you specified is invalid or does not exist. Check your spelling.\n"
									+ ChatColor.YELLOW
									+ "For a list of valid sounds, visit\n "
									+ ChatColor.RED
									+ "http://tinyurl.com/Bukkit-Sound-Enum");
							return true;
						}

						if (useYaw) {
							yaw = ((Player) sender).getLocation().getYaw();
						}
						if (usePitch) {
							pitch = ((Player) sender).getLocation().getPitch();
						}
						graveFile.AddEntry("Grave.Title", graveTitle);
						graveFile.AddEntry("Grave.X",
								(((Player) sender).getLocation()).getX());
						graveFile.AddEntry("Grave.Y", ((Player) sender)
								.getLocation().getY());
						graveFile.AddEntry("Grave.Z", ((Player) sender)
								.getLocation().getZ());
						graveFile.AddEntry("Grave.Yaw", yaw);

						graveFile.AddEntry("Grave.Pitch", pitch);
						if (sound != null) {
							graveFile.AddEntry("Grave.Sound", sound.name());
						} else {
							graveFile.AddEntry("Grave.Sound", "NULL");
						}
						graveFile.AddEntry("Grave.Sound Pitch Varies",
								varyPitch);

						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawnAdmin] " + ChatColor.YELLOW
								+ "New grave created with the "
								+ "the following settings.\n" + ChatColor.GREEN
								+ "Title: " + graveTitle + "\n"
								+ ChatColor.GREEN + "X: "
								+ ((Player) sender).getLocation().getX() + "\n"
								+ ChatColor.GREEN + "Y: "
								+ ((Player) sender).getLocation().getY() + "\n"
								+ ChatColor.GREEN + "Z: "
								+ ((Player) sender).getLocation().getZ() + "\n"
								+ ChatColor.GREEN + "Yaw: "
								+ ((Player) sender).getLocation().getYaw()
								+ "\n" + ChatColor.GREEN + "Pitch: "
								+ ((Player) sender).getLocation().getPitch()
								+ "\n" + ChatColor.GREEN + "Sound: " + sound
								+ "\n" + ChatColor.GREEN
								+ "Sound Pitch Varies: " + varyPitch);
						Plugin.console.log(Level.WARNING,
								"[Gravespawn] Refreshing graves!");
						Plugin.Graves = Grave.RefreshGraveList();
					}
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
			} else if (label.equalsIgnoreCase("gravespawn")) {

				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_RED + "[GraveSpawn] "
							+ ChatColor.YELLOW + "GraveSpawn version "
							+ plugin.version + " written by Ar7ific1al.");
					sender.sendMessage(ChatColor.DARK_RED + "[GraveSpawn] "
							+ ChatColor.YELLOW
							+ "If you don't want to spawn in graves, use "
							+ ChatColor.RED + "/gravespawn off");
					if (sender.hasPermission("gravespawn.admin")) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawnAdmin] "
								+ ChatColor.YELLOW
								+ "You can create and update graves, and set the grave world. See "
								+ ChatColor.RED + "/setgrave"
								+ ChatColor.YELLOW + " and " + ChatColor.RED
								+ "/gravespawn world" + ChatColor.YELLOW
								+ " for more information. You may also refresh the graves list with "
								+ ChatColor.RED + "/gravespawn refresh");
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("off")) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawn] "
								+ ChatColor.YELLOW
								+ "Your grave spawn has been disabled. You will no longer spawn in graves. To enable, use"
								+ ChatColor.RED + "/gravespawn on");
						try {
							toggleUserGraveSpawn(sender.getName());
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}
					} else if (args[0].equalsIgnoreCase("on")) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawn] "
								+ ChatColor.YELLOW
								+ "Your grave spawn has been enabled. You will now respawn in graves. To disable, use"
								+ ChatColor.RED + "/gravespawn off");
						try {
							toggleUserGraveSpawn(sender.getName());
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}
					} else if (args[0].equalsIgnoreCase("world")
							&& sender.hasPermission("gravespawn.admin")) {
						File settings = new File(plugin.getDataFolder(),
								"settings.yml");
						FileConfiguration tmpfc = new YamlConfiguration();
						try {
							tmpfc.load(settings);
							String world = tmpfc.getString("Settings.World");
							sender.sendMessage(ChatColor.DARK_RED
									+ "[GraveSpawn] " + ChatColor.YELLOW
									+ "The current grave world is "
									+ ChatColor.RED + world);
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}

					} else if (args[0].equalsIgnoreCase("refresh")
						&& sender.hasPermission("gravespawn.admin")){
						sender.sendMessage(ChatColor.DARK_RED + "[GraveSpawnAdmin] " + ChatColor.YELLOW
								+ "Refreshing graves list...");
						try {
							Plugin.Graves = Grave.RefreshGraveList();
							sender.sendMessage(ChatColor.DARK_RED + "[GraveSpawnAdmin] " + ChatColor.YELLOW
									+ "Grave list refreshed successfully!");
						} catch (IOException | InvalidConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("world")) {
						if (plugin.getServer().getWorld(args[1]) != null) {
							try {
								SettingsFile settings = new SettingsFile(
										"settings.yml", plugin.getDataFolder());
								settings.AddEntry("Settings.World", plugin
										.getServer().getWorld(args[1])
										.getName());
								sender.sendMessage(ChatColor.DARK_RED
										+ "[GraveSpawnAdmin] "
										+ ChatColor.YELLOW
										+ "Grave world updated. Grave world is currently "
										+ ChatColor.RED + args[1]);
							} catch (IOException
									| InvalidConfigurationException e) {
								e.printStackTrace();
							}
						} else {
							sender.sendMessage(ChatColor.DARK_RED
									+ "[GraveSpawn] "
									+ ChatColor.YELLOW
									+ "The world you specified is invalid. Check your spelling. The world name cannot have spaces.");
						}
					}
				}
				try {

				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			} else if (label.equalsIgnoreCase("tpgrave")
					&& sender.hasPermission("gravespawn.admin")) {
				if (args.length == 1) {
					Grave grave = null;
					for (Grave g : Plugin.Graves) {
						if (g.getTitle().equalsIgnoreCase(args[0])) {
							grave = g;
							break;
						}
					}
					if (grave == null) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawnAdmin] "
								+ ChatColor.RED
								+ "No grave by that name found. Check your spelling, or see the graves list at "
								+ ChatColor.YELLOW + "/graves");
						return false;
					} else {
						double x, y, z;
						float yaw, pitch;
						World graveWorld;
						x = grave.getX();
						y = grave.getY();
						z = grave.getZ();
						yaw = grave.getYaw();
						pitch = grave.getPitch();
						graveWorld = Bukkit.getServer().getWorld(grave.getWorld());
						Location l = new Location(graveWorld, x, y, z, yaw,
								pitch);
						Bukkit.getServer().broadcastMessage(l.toString());
						((Player) sender).teleport(l, TeleportCause.PLUGIN);
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawnAdmin] " + ChatColor.RED
								+ "Teleported to grave " + ChatColor.YELLOW
								+ grave.getTitle());
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED
							+ "[GraveSpawnAdmin] " + ChatColor.RED
							+ "Please specify a grave.");
				}
			} else if (label.equalsIgnoreCase("graves")) {
				String separator = ", ";
				String gravesList = "";
				for (int i = 0; i < Plugin.Graves.size(); i++) {
					gravesList += Plugin.Graves.get(i).getTitle();
					if (i != Plugin.Graves.size() - 1) {
						gravesList += separator;
					}
				}
				if (sender.hasPermission("gravespsawn.admin")) {
					sender.sendMessage(ChatColor.DARK_RED
							+ "[GraveSpawnAdmin] " + ChatColor.RED
							+ "Here is a list of all of this server's graves.");
					sender.sendMessage(gravesList);
				}
			} else if (label.equalsIgnoreCase("grave")
					&& sender.hasPermission("gravespawn.admin")) {
				if (args.length == 2) {
					Grave grave = null;
					for (Grave g : Plugin.Graves) {
						if (g.getTitle().equalsIgnoreCase(args[0])) {
							grave = g;
							break;
						}
					}
					if (grave == null) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawnAdmin] " + ChatColor.RED
								+ "Grave by the name " + ChatColor.YELLOW
								+ args[0] + ChatColor.RED + " not found.");
						return true;
					}
					if (args[1].equalsIgnoreCase("delete")) {
						try {
							SettingsFile d = new SettingsFile(grave.getTitle()
									+ ".yml", Plugin.GravesDir);
							d.DeleteFile();
							sender.sendMessage(ChatColor.DARK_RED
									+ "[GraveSpawnAdmin] " + ChatColor.RED
									+ "Grave " + ChatColor.YELLOW
									+ grave.getTitle() + ChatColor.RED
									+ " successfully removed. Refreshing graves list.");
							Plugin.console.log(Level.WARNING,
									"[Gravespawn] Player " + sender.getName() + " removed grave " + grave.getTitle() + ". Refreshing graves!");
							Plugin.Graves = Grave.RefreshGraveList();
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}
					}
				} else if (args.length == 1) {
					Grave grave = null;
					for (Grave g : Plugin.Graves) {
						if (g.getTitle().equalsIgnoreCase(args[0])) {
							grave = g;
							break;
						}
					}
					if (grave == null) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "[GraveSpawnAdmin] " + ChatColor.RED
								+ "Grave by the name " + ChatColor.YELLOW
								+ args[0] + ChatColor.RED + " not found.");
						return true;
					}
					sender.sendMessage(ChatColor.DARK_RED
							+ "[GraveSpawnAdmin] " + ChatColor.YELLOW
							+ grave.getTitle() + ChatColor.RED
							+ "'s settings are as follows.\n" + ChatColor.GREEN
							+ "Title: " + grave.getTitle() + "\n"
							+ ChatColor.GREEN + "X: " + grave.getX() + "\n"
							+ ChatColor.GREEN + "Y: " + grave.getY() + "\n"
							+ ChatColor.GREEN + "Z: " + grave.getZ() + "\n"
							+ ChatColor.GREEN + "Yaw: " + grave.getYaw() + "\n"
							+ ChatColor.GREEN + "Pitch: " + grave.getPitch()
							+ "\n" + ChatColor.GREEN + "Sound: "
							+ grave.getSound().name() + "\n" + ChatColor.GREEN
							+ "Sound Pitch Varies: "
							+ grave.getPitchVariation());
				}
			}
			return true;
		}
		return false;
	}

	private void toggleUserGraveSpawn(String playerName) throws IOException,
			InvalidConfigurationException {
		PlayerSettingsFile playerFile = new PlayerSettingsFile(playerName,
				Plugin.PlayersDir);
		boolean enabled = Boolean.parseBoolean(playerFile.GetEntry("Enabled")
				.toString());
		if (enabled)
			enabled = false;
		else
			enabled = true;
		playerFile.AddEntry("Enabled", enabled);
	}
}
