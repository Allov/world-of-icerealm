package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;


public class WaveTimer implements TimeObserver {

	private ScenarioEventsListener _listener;
	private EntityWave _wave;
	private Scenario _scenario;
	private long _alarm;
	
	public WaveTimer(EntityWave wave, ScenarioEventsListener l, Scenario s) {
		_wave = wave;
		_listener = l;
		_scenario = s;
	}
	
	@Override
	public void timeHasCome(long time) {
		if (_scenario.isActive()) {
			_wave.spawnWave();
			if (_listener != null) {
				_listener.waveStarting(_wave.getMaxNbOfEntities(), _wave.getMonsters());
			}
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
