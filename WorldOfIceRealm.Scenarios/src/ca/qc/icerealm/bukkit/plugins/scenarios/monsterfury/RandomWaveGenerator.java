package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.waves.EntityWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.waves.RegularSpawnWave;

public class RandomWaveGenerator {

	private MonsterFury _fury = null;
	private List<Location> _locations = null;
	private String[] _monstersMix;
	private boolean _increaseHealth = false;
	
	public RandomWaveGenerator(MonsterFury fury, List<Location> locations, String[] mix, boolean increaseHealth) {
		_fury = fury;
		_locations = locations;
		_monstersMix = mix;
		_increaseHealth = increaseHealth;
	}
	
	public List<EntityWave> createNewWaves() {
		List<EntityWave> waves = new ArrayList<EntityWave>();

		for (int i = 0; i < _fury.getTotalWaves(); i++) {
			
			double healthModifier = _fury.getHealthModifier();
			if (_increaseHealth) {
				healthModifier = (i+1) * healthModifier;
			}
			
			EntityWave wave = new RegularSpawnWave(100, _fury, _fury.getMaxMonsters(), healthModifier);
			wave.setMonsters(_monstersMix[RandomUtil.getRandomInt(_monstersMix.length)]);
			Collections.shuffle(_locations);
			wave.setSpawnLocation(_locations);
			waves.add(wave);
		}
		
		return waves;
	}
	
}
