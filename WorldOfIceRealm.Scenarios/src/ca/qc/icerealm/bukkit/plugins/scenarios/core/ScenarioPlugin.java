package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.DefaultEventListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.RegularSpawnWave;

public class ScenarioPlugin extends JavaPlugin {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private MonsterFury _hauntedOutpost = null;

	@Override
	public void onDisable() {
		releaseHauntedOutpost();		
	}

	@Override
	public void onEnable() {
		getCommand("sc").setExecutor(new ScenarioCommander());
		createHauntedOutpost();
	}
	
	private void createHauntedOutpost() {
		if (_hauntedOutpost == null) {
			
			MonsterFuryConfiguration _defaultConfig = new MonsterFuryConfiguration();
			_defaultConfig.ExperienceReward = 5;										// 100 level d'exp
			_defaultConfig.CoolDownTime = 600000; 										// 10 min
			_defaultConfig.InitialTimeBeforeFirstWave = 10000;							// 10 sec
			_defaultConfig.TimeoutWhenLeaving = 30000;									// 30 sec
			_defaultConfig.MinimumPlayer = 1;											// 1 joueur requis
			_defaultConfig.MonstersPerWave = 25;							    		// 10 monstres par wave
			_defaultConfig.Name = "haunted_outpost";									// le nom du scénario 
			_defaultConfig.NumberOfWaves = 5;											// 3 waves
			_defaultConfig.TimeBetweenWave = 10000;										// 10 sec
			_defaultConfig.ActivationZoneCoords = "366,181,369,184,0,128";				// zone d'Activation
			_defaultConfig.ScenarioZoneCoords = "347,168,386,201,0,128";				// zone du scenario
			
			_hauntedOutpost = new MonsterFury(this, _defaultConfig, new DefaultEventListener(), null);
			
			// creation de plusieurs wave
			List<EntityWave> waves = new ArrayList<EntityWave>();
			WorldZone scenarioZone = new WorldZone(getServer().getWorld("world"), _defaultConfig.ScenarioZoneCoords);
			List<Location> locations = scenarioZone.getFourSide(2);
			
			for (int i = 0; i < _defaultConfig.NumberOfWaves; i++) {
				EntityWave wave = new RegularSpawnWave(1000, _hauntedOutpost, _defaultConfig.MonstersPerWave);
				wave.setMonsters("skeleton,zombie,spider");
				wave.setSpawnLocation(locations);
				waves.add(wave);
			}
			
			_hauntedOutpost.setEntityWaves(waves);
			ScenarioService.getInstance().addScenario(_hauntedOutpost);	
		}
	}
	
	private void releaseHauntedOutpost() {
		if (_hauntedOutpost != null) {
			_hauntedOutpost.removeAllListener();
			_hauntedOutpost = null;
		}
	}
}