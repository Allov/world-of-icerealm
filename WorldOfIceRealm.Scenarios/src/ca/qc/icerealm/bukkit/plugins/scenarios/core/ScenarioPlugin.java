package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.barbarian.BarbarianRaid;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.Infestation;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.DefaultEventListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.RegularSpawnWave;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ScenarioPlugin extends JavaPlugin {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private MonsterFury _hauntedOutpost = null;
	private MonsterFury _defaultWaves = null;

	@Override
	public void onDisable() {
		releaseHauntedOutpost();		
	}

	@Override
	public void onEnable() {
		getCommand("sc").setExecutor(new ScenarioCommander());
		ScenarioService.getInstance().setPlugin(this);
		
		Infestation inf = new Infestation(this.getServer(), "-46,-16,-29,12,0,128", 5, new String[] {"zombie", "skeleton", "creeper"}, this);
		ZoneServer.getInstance().addListener(inf);
		getServer().getPluginManager().registerEvents(inf, this);
		
		//createBarbarianRaid();
		//createHauntedOutpost();
		//createDefaultWaves();
	}
	
	private void createBarbarianRaid() {
		
		BarbarianRaid raid = new BarbarianRaid(this.getServer(), "-102,56,-96,62,67,72", "-103,48,-99,52,66,69");
		
	}
	
	private void createDefaultWaves() {
		if (_defaultWaves == null) {
			MonsterFuryConfiguration config  = new MonsterFuryConfiguration();
			config.ExperienceReward = 5;										// 100 level d'exp
			config.CoolDownTime = 10000; 										// 10 min
			config.InitialTimeBeforeFirstWave = 500;							// 10 sec
			config.TimeoutWhenLeaving = 2000;									// 30 sec
			config.MinimumPlayer = 1;											// 1 joueur requis
			config.MonstersPerWave = 5;							    		 	// 10 monstres par wave
			config.Name = "default";											// le nom du scénario 
			config.NumberOfWaves = 3;											// 3 waves
			config.TimeBetweenWave = 5000;										// 10 sec
			config.ActivationZoneCoords = "-181,133,-177,137,0,128";			// zone d'Activation
			config.ScenarioZoneCoords = "-189,127,-168,140,0,128";				// zone du scenario
			
			_defaultWaves = new MonsterFury(this, config, new DefaultEventListener());
			
			EntityWave w = new RegularSpawnWave(1500, _defaultWaves, 20);
			w.setMonsters("spider,slime");
			w.setSpawnLocation(_defaultWaves.getScenarioZone().getRandomLocation(_defaultWaves.getWorld(), 20));
			
			List<EntityWave> waves = _defaultWaves.getEntityWaves();
			waves.add(w);
			_defaultWaves.setEntityWaves(_defaultWaves.getEntityWaves());
			
			ScenarioService.getInstance().addScenario(_defaultWaves);
		}
	}
	
	private void createHauntedOutpost() {
		if (_hauntedOutpost == null) {
			
			MonsterFuryConfiguration _defaultConfig = new MonsterFuryConfiguration();
			_defaultConfig.ExperienceReward = 5;										// 100 level d'exp
			_defaultConfig.CoolDownTime = 10000; 										// 10 min
			_defaultConfig.InitialTimeBeforeFirstWave = 10000;							// 10 sec
			_defaultConfig.TimeoutWhenLeaving = 30000;									// 30 sec
			_defaultConfig.MinimumPlayer = 1;											// 1 joueur requis
			_defaultConfig.MonstersPerWave = 35;							    		 // 10 monstres par wave
			_defaultConfig.Name = "haunted_outpost";									// le nom du scénario 
			_defaultConfig.NumberOfWaves = 5;											// 3 waves
			_defaultConfig.TimeBetweenWave = 10000;										// 10 sec
			_defaultConfig.ActivationZoneCoords = "-148,155,-144,159,0,128";				// zone d'Activation
			_defaultConfig.ScenarioZoneCoords = "-159,148,-133,165,0,128";				// zone du scenario
			
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