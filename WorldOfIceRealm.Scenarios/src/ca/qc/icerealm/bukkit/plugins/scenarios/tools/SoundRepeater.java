package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Sound;

public class SoundRepeater implements Runnable {

	private ScheduledExecutorService _executor;
	private int _intensity = 1;
	private int _strike = 0;
	private Location _location;
	private Sound _sound;
	private int _volume;
	private int _pitch;
	
	public SoundRepeater(ScheduledExecutorService exe, int intensity, Location loc, Sound s, int volume, int pitch) {
		_executor = exe;
		_intensity = intensity;
		_location = loc;
		_volume = volume;
		_pitch = pitch;
		_sound = s;
	}
	
	@Override
	public void run() {
		_strike++;
		
		if (RandomUtils.nextBoolean()) {
			_location.getWorld().playSound(_location, _sound, _volume, _pitch);
		}
		
		if (_strike < _intensity) {
			_executor.schedule(this, 500, TimeUnit.MILLISECONDS);
		}		
	}

	
	
}
