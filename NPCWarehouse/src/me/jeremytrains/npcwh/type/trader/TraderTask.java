package me.jeremytrains.npcwh.type.trader;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class TraderTask {
	private Player player;
	private TraderNPCData npc;
	private boolean stocking;
	private double moneyOwed = 0.0;
	public static ArrayList<TraderTask> tasks = new ArrayList<TraderTask>();
	private InventoryView inv;
	
	public TraderTask(Player p, TraderNPCData t, boolean s) {
		player = p;
		npc = t;
		stocking = s;
		addTask(this);
	}
	
	public void setInventoryView(InventoryView iv) {
		inv = iv;
	}
	
	public InventoryView getInventoryView() {
		return inv;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public TraderNPCData getTrader() {
		return npc;
	}
	
	public boolean isStocking() {
		return stocking;
	}
	
	public double getMoneyOwed() {
		return moneyOwed;
	}
	
	public void setMoneyOwed(double d) {
		moneyOwed = d;
	}
	
	private static void addTask(TraderTask t) {
		t.getPlayer().sendMessage(ChatColor.GOLD + "=== Trader Transaction Started ===");
		if (tasks.contains(t))
			return;
		tasks.add(t);
	}
	
	public static boolean isPlayerInTask(Player p) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getPlayer().equals(p)) {
				return true;
			}
		}
		return false;
	}
	
	public static TraderTask getPlayerTask(Player p) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getPlayer().equals(p)) {
				return tasks.get(i);
			}
		}
		return null;
	}
	
	public static boolean isNPCInTask(TraderNPCData t) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getTrader().equals(t)) {
				return true;
			}
		}
		return false;
	}
	
	public static TraderTask getNpcsTask(TraderNPCData t) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getTrader().equals(t)) {
				return tasks.get(i);
			}
		}
		return null;
	}
}
