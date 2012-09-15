package me.jeremytrains.npcwh.api.event;

import me.jeremytrains.npcwh.NPCData;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCMoveEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private NPCData npc;
	private Player player;
	private Location oldLocation, newLocation;
	
	public NPCMoveEvent(NPCData n, Player p, Location newL, Location oldL) {
		npc = n;
		player = p;
		oldLocation = oldL;
		newLocation = newL;
	}
	
	public NPCData getNpc() {
		return npc;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Location getOldLocation() {
		return oldLocation;
	}
	
	public Location getNewLocation() {
		return newLocation;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}