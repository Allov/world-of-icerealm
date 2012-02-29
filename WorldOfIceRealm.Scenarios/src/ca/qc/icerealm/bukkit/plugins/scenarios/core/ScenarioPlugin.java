package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.Infestation;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.DefaultEventListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.RegularSpawnWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneProber;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneServer;

import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioPlugin extends JavaPlugin {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private MonsterFury _hauntedOutpost = null;
	private Infestation _ruinsPlateform = null;
	private ZoneSubject _zoneServer;
	private ScenarioZoneProber _prober;

	@Override
	public void onDisable() {
		releaseHauntedOutpost();	
		releaseRuinsPlateform();
	}

	@Override
	public void onEnable() {
		// part le zone server pour les scenarios
		_zoneServer = new ScenarioZoneServer(getServer());
		_prober = new ScenarioZoneProber(_zoneServer);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(_prober, 0, 20, TimeUnit.MILLISECONDS);

		// part le scenario commander
		getCommand("sc").setExecutor(new ScenarioCommander());
		ScenarioService.getInstance().setPlugin(this);
		
		// creation des different scenario
		createHauntedOutpost();
		createRuinsPlateform();
	}

	private void createRuinsPlateform() {
		// configuration de la plate forme
		if (_ruinsPlateform == null) {
			InfestationConfiguration config = new InfestationConfiguration();
			config.InfestedZone = "26,-75,118,26,92,127";
			config.BurnDuringDaylight = false;
			config.RegenerateExplodedBlocks = true;
			config.DelayBeforeRegeneration = 300;
			config.UseLowestBlock = true;
			config.UseInfestedZoneAsRadius = false;
			config.ResetWhenNoPlayerAround = true;
			config.SpawnerMonsters = "zombie,skeleton,spider";
			config.SpawnerQuantity = 10;
			config.ProbabilityToSpawn = 1;
			config.MaxMonstersPerSpawn = 5;
			
			config.HealthModifier = 0.0;
			config.IntervalBetweenSpawn = 1500;
			config.SpawnerRadiusActivation = 20;
			config.DelayBeforeRespawn = 0;	

			config.EnterZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.YELLOW + "You are entering an infested zone. " + ChatColor.RED + "Watch your back!";
			config.LeaveZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.DARK_AQUA + "You are leaving the infested zone";
			config.Server = getServer();
			
			_ruinsPlateform = new Infestation(this, config, _zoneServer);
			_zoneServer.addListener(_ruinsPlateform);
			getServer().getPluginManager().registerEvents(_ruinsPlateform, this);
		}
	}
	
	private void releaseRuinsPlateform() {
		if (_ruinsPlateform != null) {
			_zoneServer.removeListener(_ruinsPlateform);
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