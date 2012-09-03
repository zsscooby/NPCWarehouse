package me.jeremytrains.npcwh;

import me.jeremytrains.npcwh.api.event.NPCCreationEvent;
import me.jeremytrains.npcwh.api.event.NPCDeletionEvent;
import me.jeremytrains.npcwh.api.event.NPCPropertyChangeEvent;
import me.jeremytrains.npcwh.api.event.NPCPropertyChangeEvent.Property;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.Spout;

import com.topcat.npclib.entity.*;

public class CommandHandler implements CommandExecutor {
	NPCWarehouse plugin;
	String[] validCmds = new String[]{"create", "rename", "move", "help", "select", "remove", "item", "list", "age", "message", "skin", "cape", "armor", "lookat", "owner", "follow"};
	
	public CommandHandler(NPCWarehouse instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (runCommand(sender, cmd, label, args) == false) {
			((Player)sender).sendMessage(ChatColor.RED + "Invalid command usage! Use '/npc help' for help");
		}		
		return true;
	}

	public boolean runCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player playerP = null;
		if (cmd.getName().equals("npc") || cmd.getName().equals("npcwh") || cmd.getName().equals("npcwarehouse")) {
			try {
				playerP = (Player)sender;
			} catch(Exception e) {
				plugin.log.info(plugin.INTRO + "You need to be a player to access that command!");
				return true;
			}
			if (args == null) {return false;}
			if (args.length < 1) {return false;}
			if (!isValidCmd(args[0])) {
				playerP.sendMessage(ChatColor.RED + "The argument " + ChatColor.YELLOW + args[0] + ChatColor.RED + " is invalid. Use '/npc help' for help!");
				return true;
			}
			if (!args[0].equalsIgnoreCase("help")) {
				if (!NPCWarehouse.playerHasPermission(playerP, "NPCWarehouse.command." + args[0])) {
					playerP.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
				}
			}
			
			
			if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("age") || args[0].equalsIgnoreCase("waypoint") || args[0].equalsIgnoreCase("message") || args[0].equalsIgnoreCase("skin") || args[0].equalsIgnoreCase("cape") || args[0].equalsIgnoreCase("armor") || args[0].equalsIgnoreCase("follow") || args[0].equalsIgnoreCase("lookat") || args[0].equalsIgnoreCase("owner")) {
				if (plugin.selected.get(playerP) == null || plugin.manager.getNPC(plugin.selected.get(playerP)) == null) {
					playerP.sendMessage(ChatColor.RED + "You need to select an npc first!");
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("waypoint") || args[0].equalsIgnoreCase("message") || args[0].equalsIgnoreCase("skin") || args[0].equalsIgnoreCase("cape") || args[0].equalsIgnoreCase("armor") || args[0].equalsIgnoreCase("follow") || args[0].equalsIgnoreCase("lookat") || args[0].equalsIgnoreCase("owner")) {
				if (!plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP)).isOwner(playerP)) {
					playerP.sendMessage(ChatColor.RED + "You need to be " + plugin.getPlayersSelectedNpc(playerP).getName() + "'s owner to do that!");
					return true;
				}
			}
			
			
			
			if (args[0].equalsIgnoreCase("help")) {
				String page = "0";
				if (args.length < 2) {page = "1";} else {page = args[1];}
				displayHelpPage(playerP, page);
			
			
			
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length < 2) {return false;}
				String message = "";
				if (args.length >= 3) {
					for (int i = 2; i < args.length; i++) {
						message += args[i] + " ";
					}
				} else {
					message = "Hello!";
				}
				int id = createNPC(args[1], playerP.getLocation(), message, false, playerP);
				playerP.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.DARK_GRAY + " was born! " + ChatColor.GREEN + "<ID: " + id + ">");
			
			} else if (args[0].equalsIgnoreCase("remove")) {
				removeNPC(plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP))));			
			} else if (args[0].equalsIgnoreCase("move")) {
				String npcId;
				final HumanNPC npcE;
				final Location l;
				npcId = plugin.selected.get(playerP);
				npcE = (HumanNPC)plugin.manager.getNPC(npcId);
				if (npcE == null) {
					playerP.sendMessage(ChatColor.RED + "You need to select an NPC first!");
					return true;
				}
				if (args.length == 1) {
					l = playerP.getLocation();
				} else if (args.length > 1) {
					Player p = plugin.getServer().getPlayer(args[1]);
					if (p == null) {
						playerP.sendMessage(ChatColor.RED + "That player is not online!");
						return true;
					} else {
						l = p.getLocation();
					}
				} else {
					return false;
				}
				for (int i = 0; i < plugin.getServer().getOnlinePlayers().length; i++) {
					plugin.getServer().getOnlinePlayers()[i].playEffect(npcE.getBukkitEntity().getLocation(), Effect.SMOKE, 10);
				}
				for (int i = 0; i < plugin.npcs.length; i++) {
					if (plugin.npcs[i] != null) {
						if (plugin.npcs[i].equals(plugin.getNpcInfo(npcE))) {
							plugin.npcs[i].setLocation(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()));
						}
					}
				}
				playEffect(Effect.SMOKE, playerP.getLocation(), 10);
				Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						npcE.getEntity().setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
					}
				}, 20);
				playerP.sendMessage(ChatColor.YELLOW + npcE.getName() + ChatColor.GREEN + " was moved to your location!");
			
			
			
			} else if (args[0].equalsIgnoreCase("rename")) {
				if (args.length < 2) {
					return false;
				}
				String id = plugin.selected.get(playerP);
				HumanNPC npc = (HumanNPC)plugin.manager.getNPC(id);
				String name = args[1];
				name = name.replace("/", " ");
				plugin.manager.rename(id, name);
				playerP.sendMessage(ChatColor.YELLOW + npc.getName() + "'s " + ChatColor.GREEN + "name was changed to " + ChatColor.YELLOW + args[1]);
				Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(plugin.getNpcInfo(npc), playerP, Property.NAME, name));
			
			
			} else if (args[0].equalsIgnoreCase("item")) {
				if (args.length < 2) {
					return false;
				}
				int id = 0;
				boolean num = false;
				try {
					id = Integer.parseInt(args[1]);
					num = true;
				} catch(Exception e) {}
				Material mat;
				if (!num) {
					mat = Material.getMaterial(args[1].toUpperCase());
				} else {
					mat = Material.getMaterial(id);
				}
				//ItemStack iIH = playerP.getItemInHand();
				HumanNPC npc = (HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP));
				if (mat == null) {
					playerP.sendMessage(ChatColor.RED + args[1] + " is not a recognized item id/name!");
					return true;
				}
				/*if (iIH.getTypeId() != mat.getId()) {
					playerP.sendMessage(ChatColor.RED + "You do not have any " + args[1] + " in your hand!");
					return true;
				}
				if (iIH.getAmount() <= 1) {
					iIH.setTypeId(0);
				} else {
					iIH.setAmount(iIH.getAmount() - 1);
				}
				playerP.setItemInHand(iIH);*/
				if (npc != null) {
					npc.setItemInHand(mat);
				} else {
					playerP.sendMessage(ChatColor.RED + "You need to select an npc first!");
				}
				playerP.sendMessage(ChatColor.YELLOW + npc.getName() + ChatColor.GREEN + " was successfully given a " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " to hold!");
			
			
			} else if (args[0].equalsIgnoreCase("armor")) {
				NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP)));
				ItemStack item = playerP.getItemInHand();
				//armor = 298 - 317
				if (item.getTypeId() < 298 || item.getTypeId() > 317) {
					playerP.sendMessage(ChatColor.RED + "That item is not armor");
					return true;
				}
				item.setAmount(1);
				if (item.getType().equals(Material.CHAINMAIL_HELMET) || item.getType().equals(Material.GOLD_HELMET) || item.getType().equals(Material.IRON_HELMET) || item.getType().equals(Material.DIAMOND_HELMET) || item.getType().equals(Material.LEATHER_HELMET)) {
					npc.npc.getInventory().setHelmet(item);
				} else if (item.getType().equals(Material.CHAINMAIL_CHESTPLATE) || item.getType().equals(Material.GOLD_CHESTPLATE) || item.getType().equals(Material.IRON_CHESTPLATE) || item.getType().equals(Material.DIAMOND_CHESTPLATE) || item.getType().equals(Material.LEATHER_CHESTPLATE)) {
					npc.npc.getInventory().setChestplate(item);
				} else if (item.getType().equals(Material.CHAINMAIL_LEGGINGS) || item.getType().equals(Material.GOLD_LEGGINGS) || item.getType().equals(Material.IRON_LEGGINGS) || item.getType().equals(Material.DIAMOND_LEGGINGS) || item.getType().equals(Material.LEATHER_LEGGINGS)) {
					npc.npc.getInventory().setLeggings(item);
				} else if (item.getType().equals(Material.CHAINMAIL_BOOTS) || item.getType().equals(Material.GOLD_BOOTS) || item.getType().equals(Material.IRON_BOOTS) || item.getType().equals(Material.DIAMOND_BOOTS) || item.getType().equals(Material.LEATHER_BOOTS)) {
					npc.npc.getInventory().setBoots(item);
				}
				playerP.sendMessage(ChatColor.GREEN + "You gave " + ChatColor.YELLOW + npc.npc.getName() + ChatColor.GREEN + " a " + ChatColor.YELLOW + item.getType().name() + ChatColor.GREEN + " to wear!");
			
			
			
			} else if (args[0].equalsIgnoreCase("select")) {
				if (args.length <= 1) {
					return false;
				}
				NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(args[1]));
				if (npc == null) {
					playerP.sendMessage(ChatColor.RED + "Invalid npc id");
					return true;
				} else {
					plugin.selected.put(playerP, args[1]);
				}
				playerP.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + npc.npc.getName() + " <ID: " + npc.getId() + ">");
				
				
			
			} else if (args[0].equalsIgnoreCase("list")) {
				playerP.sendMessage(ChatColor.GOLD + "===== NPC List =====");
				for (int i = 0; i < plugin.npcs.length; i++) {
					if (plugin.npcs[i] != null) {
						playerP.sendMessage(ChatColor.GREEN + plugin.npcs[i].npc.getName() + ChatColor.YELLOW + " <ID: " + plugin.npcs[i].getId() + ">");
					}
				}
			
			
			
			} else if (args[0].equalsIgnoreCase("age")) {
				playerP.sendMessage(plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP))).getAge());
			
			
			
			} else if (args[0].equalsIgnoreCase("waypoint")) {
				if (args.length < 2) {
					return false;
				}
				
				if (args[1].equalsIgnoreCase("add")) {
					NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP)));
					for (int i = 0; i < plugin.npcs.length; i++) {
						if (plugin.npcs[i] != null) {
							if (plugin.npcs[i].equals(npc)) {
								plugin.npcs[i].waypoint.addWaypoint(playerP.getLocation());
							}
						}
					}
					playerP.sendMessage(ChatColor.GREEN + "Waypoint added at your location!");
					
					
					
				} else if (args[1].equalsIgnoreCase("rem")) {
					NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP)));
					for (int i = 0; i < plugin.npcs.length; i++) {
						if (plugin.npcs[i] != null) {
							if (plugin.npcs[i].equals(npc)) {
								plugin.npcs[i].waypoint.removeWaypoint(playerP.getLocation());
							}
						}
					}
					playerP.sendMessage(ChatColor.GREEN + "Waypoint removed at your location!");
				} else {
					return false;
				}
				
				
				
			} else if (args[0].equalsIgnoreCase("message")) {
				if (args.length < 2) {
					return false;
				}
				NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(String.valueOf(plugin.selected.get(playerP))));
				String message = "";
				for (int i = 1; i < args.length; i++) {
					message += args[i] + " ";
				}
				npc.setMessage(message);
				playerP.sendMessage(ChatColor.YELLOW + npc.npc.getName() + "'s " + ChatColor.GREEN + " message was set to " + ChatColor.YELLOW + message);
				Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(npc, playerP, Property.MESSAGE, message));
			
			
			} else if (args[0].equalsIgnoreCase("skin")) {
				if (ConfigFile.skins == false) {
					playerP.sendMessage(ChatColor.RED + "This feature is currently disabled");
					return true;
				}
				if (NPCWarehouse.useSpout == false) {
					playerP.sendMessage(ChatColor.RED + "Spout must be enabled to use this feature");
					return true;
				}

				NPCData npc = plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP)));
				if (args.length == 1) {
					Spout.getServer().resetEntitySkin((LivingEntity) npc.npc.getBukkitEntity());
					playerP.sendMessage(ChatColor.YELLOW + npc.npc.getName() + "'s " + ChatColor.GREEN + "skin was changed to " + ChatColor.YELLOW + "the default skin");
					Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(npc, playerP, Property.SKIN, NPCData.DEFAULT_SKIN));
					return true;
				}
				
				npc.npc.getSpoutPlayer().setSkin(args[1]);
				playerP.sendMessage(ChatColor.YELLOW + npc.npc.getName() + "'s " + ChatColor.GREEN + "skin was changed to " + ChatColor.YELLOW + args[1]);
				Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(npc, playerP, Property.SKIN, args[1]));
			
			
			} else if (args[0].equalsIgnoreCase("cape")) {
				if (ConfigFile.capes == false) {
					playerP.sendMessage(ChatColor.RED + "This feature is currently disabled");
					return true;
				}
				if (NPCWarehouse.useSpout == false) {
					playerP.sendMessage(ChatColor.RED + "Spout needs to be enabled to use this feature");
					return true;
				}				
				HumanNPC npc = ((HumanNPC)plugin.manager.getNPC(plugin.selected.get(playerP)));
				if (args.length == 1) {
					npc.getSpoutPlayer().setCape(null);
					playerP.sendMessage(ChatColor.YELLOW + npc.getName() + ChatColor.GREEN + " took off his cape");
					Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(plugin.getNpcInfo(npc), playerP, Property.CAPE, null));
				} else if (args.length > 1) {
					npc.getSpoutPlayer().setCape(args[1]);
					playerP.sendMessage(ChatColor.YELLOW + npc.getName() + "'s " + ChatColor.GREEN + "cape has been set to " + ChatColor.YELLOW + args[1]);
					Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(plugin.getNpcInfo(npc), playerP, Property.CAPE, args[1]));
				}
			} else if (args[0].equalsIgnoreCase("owner")) {
				if (args.length < 2) {
					return false;
				}
				NPCData npc = plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP));
				npc.setOwner(args[1]);
				playerP.sendMessage(ChatColor.YELLOW + npc.npc.getName() + "'s" + ChatColor.GREEN + " owner was set to " + ChatColor.YELLOW + args[1]);
				Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(npc, playerP, Property.OWNER, args[1]));
			} else if (args[0].equalsIgnoreCase("lookat")) {
				NPCData npc = plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP));
				if (npc.getLookAt()) {
					npc.setLookat(false);
					playerP.sendMessage(ChatColor.YELLOW + npc.npc.getName() + ChatColor.GREEN + " will stop looking at players");
					Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(npc, playerP, Property.LOOKAT, false));
				} else {
					npc.setLookat(true);
					playerP.sendMessage(ChatColor.YELLOW + npc.npc.getName() + ChatColor.GREEN + " will now look at players");
					Bukkit.getPluginManager().callEvent(new NPCPropertyChangeEvent(npc, playerP, Property.LOOKAT, true));
				}
			} else if (args[0].equalsIgnoreCase("follow")) {
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("stop")) {
						plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP)).stopFollowingPlayer();
						playerP.sendMessage(ChatColor.YELLOW + plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP)).npc.getName() + ChatColor.GREEN + " will now stop following you");
						return true;
					}
				}
				plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP)).followPlayer(playerP);
				playerP.sendMessage(ChatColor.YELLOW + plugin.getNpcInfo(plugin.getPlayersSelectedNpc(playerP)).npc.getName() + ChatColor.GREEN + " is now following you");
			} else if (args[0].equalsIgnoreCase("reload")) {
				plugin.config.configCheck();
				plugin.saveAllData();
				plugin.loadAllData();
				playerP.sendMessage(ChatColor.GREEN + "NPCWarehouse config successfully reloaded");
				playerP.sendMessage(ChatColor.GREEN + "All NPC data successfully saved and reloaded");
			}
				
			return true;
		}
		return false;
	}
	
	private void displayHelpPage(Player player, String p) {
		int page;
		try {
			page = Integer.parseInt(p);
		} catch(Exception e) {
			player.sendMessage(ChatColor.RED + "The page number needs to be a number!");
			return;
		}
		ChatColor ge = ChatColor.GREEN, go = ChatColor.GOLD, ye = ChatColor.YELLOW, bl = ChatColor.BLUE;
		switch(page) {
			case 1: {
				player.sendMessage(go + "===== NPCWarehouse Help Page 1 =====");
				player.sendMessage(bl + "[] are required, <> are optional");
				player.sendMessage(ye + "/npc help <#> -- " + ge + "Displays the help page");
				player.sendMessage(ye + "/npc create [name] -- " + ge + "Creates an npc");
				player.sendMessage(ye + "/npc remove -- " + ge + "Removes the npc from the game");
				player.sendMessage(ye + "/npc rename [new name] -- " + ge + "Sets the npc's name");
				player.sendMessage(ye + "/npc select [id] -- " + ge + "Selects the npc with the specified id");
				player.sendMessage(ye + "/npc move <player> -- " + ge + "Moves the npc to your/players location");
				player.sendMessage(go + "====================================");
				break;
			}
			case 2: {
				player.sendMessage(go + "===== NPCWarehouse Help Page 2 =====");
				player.sendMessage(bl + "[] are required, <> are optional");
				player.sendMessage(ye + "/npc message [message] -- " + ge + "Sets the npc's message");
				player.sendMessage(ye + "/npc item [item name] -- " + ge + "Gives the npc an item from your inventory");
				player.sendMessage(ye + "/npc list -- " + ge + "Lists all loaded npcs");
				player.sendMessage(ye + "/npc age -- " + ge + "Tells you the npc's age");
				//player.sendMessage(ye + "/npc waypoint add|rem -- " + ge + "Adds/Rems a waypoint at your location");
				if (ConfigFile.skins)
					player.sendMessage(ye + "/npc skin <skin url> -- " + ge + "Sets the npc's skin to the specified url - leave url blank to reset");
				if (ConfigFile.capes)
					player.sendMessage(ye + "/npc cape <cape url> -- " + ge + "Sets the npc's cape to the specified url - leave url blank to reset");
				player.sendMessage(go + "====================================");
				break;
			}
			case 3: {
				player.sendMessage(go + "===== NPCWarehouse Help Page 3 =====");
				player.sendMessage(bl + "[] are required, <> are optional");
				player.sendMessage(ye + "/npc owner [player] -- " + ge + "Sets the npc's owner");
				player.sendMessage(ye + "/npc lookat -- " + ge + "Toggles if an NPC looks at a player");
				player.sendMessage(ye + "/npc follow <player> -- " + ge + "The NPC follows you, or the specified player");
				player.sendMessage(go + "====================================");
				break;
			}
			default: {
				player.sendMessage(ChatColor.RED + "Help page " + page + " does not exist!");
				break;
			}
		}
	}
	
	private void playEffect(Effect e, Location l, int num) {
		for (int i = 0; i < plugin.getServer().getOnlinePlayers().length; i++) {
			plugin.getServer().getOnlinePlayers()[i].playEffect(l, e, num);
		}
	}
	
	private boolean isValidCmd(String cmd) {
		for (int i = 0; i < validCmds.length; i++) {
			if (validCmds[i] != null) {
				if (validCmds[i].equalsIgnoreCase(cmd)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int createNPC(String name, Location loc, String message, boolean craft, Player playerP) {
		int num = 0;
		boolean done = false;
		String owner;
		if (playerP == null) {
			owner = "NPCWH_API_CREATED";
		} else {
			owner = playerP.getName();
		}
		for (int i = 0; i < plugin.npcs.length; i++) {
			if (plugin.npcs[i] == null && done == false) {
				plugin.npcs[i] = new NPCData((HumanNPC)plugin.manager.spawnHumanNPC(name, loc, String.valueOf(i), playerP), message, i, loc, owner);
				num = i;
				done = true;
			}
		}
		playEffect(Effect.SMOKE, plugin.npcs[num].npc.getBukkitEntity().getLocation(), 10);
		NPCCreationEvent event = new NPCCreationEvent(plugin.getNpcInfo((HumanNPC)plugin.manager.getNPC(String.valueOf(plugin.npcs[num].getId()))), playerP, craft);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return num;
	}
	
	public void removeNPC(NPCData npcD) {
		playEffect(Effect.SMOKE, npcD.npc.getBukkitEntity().getLocation(), 10);
		plugin.manager.despawnById(String.valueOf(npcD.getId()));
		for (int i = 0; i < plugin.npcs.length; i++) {
			if (plugin.npcs[i] != null) {
				if (plugin.npcs[i].npc.equals(npcD.npc)) {
					plugin.npcs[i] = null;
				}
			}
		}
		NPCDeletionEvent event = new NPCDeletionEvent(String.valueOf(npcD.getId()), null);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
}