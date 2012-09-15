package me.jeremytrains.npcwh;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;
import com.topcat.npclib.nms.NpcEntityTargetEvent;
import com.topcat.npclib.nms.NpcEntityTargetEvent.NpcTargetReason;

public class PlList implements Listener {
	NPCWarehouse plugin;
	
	public PlList(NPCWarehouse instance) {
		plugin = instance;
	}
	
	@EventHandler
    public void onEntityTarget(EntityTargetEvent event) {

        if (event instanceof NpcEntityTargetEvent) {
            NpcEntityTargetEvent nevent = (NpcEntityTargetEvent)event;
            NPC n = plugin.manager.getNPC(plugin.manager.getNPCIdFromEntity(event.getEntity()));
            if (n instanceof HumanNPC == false) {
            	return;
            }
            HumanNPC npc = (HumanNPC)n;

            if (npc != null && event.getTarget() instanceof Player) {
                if (nevent.getNpcReason() == NpcTargetReason.CLOSEST_PLAYER || nevent.getNpcReason() == NpcTargetReason.NPC_BOUNCED) {
                    Player p = (Player) event.getTarget();
                    if (plugin.getNpcInfo(npc).isFollowingPlayer() != null) {
                    	if (plugin.getNpcInfo(npc).isFollowingPlayer().equals(p)) {
                    		return;
                    	}
                    }
                    plugin.getNpcInfo(npc).chat(p);
                    if(plugin.getNpcInfo(npc).getLookAt()) {
                    	npc.lookAtPoint(p.getLocation());
                    }
                    event.setCancelled(true);
                    
                } else if (nevent.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
                    Player p = (Player) event.getTarget();
                    if (plugin.selected.containsKey(p)) {
                    	if (plugin.selected.get(p).equals(String.valueOf(plugin.getNpcInfo(npc).getId()))) {
                    		plugin.getNpcInfo(npc).chat(p);
                    	}
                    } else {
                    	if (NPCWarehouse.playerHasPermission(p, "NPCWarehouse.command.select") && ConfigFile.rightClickSelect) {
	                    	plugin.selected.put(p, String.valueOf(plugin.getNpcInfo(npc).getId()));
	                    	p.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + npc.getName() + " <ID: " + plugin.getNpcInfo(npc).getId() + ">");
                    	} else {
                    		plugin.getNpcInfo(npc).chat(p);
                    	}
                    }
                    event.setCancelled(true);

                }
            }
        }
    }
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (plugin.manager.isNPC(event.getEntity())) {
			if (event.getDamager() instanceof Player) {
				if (NPCWarehouse.playerHasPermission((Player)event.getDamager(), "NPCWarehouse.kill") == false) {
					event.setCancelled(true);
					((Player)event.getDamager()).sendMessage(ChatColor.RED + "You do not have permission to hurt NPCs");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		//=== LOOKAT CHECK ===
		List<Entity> nEnt= event.getPlayer().getNearbyEntities(5, 5, 5);
		if (nEnt.isEmpty())
			return;
		Iterator<Entity> it = nEnt.iterator();
		while (it.hasNext()) {
			Entity ent = it.next();
			if (plugin.manager.isNPC(ent)) {
				HumanNPC npc = (HumanNPC)plugin.manager.getNPC(plugin.manager.getNPCIdFromEntity(ent));
				NPCData data = plugin.getNpcInfo(npc);
				if (data.getLookAt()) {
					npc.lookAtPoint(event.getPlayer().getEyeLocation());
				}
			}
		}
		
		//=== FOLLOW CHECK ===
		for (int i = 0; i < plugin.npcs.length; i++) {
			if (plugin.npcs[i] != null) {
				if (plugin.npcs[i].isFollowingPlayer() != null) {
					if (plugin.npcs[i].isFollowingPlayer().equals(event.getPlayer())) {
						Location l = event.getPlayer().getLocation();
						l.setX(l.getX() - 1);
						if (!l.getBlock().getType().equals(Material.AIR)) {
							l.setX(l.getX() + 1);
							l.setZ(l.getZ() - 1);
						} else if (!l.getBlock().getType().equals(Material.AIR)) {
							l.setX(l.getX() + 1);
							l.setZ(l.getZ() + 1);
						} else if (!l.getBlock().getType().equals(Material.AIR)) {
							l.setX(l.getX() - 1);
							l.setZ(l.getZ() + 1);
						} else {
							l = event.getPlayer().getLocation();
						}
						plugin.npcs[i].npc.walkTo(l);
					}
				}
			}
		}
	}
}
