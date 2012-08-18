package me.jeremytrains.npcwh;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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

                }
            }
        }
    }
	
	@EventHandler
	public void onEntityDeath(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
		if (plugin.manager.isNPC(event.getEntity())) {
			if (event.getDamager() instanceof Player) {
				if (NPCWarehouse.playerHasPermission((Player)event.getDamager(), "NPCWarehouse.kill") == false) {
					event.setCancelled(true);
					((Player)event.getDamager()).sendMessage(ChatColor.RED + "You do not have permission to hurt NPCs");
				}
			}
		}
	}
}
