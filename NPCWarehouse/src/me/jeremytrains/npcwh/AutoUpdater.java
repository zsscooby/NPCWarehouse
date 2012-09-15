package me.jeremytrains.npcwh;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoUpdater {
	private Logger log = Logger.getLogger("Minecraft");
	private NPCWarehouse plugin;
	private String url;
	
	
	public AutoUpdater(String url, NPCWarehouse inst) {
		plugin = inst;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void download(String path) {
		File f = new File(path);
		if (f.exists() == false) {
			throw new IllegalArgumentException("The file has to be created. Path given: " + path);
		}
		BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
                in = new BufferedInputStream(new URL(url).openStream());
                fout = new FileOutputStream(f.getPath());

                byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1)  {
                	fout.write(data, 0, count);
                }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (java.net.UnknownHostException e) {
			log.log(Level.SEVERE, plugin.INTRO + "Unable to download update from the internet. Please download from http://dev.bukkit.org/server-mods/npcwarehouse.");
			return;
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
	            if (in != null)
	            	in.close();
	            if (fout != null)
	            	fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info(plugin.INTRO + "The new version of NPCWarehouse was saved at " + path);
        }
	}
}
