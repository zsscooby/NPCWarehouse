package me.jeremytrains.npcwh.api;

import me.jeremytrains.npcwh.NPCData;
import me.jeremytrains.npcwh.NPCWarehouse;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.topcat.npclib.entity.*;

public class API {
	NPCWarehouse plugin;
	
	public API(NPCWarehouse m) {
		plugin = m;
	}
	
	public NPCData createNpc(String name, Location loc, String message) {
		int id = plugin.commandHandler.createNPC(name, loc, message, false, null);
		return plugin.npcs[id];
	}
	
	public NPCData createNpc(String name, Location loc) {
		return createNpc(name, loc, "Hello!");
	}
	
	public void removeNpc(int id) {
		plugin.commandHandler.removeNPC(plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(String.valueOf(id))));
	}
	
	public void moveNpc(String id, Location l) {
		NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(id));
		
		npc.npc.moveTo(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
		
		for (int i = 0; i < plugin.npcs.length; i++) {
			if (plugin.npcs[i] != null) {
				if (plugin.npcs[i].equals(plugin.getNpcInfo(npc.npc))) {
					plugin.npcs[i].setLocation(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()));
				}
			}
		}
	}
	
	public NPCData[] getNpcList() {
		return plugin.npcs;
	}
	
	/**Returns the npc's id*/
	public String getSelectedNpc(Player p) {
		return plugin.selected.get(p);
	}
	
	/**Return's the npc with the id given*/
	public NPCData getNpcById(String id) {
		return plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(id));
	}
	
	public NPCData getNpcInfo(HumanNPC e) {
		return plugin.getNpcInfo(e);
	}
	
	public void remNpc(NPCData npc) {
		plugin.manager.despawnById(String.valueOf(npc.getId()));
		for (int i = 0; i < plugin.npcs.length; i++) {
			if (plugin.npcs[i] != null) {
				if (plugin.npcs[i].npc.equals(npc)) {
					plugin.npcs[i] = null;
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean isSpoutSupportEnabled() {
		return plugin.useSpout;
	}
}
