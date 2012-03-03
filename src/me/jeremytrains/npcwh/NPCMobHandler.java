package me.jeremytrains.npcwh;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.jeremytrains.npcwh.entity.NPCMob;

public class NPCMobHandler {
	NPCWarehouse plugin;
	private int currentMobs = 0;
	
	
	public NPCMobHandler(NPCWarehouse instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (ConfigFile.enableMobs) {
					final World world = ConfigFile.mobWorld;
					final int max = ConfigFile.maxMobs;
					if (currentMobs < max) {
						final Player target = world.getPlayers().get(new Random().nextInt());
						final Location loc = target.getLocation();
						if (target != null) {
							//plugin.manager.spawnMobNPC(new Location(world, loc.getBlockX() - 20, loc.getBlockY() - 20, loc.getBlockY() - 20), String.valueOf(currentMobs));
						}
					}
				}
			}
		}, 20, 20);
	}
	
	public NPCMob spawnMobNpc(Location l, String id) {
		//return plugin.manager.spawnMobNPC(l, id);
		return null;
	}
}
