package me.jeremytrains.npcwh;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings("unused")
public class ConfigFile {
	NPCWarehouse plugin;
	public String directory = "plugins" + File.separator + "NPCWarehouse";
    File file = new File(directory + File.separator + "config.yml");
    
    //CONFIG FILE SETTINGS
	public static boolean useSpout=false, usePermissions=true, useVault=false, rightClickSelect=true, capes=false, skins=false, crafting=true;
	public static String chatFormat = null;
	//=-=-=-=-=-=-=-=-=-=-	
	
	
    public ConfigFile(NPCWarehouse instance) {
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
    
	private List<?> readStringList(String root){
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
        plugin.log.info(plugin.INTRO + "Generating Config File...");
        write("General.use-permissions", true);
        write("General.use-vault", false);
        write("General.enable-right-click-selecting", true);
        write("General.enable-npc-crafting", true);
        write("Messages.chat-format", "<{name}> {msg}");
        write("Spout.use-spout", false);
        write("Spout.allow-skin-changing", false);
        write("Spout.allow-cape-changing", false);
        loadkeys();
        plugin.log.info(plugin.INTRO + "Config File Generated Successfully!");
    }
    
    private void loadkeys() {
        plugin.log.info(plugin.INTRO + "Loading Config File...");
        usePermissions = readBoolean("General.use-permissions");
        useVault = readBoolean("General.use-vault");
        rightClickSelect = readBoolean("General.right-click-selecting");
        crafting = readBoolean("General.enable-npc-crafting");
        chatFormat = readString("Messages.chat-format");
        useSpout = readBoolean("Spout.use-spout");
        capes = readBoolean("Spout.allow-cape-changing");
        skins = readBoolean("Spout.allow-skin-changing");
        plugin.log.info(plugin.INTRO + "Config File Loaded Successfully!");
    }
}
