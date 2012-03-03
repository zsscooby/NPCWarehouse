package me.jeremytrains.npcwh;


import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.nms.NPCEntity;

public class MobNPCManager extends NPCManager {
	public static JavaPlugin plugin;

	public MobNPCManager(JavaPlugin plug) {
		super(plug);
		plugin = plug;
	}
	
	public boolean isMobNPC(org.bukkit.entity.Entity e) {
		return ((CraftEntity) e).getHandle() instanceof NPCEntity;
	}
}
