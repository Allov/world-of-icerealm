package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class BloodMoonStopper implements TimeObserver {

	private long _alarm;
	private BloodMoon _moon;
	
	public BloodMoonStopper(BloodMoon moon) {
		_moon = moon;
	}
	
	@Override
	public void timeHasCome(long time) {
		_moon.stopBloodMoon();		
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}

}
