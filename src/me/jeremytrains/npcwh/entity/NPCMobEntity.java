package me.jeremytrains.npcwh.entity;

import me.jeremytrains.npcwh.ConfigFile;
import net.minecraft.server.World;

public class NPCMobEntity extends net.minecraft.server.EntityCreature {

	public NPCMobEntity(World world) {
		super(world);
	}

	@Override
	public int getMaxHealth() {
		return ConfigFile.mobMaxHealth;
	}

}
