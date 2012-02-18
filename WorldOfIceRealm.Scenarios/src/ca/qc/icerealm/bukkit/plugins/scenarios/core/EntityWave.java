package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public interface EntityWave {
	void spawnWave();
	void processEntityDeath(Entity e);
	void processDamage(EntityDamageEvent e);
	void cancelWave();
	int getNbOfEntities();
	void setSpawnLocation(List<Location> l);
	void setMonsters(String monsters);
	String[] getMonsters();
	int getMaxNbOfEntities();
}
