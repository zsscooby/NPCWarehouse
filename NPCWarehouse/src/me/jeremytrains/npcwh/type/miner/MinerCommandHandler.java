package me.jeremytrains.npcwh.type.miner;

import java.util.logging.Logger;

import me.jeremytrains.npcwh.NPCData;
import me.jeremytrains.npcwh.NPCTypeManager;
import me.jeremytrains.npcwh.NPCWarehouse;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinerCommandHandler implements CommandExecutor {
	private Logger log = Logger.getLogger("Minecraft");
	private NPCWarehouse plugin;
	
	public MinerCommandHandler(NPCWarehouse inst) {
		plugin = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		try {
			player = (Player)sender;
		} catch (Exception e) {
			log.info(plugin.INTRO + "You have to be a player to access that command!");
			return true;
		}
		
		if (args.length == 0) {
			return false;
		}
		
		if (args[0].equalsIgnoreCase("help")) {
			showHelp(player);
		} else if (args[0].equalsIgnoreCase("toggle")) {
			NPCData npc = plugin.getNpcInfo(plugin.getPlayersSelectedNpc(player));
			if (npc instanceof MinerNPCData) {
				player.sendMessage(ChatColor.RED + npc.npc.getName() + " is already a miner!");
				return true;
			}
			NPCTypeManager.convertToMiner(npc);
			player.sendMessage(ChatColor.YELLOW + npc.npc.getName() + ChatColor.GREEN + " is now a miner");
		} else if (args[0].equalsIgnoreCase("mine")) {
			if (args.length < 2) {
				return false;
			}
			NPCData npc = plugin.getNpcInfo(plugin.getPlayersSelectedNpc(player));
			if (npc instanceof MinerNPCData == false) {
				player.sendMessage(ChatColor.RED + npc.npc.getName() + " is not a miner!");
				return true;
			}
			MinerNPCData miner = (MinerNPCData)npc;
			if (!args[1].equalsIgnoreCase("shaft")) {
				 player.sendMessage(ChatColor.RED + "Invalid Mining Task type");
				 return true;
			}
			if (args[1].equalsIgnoreCase("shaft")) {
				miner.assignAndBeginTask(new MineShaftTask(plugin, miner, player.getLocation(), player));
			}
		} else {
			return false;
		}
		return true;
	}
	
	private void showHelp(Player player) {
		ChatColor ge = ChatColor.GREEN, go = ChatColor.GOLD, ye = ChatColor.YELLOW, bl = ChatColor.BLUE;
		player.sendMessage(go + "===== NPCWarehouse Miner Help =====");
		player.sendMessage(bl + "[] are required, <> are optional");
		player.sendMessage(ye + "/miner help -- " + ge + "Shows this help page");
		player.sendMessage(ye + "/miner toggle -- " + ge + "Converts the selected NPC into a miner");
		player.sendMessage(ye + "/miner mine [task] -- " + ge + "Gives the selected Miner the specified task at your location");
		player.sendMessage(go + "====================================");
	}
}
