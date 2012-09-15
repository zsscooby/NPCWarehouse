package me.jeremytrains.npcwh;

import com.topcat.npclib.entity.HumanNPC;

import me.jeremytrains.npcwh.type.miner.MinerNPCData;
import me.jeremytrains.npcwh.type.trader.TraderNPCData;

public class NPCTypeManager {
	
	
	public static String getTypeString(NPCData npc) {
		if (npc instanceof MinerNPCData) {
			return "miner";
		} else if (npc instanceof TraderNPCData) {
			return "trader";
		} else {
			return "regular";
		}
	}
	
	public static boolean isMiner(NPCData npc) {
		if (npc instanceof MinerNPCData) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMiner(HumanNPC npc) {
		NPCData npcD = NPCWarehouse.getNPCWarehouse().getNpcInfo(npc);
		if (npcD instanceof MinerNPCData) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTrader(NPCData npc) {
		if (npc instanceof TraderNPCData) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTrader(HumanNPC npc) {
		NPCData npcD = NPCWarehouse.getNPCWarehouse().getNpcInfo(npc);
		if (npcD instanceof TraderNPCData) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isRegular(NPCData npc) {
		if (npc instanceof MinerNPCData == false && npc instanceof TraderNPCData == false) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isRegular(HumanNPC npc) {
		NPCData npcD = NPCWarehouse.getNPCWarehouse().getNpcInfo(npc);
		if (npcD instanceof MinerNPCData == false && npcD instanceof TraderNPCData == false) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void convertToMiner(NPCData data) {
		for (int i = 0; i < NPCWarehouse.getNPCWarehouse().npcs.length; i++) {
			if (NPCWarehouse.getNPCWarehouse().npcs[i] != null) {
				if (NPCWarehouse.getNPCWarehouse().npcs[i].equals(data)) {
					NPCWarehouse.getNPCWarehouse().npcs[i] = new MinerNPCData(data.npc, data.getMessage(), data.getId(), data.getLocation(), data.getOwner());
				}
			}
		}
	}
	
	public static void convertToTrader(NPCData data) {
		for (int i = 0; i < NPCWarehouse.getNPCWarehouse().npcs.length; i++) {
			if (NPCWarehouse.getNPCWarehouse().npcs[i] != null) {
				if (NPCWarehouse.getNPCWarehouse().npcs[i].equals(data)) {
					NPCWarehouse.getNPCWarehouse().npcs[i] = new TraderNPCData(data.npc, data.getMessage(), data.getId(), data.getLocation(), data.getOwner());
				}
			}
		}
	}
}
