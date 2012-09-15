package me.jeremytrains.npcwh;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**This is the class that handles all npc "crafting"*/
public class BlockList implements Listener {
	
	private NPCWarehouse plugin;
	
	public BlockList(NPCWarehouse instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Location loc = event.getBlock().getLocation();
		Location craftingLoc = null;
		
		if (event.getBlock().getType().equals(Material.SAND)) {
			if (!loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType().equals(Material.WOOL)) {
				return;
			}
			craftingLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
		} else if (event.getBlock().getType().equals(Material.WOOL)) {
			if (!loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()).getType().equals(Material.SAND)) {
				return;
			}
			craftingLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		} else {
			return;
		}
		
		if (!NPCWarehouse.playerHasPermission(event.getPlayer(), "NPCWarehouse.craft")) {
			return;
		}
		
		loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).setType(Material.AIR);
		loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).setType(Material.AIR);
		
		plugin.commandHandler.createNPC("CraftedNPC", craftingLoc, "I was crafted from the sand and wool of " + event.getPlayer().getDisplayName(), true, null);
	}
}
