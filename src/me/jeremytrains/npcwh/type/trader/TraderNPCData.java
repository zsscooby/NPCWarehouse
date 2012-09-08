package me.jeremytrains.npcwh.type.trader;

import java.util.ArrayList;

import org.bukkit.Location;

import com.topcat.npclib.entity.HumanNPC;

import me.jeremytrains.npcwh.NPCData;

public class TraderNPCData extends NPCData {
	private double money = 0.0;
	private boolean unlimitedMoney = false, unlimitedStock = false;
	private TraderTask task = null;
	private ArrayList<Stockable> stock = null;
	
	public TraderNPCData(HumanNPC npc, String message, int id, Location l, String owner) {
		super(npc, message, id, l, owner);
	}
	
	public double getMoney() {
		return money;
	}
	
	public boolean isMoneyUnlimited() {
		return unlimitedMoney;
	}
	
	public void setUnlimitedMoney(boolean b) {
		unlimitedMoney = b;
	}
	
	public boolean isStockUnlimited() {
		return unlimitedStock;
	}
	
	public void setUnlimitedStock(boolean b) {
		unlimitedStock = b;
	}
	
	public TraderTask getTask() {
		return task;
	}
	
	public void assignTask(TraderTask t) {
		if (task != null)
			return;
		task = t;
		task.setInventoryView(t.getPlayer().openInventory(t.getTrader().npc.getInventory()));
	}
	
	public ArrayList<Stockable> getStock() {
		return stock;
	}
	
	public void setStock(ArrayList<Stockable> s) {
		stock = s;
	}
}
