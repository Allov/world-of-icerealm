package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.SimpleTimer;

public class WaveTimer extends SimpleTimer {

	private MonsterWave _wave;
	private MonsterFury _fury;
	
	public WaveTimer(MonsterWave wave, MonsterFury f) {
		_wave = wave;
		_fury = f;
	}
	
	@Override
	public void timeHasCome(long time) {
		_fury.sendMessageToPlayers("Another wave is here!");
		_wave.spawnWave();
	}


}
