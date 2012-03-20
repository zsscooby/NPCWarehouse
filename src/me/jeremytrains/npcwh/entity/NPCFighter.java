package me.jeremytrains.npcwh.entity;

import me.jeremytrains.npcwh.NPCWarehouse;
import net.minecraft.server.EntityMonster;

import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Faction;

public class NPCFighter extends NPCMob {
	private Faction faction;
	private NPCWarehouse plugin;
	
	public NPCFighter(NPCMobEntity npcEntity, Faction f, JavaPlugin instance) {
		super(npcEntity);
		faction = f;
		plugin = (NPCWarehouse)instance;
		final NPCFighter npc = this;
		this.getBukkitEntity().getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run() {
				
			}
		}, 20, 20);
	}
}
