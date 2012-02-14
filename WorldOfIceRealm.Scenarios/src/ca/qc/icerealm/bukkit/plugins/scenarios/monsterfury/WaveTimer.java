package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import org.bukkit.entity.Player;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;


public class WaveTimer implements TimeObserver {

	private MonsterWave _wave;
	private long _alarm;
	
	public WaveTimer(MonsterWave wave) {
		_wave = wave;
	}
	
	@Override
	public void timeHasCome(long time) {
		_wave.broadcastToPlayers("Another wave is here!");
		_wave.spawnWave();
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
