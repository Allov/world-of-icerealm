package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.barbarian.BarbarianRaid;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.Infestation;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.DefaultEventListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.RegularSpawnWave;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ScenarioPlugin extends JavaPlugin {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private MonsterFury _hauntedOutpost = null;
	private Infestation _ruinsPlateform = null;

	@Override
	public void onDisable() {
		releaseHauntedOutpost();	
		releaseRuinsPlateform();
	}

	@Override
	public void onEnable() {
		getCommand("sc").setExecutor(new ScenarioCommander());
		ScenarioService.getInstance().setPlugin(this);
		
		createHauntedOutpost();
		createRuinsPlateform();
	}

	private void createRuinsPlateform() {
		// configuration de la plate forme
		if (_ruinsPlateform == null) {
			InfestationConfiguration config = new InfestationConfiguration();
			config.InfestedZone = "26,-74,116,24,98,127";
			config.BurnDuringDaylight = false;
			config.RegenerateExplodedBlocks = true;
			config.UseLowestBlock = true;
			config.SpawnerMonsters = "zombie,skeleton,spider";
			config.SpawnerQuantity = 10;
			config.ProbabilityToSpawn = 1;
			config.MaxMonstersPerSpawn = 5;
			config.DelayBeforeRegeneration = 300;
			config.HealthModifier = 0.0;
			config.IntervalBetweenSpawn = 1500;
			config.SpawnerRadiusActivation = 20;
			config.DelayBeforeRespawn = 0;	
			config.UseInfestedZoneAsRadius = false;
			config.ResetWhenNoPlayerAround = true;
			config.Server = getServer();

			_ruinsPlateform = new Infestation(this, config);
			ZoneServer.getInstance().addListener(_ruinsPlateform);
			getServer().getPluginManager().registerEvents(_ruinsPlateform, this);
		}
	}
	
	private void releaseRuinsPlateform() {
		if (_ruinsPlateform != null) {
			ZoneServer.getInstance().removeListener(_ruinsPlateform);
		}
	}
	
	private void createHauntedOutpost() {
		if (_hauntedOutpost == null) {
			
			MonsterFuryConfiguration _defaultConfig = new MonsterFuryConfiguration();
			_defaultConfig.ExperienceReward = 5;										// 100 level d'exp
			_defaultConfig.CoolDownTime = 600000; 										// 10 min
			_defaultConfig.InitialTimeBeforeFirstWave = 10000;							// 10 sec
			_defaultConfig.TimeoutWhenLeaving = 30000;									// 30 sec
			_defaultConfig.MinimumPlayer = 1;											// 1 joueur requis
			_defaultConfig.MonstersPerWave = 35;							    		 // 10 monstres par wave
			_defaultConfig.Name = "haunted_outpost";									// le nom du sc�nario 
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

// configuration de la maison a zoune 2 enderman
/*
InfestationConfiguration zounehouse = new InfestationConfiguration();
zounehouse.InfestedZone = "46,-8,54,0,98,102";
zounehouse.BurnDuringDaylight = false;
zounehouse.UseInfestedZoneAsRadius = true;
zounehouse.RegenerateExplodedBlocks = true;
zounehouse.UseLowestBlock = false;
zounehouse.SpawnerMonsters = "zombie";
zounehouse.SpawnerQuantity = 1;
zounehouse.ProbabilityToSpawn = 1;
zounehouse.MaxMonstersPerSpawn = 2;
zounehouse.DelayBeforeRegeneration = 150;
zounehouse.HealthModifier = 1.0;
zounehouse.IntervalBetweenSpawn = 1500;
zounehouse.SpawnerRadiusActivation = 1;
zounehouse.DelayBeforeRespawn = 120000;	
config.ResetWhenNoPlayerAround = false;
zounehouse.Server = getServer();
//Infestation zounehousesurprise = new Infestation(this, zounehouse);
//ZoneServer.getInstance().addListener(zounehousesurprise);
//getServer().getPluginManager().registerEvents(zounehousesurprise, this);



// configuration de la zone du chateau
InfestationConfiguration config1 = InfestationConfiguration.clone(config);
config1.InfestedZone = "-73,-355,134,-22,50,120";
config1.HealthModifier = 0.0;
config1.ProbabilityToSpawn = 2;
config1.MaxMonstersPerSpawn = 5;
config1.SpawnerQuantity = 100;
config1.SpawnerMonsters = "zombie,skeleton,spider,creeper";
config1.UseLowestBlock = false;
config1.DelayBeforeRespawn = 600000;
config1.SpawnerRadiusActivation = 10;
// creation de l'instance
//Infestation inf1 = new Infestation(this, config1);
//ZoneServer.getInstance().addListener(inf1);
//getServer().getPluginManager().registerEvents(inf1, this);

//createBarbarianRaid();
//createHauntedOutpost();
//createDefaultWaves();
*/