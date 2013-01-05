package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Sound;

public class ThunderAmbiance implements Runnable {

	private ScheduledExecutorService _executor;
	private int _intensity = 1;
	private int _strike = 0;
	private Location _location;
	private ThunderSound _thunder;
	
	public ThunderAmbiance(ScheduledExecutorService exe, int intensity, Location loc) {
		_executor = exe;
		_intensity = intensity;
		_location = loc;
	}
	
	@Override
	public void run() {
		_strike++;
		
		if (RandomUtils.nextBoolean()) {
			_location.getWorld().playSound(_location, Sound.AMBIENCE_THUNDER, 10, 1);
		}
		
		if (_strike < _intensity) {
			_executor.schedule(this, 500, TimeUnit.MILLISECONDS);
		}		
	}

	
	
}
