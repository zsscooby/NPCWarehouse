package me.jeremytrains.npcwh.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCDeletionEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private String npcId;
	private Player player;
	
	public NPCDeletionEvent(String id, Player p) {
		npcId = id;
		player = p;
	}
	
	public String getNpcId() {
		return npcId;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
