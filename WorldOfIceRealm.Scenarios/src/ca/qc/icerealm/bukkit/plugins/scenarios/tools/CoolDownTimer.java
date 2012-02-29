package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class CoolDownTimer implements TimeObserver {

	private long _alarm;
	private CoolDown _down;
	
	public CoolDownTimer(CoolDown f) {
		_down = f;
	}
	
	@Override
	public void timeHasCome(long time) {
		_down.setCoolDownActive(false);
	}

	@Override
	public void setAlaram(long time) {
		// TODO Auto-generated method stub
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}
	

}
