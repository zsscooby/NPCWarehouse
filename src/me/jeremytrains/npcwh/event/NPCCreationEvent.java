package me.jeremytrains.npcwh.event;

import me.jeremytrains.npcwh.NPCData;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCCreationEvent extends Event {
	private static final long serialVersionUID = -8108685956288694847L;
	private static final HandlerList handlers = new HandlerList();
	
	private NPCData npc;
	private Player player;
	private boolean crafted;
	
	public NPCCreationEvent(NPCData n, Player p, boolean c) {
		npc = n;
		player = p;
		crafted = c;
	}
	
	public NPCData getNpc() {
		return npc;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean wasCrafted() {
		return crafted;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
