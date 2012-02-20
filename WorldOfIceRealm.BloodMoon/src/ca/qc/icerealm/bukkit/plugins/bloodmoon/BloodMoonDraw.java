package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class BloodMoonDraw implements TimeObserver {

	private long _alarm;
	private BloodMoon _moon;
	
	public BloodMoonDraw(BloodMoon moon)  {
		_moon = moon;
	}
	
	@Override
	public void timeHasCome(long time) {
		if (_moon.drawBloodMoon()) {
			_moon.startBloodMoon();
		}
		else {
			TimeServer.getInstance().addListener(this, 24000 * 50);
		}
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
