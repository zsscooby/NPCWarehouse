package me.jeremytrains.npcwh.type.trader;

import java.util.logging.Logger;

import me.jeremytrains.npcwh.NPCWarehouse;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class TraderInventoryListener implements Listener {
	NPCWarehouse plugin;
	Logger log = Logger.getLogger("Minecraft");
	
	public TraderInventoryListener(NPCWarehouse inst) {
		plugin = inst;
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		log.info("Click Event Called");
		return;
		/*
		if (!NPCWarehouse.useEconomy)
			return;
		Player player = Bukkit.getPlayerExact(event.getWhoClicked().getName());
		if (player == null)
			return;
		if (TraderTask.isPlayerInTask(player) == false)
			return;
		TraderTask task = TraderTask.getPlayerTask(player);
		TraderNPCData trader = task.getTrader();
		if (task.getTrader().isStockUnlimited())
			event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() + event.getCursor().getAmount());
		*/
	}
	
	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent event) {
		log.info("Close Event Called");
		return;
		/*
		if (!NPCWarehouse.useEconomy)
			return;
		Player player = Bukkit.getPlayerExact(event.getPlayer().getName());
		if (TraderTask.isPlayerInTask(player) == false)
			return;
		player.sendMessage(ChatColor.GOLD + "=== Trader Transaction Ended ===");
		*/
	}
}
