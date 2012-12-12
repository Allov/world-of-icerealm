package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class ArtilleryShelling implements Runnable {
	private WorldZone _shellingZone;
	private int _limit;
	private int _shots;
	
	public ArtilleryShelling(WorldZone zone, int shot) {
		_shellingZone = zone;
		_shots = shot;
	}
	
	@Override
	public void run() {
		_limit++;
		if (RandomUtils.nextBoolean()) {
			Location location = _shellingZone.getRandomHighestLocation(_shellingZone.getWorld());
			Location field = new Location(location.getWorld(), location.getX() + 30, location.getY(), location.getZ() + 30);
			_shellingZone.getWorld().playSound(field, Sound.GHAST_FIREBALL, 1000, 5);
			Executors.newSingleThreadScheduledExecutor().schedule(new ArtilleryShot(location), 1000, TimeUnit.MILLISECONDS);
		}
		
		if (_limit < _shots) {
			Executors.newSingleThreadScheduledExecutor().schedule(this, 500, TimeUnit.MILLISECONDS);
		}
	}
}
