package me.jeremytrains.npcwh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class WaypointFile {
	private static final File file = new File("plugins/NPCWarehouse/waypoints.bin");
	
	public static void saveToFile() {
		if (file.exists() == false) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(NPCWarehouse.npcwypts);
			oos.flush();
			oos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void loadFromFile() throws Exception {
		if (file.exists() == false) {
			NPCWarehouse.npcwypts = new NPCWaypoint[1000];
			return;
		}
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Object result = ois.readObject();
		ois.close();
		NPCWarehouse.npcwypts = (NPCWaypoint[])result;
	}
	
	public static void removeWaypointFromList(NPCWaypoint w) {
		for (int i = 0; i < NPCWarehouse.npcwypts.length; i++) {
			if (NPCWarehouse.npcwypts[i] != null) {
				if (NPCWarehouse.npcwypts[i].equals(w)) {
					NPCWarehouse.npcwypts[i] = null;
				}
			}
		}
	}
}
