package me.jeremytrains.npcwh.api.event;

import me.jeremytrains.npcwh.NPCData;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCPropertyChangeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private NPCData npc;
	private Player player;
	private Property property;
	private Object value;
	
	public enum Property {
		LOOKAT,
		NAME,
		OWNER,
		MESSAGE,
		CAPE,
		SKIN
	}
	
	public NPCPropertyChangeEvent(NPCData n, Player p, Property prop, Object val) {
		npc = n;
		player = p;
		property = prop;
		value = val;
	}
	
	public NPCData getNpc() {
		return npc;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Property getProperty() {
		return property;
	}
	
	public Object getValue() {
		return value;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
