package me.jeremytrains.npcwh;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

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
                    p.sendMessage("<" + npc.getName() + "> " + plugin.getNpcInfo(npc).getMessage());
                    event.setCancelled(true);

                } else if (nevent.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
                    Player p = (Player) event.getTarget();
                    if (plugin.selected.containsKey(p)) {
                    	if (plugin.selected.get(p).equals(String.valueOf(plugin.getNpcInfo(npc).getId()))) {
                    		p.sendMessage("<" + npc.getName() + "> " + plugin.getNpcInfo(npc).getMessage());
                    	}
                    } else {
                    	if (NPCWarehouse.playerHasPermission(p, "NPCWarehouse.command.select") && ConfigFile.rightClickSelect) {
	                    	plugin.selected.put(p, String.valueOf(plugin.getNpcInfo(npc).getId()));
	                    	p.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + npc.getName() + " <ID: " + plugin.getNpcInfo(npc).getId() + ">");
                    	} else {
                    		p.sendMessage("<" + npc.getName() + "> " + plugin.getNpcInfo(npc).getMessage());
                    	}
                    }
                    event.setCancelled(true);

                }/* else if (nevent.getNpcReason() == NpcTargetReason.NPC_BOUNCED) {
                    Player p = (Player) event.getTarget();
                    p.sendMessage("<" + npc.getName() + "> Stop bouncing on me!");
                    event.setCancelled(true);
                }*/
            }
        }

    }
	
	/*@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		NPCData npc = getNearestNpc(event.getPlayer());
		
		if (npc != null) {
			npc.chat(event.getPlayer());
		}
	}
	
	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		if (e instanceof HumanNPC) {
			NPCData npc = plugin.getNpcInfo((HumanNPC)e);
			
			if (plugin.selected.containsKey(p)) {
				if (plugin.selected.get(p).equals(npc.getId())) {
					
				}
				npc.chat(p);
				return;
			} else {
				p.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW  + " <ID: " + npc.getId() + ">");
			}
		}
	}
	
	public NPCData getNearestNpc(Player p) {
		Location l = p.getLocation();
		if (l == null) {
			throw new IllegalArgumentException("l cannot be null!");
		}
		NPCData npc = null;
		int prev = 1000000000;
		final NPCData[] npcs = plugin.npcs;
		for (int i = 0; i < npcs.length; i++) {
			if (npcs[i] != null) {
				Location nl = npcs[i].getLocation();
				int a = (int)Math.sqrt((double)(((nl.getBlockX() - l.getBlockX()) ^ 2) + ((nl.getBlockY() - l.getBlockY()) ^ 2)));
				int b;
				if (a <= dist) {
					b = (int)Math.sqrt((double)((a ^ 2) + ((nl.getBlockZ() - l.getBlockZ()) ^ 2)));
					if (b <= dist) {
						if (b < prev) {
							npc = npcs[i];
						}
					}
				}
			}
		}
		return npc;
	}*/
}
