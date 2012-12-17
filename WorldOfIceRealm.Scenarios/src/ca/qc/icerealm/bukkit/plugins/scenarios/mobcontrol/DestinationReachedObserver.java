package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;


public interface DestinationReachedObserver {
	
	public void destinationReached(LivingEntity e, Location l);
	
}
