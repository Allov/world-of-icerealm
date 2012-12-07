package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;

public class ArtilleryShot implements Runnable {
	
	private Location _location;
	
	public ArtilleryShot(Location location) {
		_location = location;
	}
	
	@Override
	public void run() {
		_location.getWorld().createExplosion(_location, 0.85f, RandomUtils.nextBoolean());
	}
}
