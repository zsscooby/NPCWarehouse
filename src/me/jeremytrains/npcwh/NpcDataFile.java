package me.jeremytrains.npcwh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

import com.topcat.npclib.entity.*;

import org.bukkit.Location;
import org.bukkit.Material;

public class NpcDataFile {
	public File file = new File("plugins/NPCWarehouse/NpcData.txt");
	private static final int ID = 0, MESSAGE = 1, WORLD = 2, X = 3, Y = 4, Z = 5, YAW = 6, PITCH = 7, NAME= 8, ITEM = 9, LOOKAT = 10;
	NPCWarehouse plugin;
	NPCData[] data;
	public int totNpcs = 0, totWorlds = 0;
	private String[] worldsLoaded = new String[20];
	
	public NpcDataFile(NPCWarehouse instance) {
		plugin = instance;
		if (!file.exists()) {
			new File("plugins/NPCWarehouse").mkdir();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		data = plugin.npcs;
	}
	
	public void saveNpcData() {
		data = plugin.npcs;
		Formatter f = null;
		String sep = ":";
		try {
			f = new Formatter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (f == null) {
			plugin.log.warning(plugin.INTRO + "ERROR IN SAVING DATA: FORMATTER WAS BLANK!");
			return;
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				NPCData npc = data[i];
				Location loc = npc.getLocation();
				f.format("%s%n", npc.getId() + sep + npc.getMessage() + sep + loc.getWorld().getName() + sep + loc.getX() + sep + loc.getY() + sep + loc.getZ() + sep + loc.getYaw() + sep + loc.getPitch() + sep + npc.npc.getName() + sep + npc.npc.getInventory().getItemInHand().getTypeId());
			}
		}
		f.flush();
		f.close();
	}
	public NPCData[] loadNpcData() {
		Scanner scan = null;
		int nextNpc = 0;
		NPCData[] npcx = new NPCData[1000];
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (scan == null) {
			plugin.log.warning(plugin.INTRO + "Error in loading data! <The scanner was null!>");
		}
		int oldRegistries = 0;
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line != null) {
				String[] parts = line.split(":");
				if (parts[1] == null) {
					parts[1] = "Hello!";
				} else if (parts[1].equals("null")) {
					parts[1] = "Hello!";
				}
				
				//=== VERSION CHECK ===
				
				
				//If the file is in the v0.2 format (no yaw or pitch)
				if (parts.length == 8) {
					String[] old = parts;
					parts = new String[]{old[0], old[1], old[2], old[3], old[4], old[5], "0", "0", old[6], old[7]};
					oldRegistries++;
				}
				//If the file is in the v0.6.2 format (no lookat boolean)
				if (parts.length == 10) {
					String[] old = parts;
					parts = new String[]{old[0], old[1], old[2], old[3], old[4], old[5], old[6], old[7], old[8], old[9], "true"};
					oldRegistries++;
				}
				
				
				//=== END VERSION CHECK ===
				
				Location l = new Location(plugin.getServer().getWorld(parts[WORLD]), Double.parseDouble(parts[X]), Double.parseDouble(parts[Y]), Double.parseDouble(parts[Z]), Float.parseFloat(parts[YAW]), Float.parseFloat(parts[PITCH]));
				npcx[nextNpc] = new NPCData((HumanNPC)plugin.manager.spawnHumanNPC(parts[NAME], l, parts[ID]), parts[MESSAGE], Integer.parseInt(parts[ID]), l);
				npcx[nextNpc].setLookat(Boolean.parseBoolean(parts[LOOKAT]));
				if (!parts[ITEM].equals("0")) {
					npcx[nextNpc].npc.setItemInHand(Material.getMaterial(Integer.parseInt(parts[7])));
				}
				totNpcs++;
				boolean got = true;
				for (int i = 0; i < worldsLoaded.length; i++) {
					if (got) {
						if (worldsLoaded[i] != null) {
							if (worldsLoaded[i].equals(l.getWorld().getName())) {
								got = false;
							}
						}
					}
				}
				if (got) {
					totWorlds++;
				}
			}
			nextNpc++;
		}
		if (oldRegistries > 0) {
			plugin.log.info(plugin.INTRO + "Found and reformatted " + oldRegistries + " npc listing(s) in your NpcData file from an old version");
		}
		return npcx;
	}
}
