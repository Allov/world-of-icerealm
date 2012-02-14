package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import org.bukkit.entity.Player;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;


public class WaveTimer implements TimeObserver {

	private MonsterFuryEventsListener _listener;
	private MonsterWave _wave;
	private long _alarm;
	
	public WaveTimer(MonsterWave wave, MonsterFuryEventsListener l) {
		_wave = wave;
		_listener = l;
	}
	
	@Override
	public void timeHasCome(long time) {
		
		
		_wave.spawnWave();
		if (_listener != null) {
			_listener.waveStarting(_wave.getMonstersSize());
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
