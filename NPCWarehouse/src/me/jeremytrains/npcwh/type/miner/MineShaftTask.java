package me.jeremytrains.npcwh.type.miner;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.jeremytrains.npcwh.NPCWarehouse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MineShaftTask extends MiningTask {
	Logger log = Logger.getLogger("Minecraft");

	public MineShaftTask(NPCWarehouse plugin, MinerNPCData npc, Location start, Player p) {
		super(plugin, npc, start, p);
	}
	
	@Override
	public void beginMining() {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				if (checkPickaxe() == false) {
					player.sendMessage(ChatColor.RED + npc.npc.getName() + " does not have a pickaxe to use!");
					npc.completeTask();
					return;
				}
				start.setX(start.getX() + 1);
				if (npc.getLocation().distance(start) > 10) {
					npc.npc.moveTo(start);
				} else {
					npc.npc.walkTo(start);
				}
				start.setX(start.getX() - 1);
				log.info("Moving to start location " + start.toString());
				boolean completed = false;
				int count = 1;
				Location l = start, skip = new Location(start.getWorld(), start.getBlockX() + 1, start.getBlockY(), start.getBlockZ() - 1);
				while(!completed) {
					npc.npc.walkTo(skip);
					log.info("l.getBlockZ=" + l.getBlockZ() + " l.getBlockZ-1=" + (l.getBlockZ()-1) + " l.getBlockZ+1=" + l.getBlockZ()+1);
					for (int i = l.getBlockZ() - 1; i <= l.getBlockZ() + 1; i++) { //z
						log.info("i(z)=" + i);
						for (int j = l.getBlockX() - 1; j <= l.getBlockX() + 1; j++) { //x
							log.info("j(x)=" + j);
							/*if (npc.npc.getInventory().getItemInHand().getType().equals(Material.AIR) || npc.npc.getInventory().getItemInHand().getDurability() == 0) {
								player.sendMessage(ChatColor.GOLD + npc.npc.getName() + "'s pickaxe has broken!");
								completed = true;
								break;
							}*/
							Location current = new Location(l.getWorld(), j, l.getY(), i);
							log.info("set current loc to " + current.toString());
							if (current.getBlock().getType().equals(Material.BEDROCK)) {
								player.sendMessage(ChatColor.GOLD + npc.npc.getName() + " has reached bedrock!");
								completed = true;
								break;
							}
							if (!current.equals(skip)) { // if the current block is supposed to be broken (not the skipped block)
								log.info("was not skip block");
								npc.npc.lookAtPoint(current);
								//npc.npc.getInventory().addItem(new ItemStack(current.getBlock().getType())); //add item to inv
								current.getBlock().setType(Material.AIR); // break block with no drops
								log.info("broke block");
								//npc.npc.getInventory().getItemInHand().setDurability((short)(npc.npc.getInventory().getItemInHand().getDurability() - 1)); //decrease durability
								log.info("set durability to " + (short)(npc.npc.getInventory().getItemInHand().getDurability() - 1));
								try {
									Thread.sleep(1000 * 2);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								log.info("finished sleeping");
							} else {
								log.info("was skip block");
							}
						}
					}
					l.setY(l.getBlockY() - 1);
					skip.setY(skip.getBlockY() - 1);
					log.info("adjusted locs; count = " + count);
					switch (count) {
						case 1: skip.setZ(skip.getBlockZ() + 1); break;
						case 2: skip.setZ(skip.getBlockZ() + 1); break;
						case 3: skip.setX(skip.getBlockX() - 1); break;
						case 4: skip.setX(skip.getBlockX() - 1); break;
						case 5: skip.setZ(skip.getBlockZ() - 1); break;
						case 6: skip.setZ(skip.getBlockZ() - 1); break;
						case 7: skip.setX(skip.getBlockX() + 1); break;
						case 8: {
							skip.setX(skip.getBlockX() + 1);
							count = 0;
							 break;
						}
						default: {
							player.sendMessage(ChatColor.RED + "An error has occurred with your npc's mining task. Check server log for error report");
							Logger.getLogger("Minecraft").log(Level.SEVERE, plugin.INTRO + "An error has occurred with " + npc.npc.getName() + "'s mining task " + this.getClass().toString() + ". Please tell the plugin owner that the count was: " + count);
							npc.completeTask();
							 break;
						}
					}
					count++;
					log.info("count was set to " + count);
				}
				player.sendMessage(ChatColor.YELLOW + npc.npc.getName() + "'s" + ChatColor.GREEN + " mining task has been completed!");
				npc.completeTask();
				
			}
			
		});
	}
	
	private boolean checkPickaxe() {
		if(npc.npc.getInventory().getItemInHand().getType().equals(Material.WOOD_PICKAXE) || npc.npc.getInventory().getItemInHand().getType().equals(Material.GOLD_PICKAXE) || npc.npc.getInventory().getItemInHand().getType().equals(Material.IRON_PICKAXE) || npc.npc.getInventory().getItemInHand().getType().equals(Material.DIAMOND_PICKAXE) || npc.npc.getInventory().getItemInHand().getType().equals(Material.STONE_PICKAXE)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MineShaftTask$" + npc.npc.getName() + "$" + player.getName();
	}
}
