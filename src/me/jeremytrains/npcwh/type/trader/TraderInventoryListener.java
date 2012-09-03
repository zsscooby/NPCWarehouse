package me.jeremytrains.npcwh.type.trader;

import me.jeremytrains.npcwh.NPCWarehouse;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TraderInventoryListener implements Listener {
	NPCWarehouse plugin;
	
	public TraderInventoryListener(NPCWarehouse inst) {
		plugin = inst;
	}
	
	@EventHandler
	public void inventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent event) {
		Player player = Bukkit.getPlayerExact(event.getWhoClicked().getName());
		if (player == null)
			return;
		if (TraderTask.isPlayerInTask(player) == false)
			return;
		TraderTask task = TraderTask.getPlayerTask(player);
		TraderNPCData trader = task.getTrader();
		if (task.getTrader().isStockUnlimited())
			event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() + event.getCursor().getAmount());
	}
}
