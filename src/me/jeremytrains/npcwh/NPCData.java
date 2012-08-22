package me.jeremytrains.npcwh;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.topcat.npclib.entity.HumanNPC;

public class NPCData {
	public HumanNPC npc;
	private String message = "Hello!";
	private int ticksLived, id, nextWp;
	private int ticksPerSecond = 20;
	private boolean lookAt = true;
	Location loc;
	public NPCWaypoint waypoint;
	public final static String DEFAULT_SKIN = "http://www.minecraft.net/images/char.png";
	
	public NPCData(HumanNPC npc, String message, int id, Location l) {
		this.npc = npc;
		this.message = message;
		this.id = id;
		loc = l;
		waypoint = new NPCWaypoint(this);
	}
	
	public boolean getLookAt() {
		return lookAt;
	}
	
	public String getAge() {
		int ticks = getTotTicksLived();
		int days = ((((ticks / ticksPerSecond) / 60) / 60) / 24);
		return this.npc.getName() + " has been alive for " + days + " days";
	}
	
	public int getTotTicksLived() {
		return ticksLived + npc.getBukkitEntity().getTicksLived();
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getId() {
		return id;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public void setLocation(Location l) {
		loc = l;
	}
	
	public void setMessage(String me) {
		message = me;
	}
	
	public void setLookat(boolean l) {
		lookAt = l;
	}
	
	public void moveToNextWaypoint() {
		Logger.getLogger("Minecraft").info("started");
		if (waypoint.numWpts < 2) {
			return;
		}
		Logger.getLogger("Minecraft").info("Ran for " + this.npc.getName());
		npc.walkTo(waypoint.getWaypoint(nextWp));
		Logger.getLogger("Minecraft").info("completed");
		nextWp++;
	}
	
	public void chat(Player p) {		
		if (message == null) {
			message = "Hello!";
		}
		
		p.sendMessage("[NPC] <" + npc.getName() + "> " + message);
	}
}
