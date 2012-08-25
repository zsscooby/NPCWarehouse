package me.jeremytrains.npcwh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import com.topcat.npclib.*;
import com.topcat.npclib.entity.HumanNPC;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class NPCWarehouse extends JavaPlugin {
	public Logger log = Logger.getLogger("Minecraft");
	public final String INTRO = "[NPCWarehouse] ";
	private boolean inSetupMode = false;
	public CommandHandler commandHandler;
	public NPCData[] npcs = new NPCData[1000];
	public NPCManager manager;
	public Map<Player, String> selected = new HashMap<Player, String>();
	public int nullCode = 43343;
	private NpcDataFile npcData;
	public static PermissionHandler permissionHandler;
	private static boolean usePermPlugin = false;
	public static boolean useSpout = false, useFactions = false;
	public static NPCWaypoint[] npcwypts = new NPCWaypoint[1000];
	private ConfigFile config;

	@Override
	public void onDisable() {
		log.info(INTRO + "Saving npc data...");
		saveAllData();
		log.info(INTRO + "Npc data saved successfully!");
		log.info(INTRO + "NPCWarehouse v" + this.getDescription().getVersion() + " by jeremytrains is now disabled");
	}

	@Override
	public void onEnable() {
		log.info("===== NPCWarehouse v" + this.getDescription().getVersion() + " by jeremytrains =====");
		log.info(INTRO + "Searching for permissions plugin...");
		if (inSetupMode) {
			log.info(INTRO + "Plugin is in Development Mode! Some features may not work!");
		}
		config = new ConfigFile(this);
		config.configCheck();
		setupLinkedPlugins();
		commandHandler = new CommandHandler(this);
		log.info(INTRO + "Setting up command handler...");
		getCommand("npc").setExecutor(commandHandler);
		getCommand("npcwh").setExecutor(commandHandler);
		getCommand("npcwarehouse").setExecutor(commandHandler);
		log.info(INTRO + "Command handler setup successfully!");
		log.info(INTRO + "Setting up npc manager...");
		try {
			manager = new NPCManager(this);
		} catch (Exception e) {
			log.log(Level.SEVERE, INTRO + "There was an error in instantiating the NPCManager. Please report this error to jeremytrains ASAP!");
			log.log(Level.SEVERE, INTRO + "===== Error Report Start =====");
			log.log(Level.SEVERE, INTRO + "|-|-|Error Code #001|-|-|");
			e.printStackTrace();
			log.log(Level.SEVERE, INTRO + "==============================");
		}	
		log.info(INTRO + "Npc manager setup successfully!");
		log.info(INTRO + "Loading npc data...");
		loadAllData();
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				log.info(INTRO + npcData.totNpcs + " NPC(s) have been loaded!");		
			}
		}, 60);
		log.info(INTRO + "Npc data loaded successfully!");
		log.info(INTRO + "Setting up listeners...");
		this.getServer().getPluginManager().registerEvents(new PlList(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockList(this), this);
		log.info(INTRO + "Listeners setup successfully!");
		try {
			if (this.getLatestVersion()) {
				new File("plugins/update").mkdir();
				if (new File("plugins/update/NPCWarehouse.jar").exists()) {
					new File("plugins/update/NPCWarehouse.jar").delete();
				}
				new File("plugins/update/NPCWarehouse.jar").createNewFile();
				new AutoUpdater("https://bitbucket.org/jeremytrains/npcwarehouse/downloads/NPCWarehouse.jar", this).download("plugins/update/NPCWarehouse.jar");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//Quoting this line out completely disables the waypoint system -- just remove it from the cmd handler
		//new WptList(this).start();
		log.info("===== NPCWarehouse v" + this.getDescription().getVersion() + " by jeremytrains is now enabled! =====");
	}
	
	public static boolean playerHasPermission(Player player, String node) {
		if (usePermPlugin) {
			if (permissionHandler.has(player, node)) {
			      return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	private void saveAllData() {
		npcData.saveNpcData();
	}
	private void loadAllData() {
		npcData = new NpcDataFile(this);
		npcs = npcData.loadNpcData();
	}
	
	public NPCData getNpcInfo(HumanNPC n) {
		if (n == null) {
			return null;
		}
		for (int i = 0; i < npcs.length; i++) {
			if (npcs[i] != null) {
				if (npcs[i].npc.getBukkitEntity().getEntityId() == n.getBukkitEntity().getEntityId()) {
					return npcs[i];
				}
			}
		}
		return null;
	}
	
	private boolean getLatestVersion() throws IOException {
		log.info(INTRO + "Checking for latest version...");
		URL url = null;
		try {
			url = new URL("http://dl.dropbox.com/u/31442127/Plugins/NPCWarehouse/version.txt");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}
		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		String version =  sb.toString();
		if (version.equals(this.getDescription().getVersion())) {
			log.info(INTRO + "You have the latest version of NPCWarehouse!");
			return false;
		}
		log.warning(INTRO + "A new version of NPCWarehouse is now available! <" + version + ">");
		log.warning(INTRO + "Downloading NPCWarehouse version " + version + "...");
		return true;
	}
	
	//=== LINKED PLUGINS START ===
	
	private void setupLinkedPlugins() {
		if (ConfigFile.usePermissions) {
			setupPermissions();
		}
		
		if (ConfigFile.useSpout) {
			setupSpout();
		}
		setupFactions();
	}
	
	private void setupPermissions() {
	    if (permissionHandler != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        log.info(INTRO + "Permission system not detected, defaulting to OP");
	        usePermPlugin = false;
	        return;
	    }
	    usePermPlugin = true;
	    permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	    log.info(INTRO + "Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName() + " for permissions!");
	}
	
	private void setupSpout() {
		Plugin spoutPlugin = this.getServer().getPluginManager().getPlugin("Spout");
	    
	    if (spoutPlugin == null) {
	        log.info(INTRO + "Spout not detected! Spout features will be disabled.");
	        useSpout = false;
	        return;
	    }
	    
	    log.info(INTRO + "Spout detected! Using " + spoutPlugin.getDescription().getFullName() + "for spout support!");
	    useSpout = true;
	}
	
	private void setupFactions() {
		Plugin factionsPlugin = this.getServer().getPluginManager().getPlugin("Factions");
	    
	    if (factionsPlugin == null) {
	        log.info(INTRO + "Factions not detected! Factions features will be disabled.");
	        useFactions = false;
	        return;
	    }
	    
	    log.info(INTRO + "Factions detected! Using " + factionsPlugin.getDescription().getFullName() + "for factions support!");
	    useFactions = true;
	}
	
	//=== LINKED PLUGINS END ===
	
	public HumanNPC getPlayersSelectedNpc(Player p) {
		return (HumanNPC)manager.getNPC(selected.get(p));
	}
}
