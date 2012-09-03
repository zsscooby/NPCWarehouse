package me.jeremytrains.npcwh.type.miner;

import org.bukkit.Location;

import com.topcat.npclib.entity.HumanNPC;

import me.jeremytrains.npcwh.NPCData;

public class MinerNPCData extends NPCData {
	private boolean hasTask = false;
	private MiningTask task;
	
	public MinerNPCData(HumanNPC npc, String message, int id, Location l, String owner) {
		super(npc, message, id, l, owner);
	}
	
	public boolean hasTask() {
		return hasTask;
	}
	
	public void assignAndBeginTask(MiningTask task) {
		this.task = task;
		this.task.beginMining();
	}
	
	public void completeTask() {
		hasTask = false;
		task = null;
	}
}
