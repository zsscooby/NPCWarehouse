package me.jeremytrains.npcwh.type.miner;

import me.jeremytrains.npcwh.NPCWarehouse;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MiningTask {
	
	final protected Location start;
	final protected MinerNPCData npc;
	protected final NPCWarehouse plugin;
	final protected Player player;
	
	protected MiningTask(NPCWarehouse plugin, MinerNPCData npc, Location start, Player p) {
		this.start = start;
		this.npc = npc;
		this.plugin = plugin;
		player = p;
	}
	
	public void beginMining() {
		
	}
}
