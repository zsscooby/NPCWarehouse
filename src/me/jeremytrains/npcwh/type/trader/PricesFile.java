package me.jeremytrains.npcwh.type.trader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import me.jeremytrains.npcwh.NPCWarehouse;

@SuppressWarnings("unused")
public class PricesFile {
	
	NPCWarehouse plugin;
	public String directory = "plugins" + File.separator + "NPCWarehouse" + File.separator + "trader";
	public File file = new File(directory + File.separator + "prices.yml");
	   
	//TRADER PRICES FILE SETTINGS
	public static ArrayList<Stockable> stockables = new ArrayList<Stockable>();
	public static ArrayList<Integer> invalidIds;
	//=-=-=-=-=-=-=-=-=-=-	
	
	
	   public PricesFile(NPCWarehouse instance) {
	       plugin = instance;
	   }

    public void configCheck(){   
    	new File(directory).mkdir();   
        if(!file.exists()){
            try {
                file.createNewFile();
                addDefaults();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            loadkeys();
        }
    }
	private void write(String root, Object x){
        YamlConfiguration config = load();
        config.set(root, x);
        try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	private Boolean readBoolean(String root){
        YamlConfiguration config = load();
        return config.getBoolean(root, true);
    }
	    private Double readDouble(String root){
        YamlConfiguration config = load();
        return config.getDouble(root, 0);
    }
    
    private int readInt(String root) {
    	YamlConfiguration config = load();
    	return config.getInt(root);
    }
    
	private List<?> readList(String root){
        YamlConfiguration config = load();
        return config.getList(root);
    }
    private String readString(String root){
        YamlConfiguration config = load();
        return config.getString(root);
    }
    private YamlConfiguration load(){
        try {
            org.bukkit.configuration.file.YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            return config;
	        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void addDefaults(){
        plugin.log.info(plugin.INTRO + "Generating Trader Prices File...");
        for (int i = 0; i < Material.values().length; i++) {
        	write("Prices." + Material.values()[i].name().replace(" ", "_"), 5.0);
        }
        plugin.log.info(plugin.INTRO + "Trader Prices File Generated Successfully!");
        loadkeys();
    }
    
    private void loadkeys() {
        plugin.log.info(plugin.INTRO + "Loading Trader Prices File...");
        Set<String> keys = load().getConfigurationSection("Prices").getKeys(false);
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
        	int id = 43344;
        	String key = it.next();
        	try {
        		id = Integer.parseInt(key);
        	} catch (Exception e) {}
        	if (id == 43344)
        		stockables.add(new Stockable(Material.getMaterial(key), readDouble("Prices." + key)));
        	else
        		stockables.add(new Stockable(Material.getMaterial(id), readDouble("Prices." + key)));
        }
        invalidIds.add(0);
        @SuppressWarnings("unchecked")
		List<String> idsX = (List<String>)readList("Invalid-IDs");
        for (int i = 0; i < idsX.size(); i++) {
        	int id = 0;
        	try {
        		id = Integer.parseInt(idsX.get(i));
        	} catch (Exception e) {
        		Logger.getLogger("Minecraft").log(Level.SEVERE, plugin.INTRO + "You have an error in your prices.yml file! Invalid-ID > " + idsX.get(i) + " is invalid!");
        	} finally {
        		if (invalidIds.contains(id) == false)
        			invalidIds.add(id);
        	}
        }
        plugin.log.info(plugin.INTRO + "Trader Prices File Loaded Successfully!");
    }
}