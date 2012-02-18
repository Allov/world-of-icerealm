package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;


public class WaveTimer implements TimeObserver {

	private MonsterFuryEventsListener _listener;
	private EntityWave _wave;
	private long _alarm;
	
	public WaveTimer(EntityWave wave, MonsterFuryEventsListener l) {
		_wave = wave;
		_listener = l;
	}
	
	@Override
	public void timeHasCome(long time) {
		_wave.spawnWave();
		if (_listener != null) {
			_listener.waveStarting(_wave.getNbOfEntities());
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
