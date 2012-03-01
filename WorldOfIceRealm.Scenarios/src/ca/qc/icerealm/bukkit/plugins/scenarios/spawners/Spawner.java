package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import org.bukkit.Location;

public interface Spawner {
	void removeListener();
	Location getLocation();
}
