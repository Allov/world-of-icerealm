package ca.qc.icerealm.bukkit.plugins.scenarios.waves;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class CoolDownTimer implements TimeObserver {

	private long _alarm;
	private MonsterFury _fury;
	
	public CoolDownTimer(MonsterFury fury) {
		_fury = fury;
	}
	
	@Override
	public void timeHasCome(long time) {
		// TODO Auto-generated method stub
		_fury.setCoolDownActive(false);
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
