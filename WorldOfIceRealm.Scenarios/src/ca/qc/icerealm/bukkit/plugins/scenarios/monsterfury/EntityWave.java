package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public interface EntityWave {
	void spawnWave();
	void processEntityDeath(Entity e);
	void processDamage(EntityDamageEvent e);
	void cancelWave();
	int getNbOfEntities();
}
