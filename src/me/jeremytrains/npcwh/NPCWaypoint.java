package me.jeremytrains.npcwh;

import org.bukkit.Location;

public class NPCWaypoint implements java.io.Serializable {
	private static final long serialVersionUID = -6732951076475487230L;
	private Location[] waypoints = new Location[1000];
	public int numWpts = 0;
	public NPCData npc;
	
	public NPCWaypoint(NPCData npc) {
		this.npc = npc;
	}
	
	public void addWaypoint(Location l) {
		for (int i = 0; i < waypoints.length; i++) {
			if (waypoints[i] == null) {
				waypoints[i] = l;
				numWpts++;
				return;
			}
		}
	}
	
	public void removeWaypoint(Location l) {
		if (numWpts <= 0) {
			return;
		}
		
		for (int i = 0; i < waypoints.length; i++) {
			if (waypoints[i] != null) {
				if (waypoints[i].equals(l)) {
					waypoints[i] = null;
					numWpts--;
				}
			}
		}
	}
	
	public Location getWaypoint(int num) {
		if (waypoints[num] == null) {
			return waypoints[0];
		}
		return waypoints[num];
	}
}