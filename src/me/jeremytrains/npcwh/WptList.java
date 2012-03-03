package me.jeremytrains.npcwh;

public class WptList extends Thread {
	NPCWarehouse plugin;
	
	public WptList(NPCWarehouse instance) {
		plugin = instance;
	}
	
	public void run() {
		while(true) {
			for (int i = 0; i < plugin.npcs.length; i++) {
				if (plugin.npcs[i] != null) {
					plugin.npcs[i].moveToNextWaypoint();
				}
			}
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
