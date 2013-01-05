package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.ambush.Ambush;
import ca.qc.icerealm.bukkit.plugins.scenarios.barbarian.BarbarianRaid;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.services.EventCommander;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.services.EventService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.Infestation;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationCommander;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.DefaultEventListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.RandomWaveGenerator;
import ca.qc.icerealm.bukkit.plugins.scenarios.obsidian.BreakBlockSession;
import ca.qc.icerealm.bukkit.plugins.scenarios.obsidian.ObsidianMission;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.LoggerFormater;
import ca.qc.icerealm.bukkit.plugins.scenarios.waves.EntityWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.waves.RegularSpawnWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneProber;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioPlugin extends JavaPlugin {
	public final static Logger logger = Logger.getLogger(("Scenario"));
	public final static Logger mainLogger = Logger.getLogger("Minecraft");
	private String _filename = "sc_logs/scenario.log";
	
	private MonsterFury _hauntedOutpost = null;
	private Infestation _ruinsPlateform = null;
	private Infestation _castleSiege = null;
	private MonsterFury _moonTemple = null;
	private BarbarianRaid _raid = null;
	private ObsidianMission _obsidianMission = null;
	private Frontier _frontier = null;
	private Ambush _ambush = null;
		
	private ZoneSubject _zoneServer;
	private ScenarioZoneProber _prober;
	private FileHandler _logFile; 
	private Formatter _logFormat;

	@Override
	public void onDisable() {
		releaseHauntedOutpost();	
		releaseRuinsPlateform();
		releaseCastleSiege();
		releaseObsidianMission();
	}

	@Override
	public void onEnable() {
		
		initFileLogger(_filename);
		initZoneServer(getServer());
		/*
		_invicibleMonsters = new InvicibleMonsterServer();
		getServer().getPluginManager().registerEvents(_invicibleMonsters, this);
		*/
		// scenario commander
		getCommand("sc").setExecutor(new ScenarioCommander());
		ScenarioService.getInstance().setPlugin(this);
		getCommand("ev").setExecutor(new EventCommander(EventService.getInstance()));
		
		// creation des different scenarios
		//createHauntedOutpost();
		//createRuinsPlateform();
		//createCastleSiege();
		//createObsidianMission();
		//createBarbarianRaid();
		//createMoonTemple();
		createFrontier();
		createAmbush();
		//createArena();
	}
	
	public void initZoneServer(Server s) {
		_zoneServer = new ScenarioZoneServer(s);
		_prober = new ScenarioZoneProber(_zoneServer);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(_prober, 0, 20, TimeUnit.MILLISECONDS);
	}
	
	public void initFileLogger(String name) {
		// init le logger pour mettre ca dans un fichier
		try {
			logger.setLevel(Level.FINE);
			_logFile = new FileHandler(name, true);
			_logFormat = new LoggerFormater();
			_logFile.setFormatter(_logFormat);
			logger.addHandler(_logFile);
		} 
		catch (Exception e) {
			logger.info("[Scenarios] Could not create the log file " + name);
		}	
	}
	
	private void createArena() {
		MonsterFuryConfiguration config = new MonsterFuryConfiguration();
		config.ScenarioZoneCoords = "-28,168,-10,186,55,61";
		config.ActivationZoneCoords = "-19,176,-18,177,55,61";
		config.MinimumPlayer = 1;
		config.CoolDownTime = 480000;
		config.InitialTimeBeforeFirstWave = 5000;
		config.MonstersPerWave = 3;
		config.NumberOfWaves = 5;
		config.TimeBetweenWave = 10000;
		config.TimeoutWhenLeaving = 40000;
		config.ZoneServer = ZoneServer.getInstance();
		config.HealthModifier = 0.33;
		config.ExperienceReward = 10;
		config.Name = "Arena";
		
		MonsterFury arena = new MonsterFury(this, config, new DefaultEventListener());
		
		World world = getServer().getWorld("world");
		Location first = new Location(world, -11, 59, 184);
		Location second = new Location(world, -11, 59, 169);
		Location third = new Location(world, -25, 59, 184);
		Location fourth = new Location(world, -25, 59, 169);
		List<Location> locations = new ArrayList<Location>();
		locations.add(first);
		locations.add(second);
		locations.add(third);
		locations.add(fourth);
		
		String[] possibleMix = new String[] { "zombie,spider", "zombie", "pigzombie", "cavespider,spider", "zombie,spider,pigzombie" };
		RandomWaveGenerator generator = new RandomWaveGenerator(arena, locations, possibleMix, true);
		arena.setWaveGenerator(generator);
		ScenarioService.getInstance().addScenario(arena);
		
	}
	
	private void createFrontier() {
		
		// a chaque 50m du spawn point, 100% plus fort, 33% plus de dommage
		//_frontier = new Frontier(getServer().getWorld("world"), 400, 3);
		_frontier = Frontier.getInstance();
		getServer().getPluginManager().registerEvents(_frontier, this);
		getCommand("fr").setExecutor(_frontier);
		
		if (_frontier != null) {
			logger.info("[Scenarios] Frontier feature is enabled");
		}
	}
	
	private void createAmbush() {
		// tirage au sort chaque 5 minutes, 1/15 de probabilité, radius de 25m, 10 sec avant spawn, 4 monstres
		_ambush = new Ambush(this, 300000, 15, 25, 10000, 4);
		getCommand("am").setExecutor(_ambush);
		logger.info("[Scenarios] Ambush feature is enabled");
	}
	
	private void createObsidianMission() {
		
		_obsidianMission = new ObsidianMission(_zoneServer);
		
		List<BreakBlockSession> session = new ArrayList<BreakBlockSession>();
		
		// desert 1
		Location obsidianLocation = new Location(getServer().getWorld("world"), 205,69,1007);
		BreakBlockSession desert = new BreakBlockSession(obsidianLocation, Material.STONE, 10, 1, "zombie");
		session.add(desert);
		
		// desert 2
		Location obsidianLocation1 = new Location(getServer().getWorld("world"), 247,70,1033);
		BreakBlockSession desert1 = new BreakBlockSession(obsidianLocation1, Material.GLOWSTONE, 10, 1, "zombie");
		session.add(desert1);
		
		Location obsidianLocation2 = new Location(getServer().getWorld("world"), 254,68,1011);
		BreakBlockSession desert2 = new BreakBlockSession(obsidianLocation2, Material.OBSIDIAN, 10, 1, "zombie");
		session.add(desert2);
		
		_obsidianMission.setCooldownTime(60000);
		_obsidianMission.setReward(new int[] { 314, 315, 316, 317 });
		_obsidianMission.setBreakBlockSession(session);
		_obsidianMission.activateBlockZone();
		
		getServer().getPluginManager().registerEvents(_obsidianMission, this);
		
		if (_obsidianMission != null) {
			logger.info("[Scenarios] Obsidian Mission created and activated");
		}
	}
	
	private void releaseObsidianMission() {
		
	}
	
	private void createBarbarianRaid() {
		if (_raid == null) {
			_raid  = new BarbarianRaid(this, _zoneServer);
		}
	}
	
	private void createMoonTemple() {
		
		MonsterFuryConfiguration _defaultConfig = new MonsterFuryConfiguration();
		_defaultConfig.ExperienceReward = 5;										// 100 level d'exp
		_defaultConfig.CoolDownTime = 600000; 										// 10 min
		_defaultConfig.InitialTimeBeforeFirstWave = 10000;							// 10 sec
		_defaultConfig.TimeoutWhenLeaving = 30000;									// 30 sec
		_defaultConfig.MinimumPlayer = 1;											// 1 joueur requis
		_defaultConfig.MonstersPerWave = 3;							    		 	// 10 monstres par wave
		_defaultConfig.Name = "moon_temple";										// le nom du scénario 
		_defaultConfig.NumberOfWaves = 1;											// 3 waves
		_defaultConfig.TimeBetweenWave = 10000;										// 10 sec
		_defaultConfig.HealthModifier = 10;											//
		_defaultConfig.ActivationZoneCoords = "284,-381,289,-376,0,128";			// zone d'Activation
		_defaultConfig.ScenarioZoneCoords = "277,-388,296,-369,0,128";				// zone du scenario
		_defaultConfig.ZoneServer = _zoneServer;
		
		_moonTemple = new MonsterFury(this, _defaultConfig, new DefaultEventListener(), null);
		
		// creation de plusieurs wave
		List<EntityWave> waves = new ArrayList<EntityWave>();
		WorldZone scenarioZone = new WorldZone(getServer().getWorld("world"), _defaultConfig.ScenarioZoneCoords);
		List<Location> locations = scenarioZone.getFourSide(2);
		
		for (int i = 0; i < _defaultConfig.NumberOfWaves; i++) {
			EntityWave wave = new RegularSpawnWave(1000, _moonTemple, _defaultConfig.MonstersPerWave, _defaultConfig.HealthModifier);
			wave.setMonsters("skeleton,zombie,spider");
			wave.setSpawnLocation(locations);
			waves.add(wave);
		}
		
		EntityWave wave = new RegularSpawnWave(1000, _moonTemple, 2, 20);
		wave.setMonsters("zombie");
		wave.setSpawnLocation(scenarioZone.getRandomLocation(getServer().getWorld("world"), 1));
		waves.add(wave);
		_moonTemple.setEntityWaves(waves);
		ScenarioService.getInstance().addScenario(_moonTemple);	
		
		/*
		if (_moonTemple == null) {
			InfestationConfiguration config = new InfestationConfiguration();
			config.InfestedZone = "277,-388,296,-369,0,128";
			config.BurnDuringDaylight = false;
			config.RegenerateExplodedBlocks = true;
			config.DelayBeforeRegeneration = 100;
			config.UseInfestedZoneAsRadius = true;
			config.SpawnerMonsters = "zombie";
			config.SpawnerQuantity = 1;
			config.ProbabilityToSpawn = 1;
			config.MaxMonstersPerSpawn = 1;
			config.RareDropMultiplier = 5.0;
			
			config.HealthModifier = 40.0;
			config.IntervalBetweenSpawn = 1500;
			config.SpawnerRadiusActivation = 15;
			config.DelayBeforeRespawn = 500000;

			config.EnterZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.YELLOW + "Welcome to the " + ChatColor.DARK_GRAY + "Infested Castle." + ChatColor.DARK_RED + " Watch your back!";
			config.LeaveZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.DARK_AQUA + "You are leaving the " + ChatColor.DARK_GRAY + "Infested Castle.";
			config.Server = getServer();
			
			_moonTemple = new Infestation(this, config, _zoneServer);
			_zoneServer.addListener(_moonTemple);
			getServer().getPluginManager().registerEvents(_moonTemple, this);
			
			InfestationConfiguration subordinates = new InfestationConfiguration();
			subordinates.InfestedZone = "277,-388,296,-369,0,128";
			subordinates.BurnDuringDaylight = false;
			subordinates.RegenerateExplodedBlocks = true;
			subordinates.DelayBeforeRegeneration = 100;
			subordinates.UseInfestedZoneAsRadius = true;
			subordinates.SpawnerMonsters = "skeleton,spider";
			subordinates.SpawnerQuantity = 3;
			subordinates.ProbabilityToSpawn = 1;
			subordinates.MaxMonstersPerSpawn = 1;
			subordinates.RareDropMultiplier = 0.0;
			
			subordinates.HealthModifier = 2;
			subordinates.IntervalBetweenSpawn = 1500;
			subordinates.SpawnerRadiusActivation = 10;
			subordinates.DelayBeforeRespawn = 30000;

			subordinates.EnterZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.YELLOW + "Welcome to the " + ChatColor.DARK_GRAY + "Infested Castle." + ChatColor.DARK_RED + " Watch your back!";
			subordinates.LeaveZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.DARK_AQUA + "You are leaving the " + ChatColor.DARK_GRAY + "Infested Castle.";
			subordinates.Server = getServer();
			
			Infestation subordinatesInf = new Infestation(this, subordinates, _zoneServer);
			_zoneServer.addListener(subordinatesInf);
			getServer().getPluginManager().registerEvents(subordinatesInf, this);
			
			getCommand("mt").setExecutor(new InfestationCommander(_moonTemple, "mt"));
			getCommand("sbmt").setExecutor(new InfestationCommander(_moonTemple, "sbmt"));
		}
		

		if (_moonTemple != null) {
			logger.info("[Scenarios] Moon Temple created!");
		}
		*/
	}
	
	private void createCastleSiege() {
		if (_castleSiege == null) {
			InfestationConfiguration config = new InfestationConfiguration();
			//config.InfestedZone = "147,270,163,289,0,128";
			config.InfestedZone = "-250,70,-233,84,0,128";
			config.ResetWhenPlayerLeave = true;
			config.BurnDuringDaylight = false;
			config.RegenerateExplodedBlocks = true;
			config.DelayBeforeRegeneration = 60000;
			config.UseInfestedZoneAsRadius = false;
			config.SpawnerMonsters = "zombie,skeleton,spider";
			config.SpawnerQuantity = 1;
			config.ProbabilityToSpawn = 1;
			config.MaxMonstersPerSpawn = 1;
			config.RareDropMultiplier = 2.0;
			
			config.HealthModifier = 20.0;
			config.IntervalBetweenSpawn = 1500;
			config.SpawnerRadiusActivation = 15;
			config.DelayBeforeRespawn = 60000;

			config.EnterZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.YELLOW + "Welcome to the " + ChatColor.DARK_GRAY + "Infested Castle." + ChatColor.DARK_RED + " Watch your back!";
			config.LeaveZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.DARK_AQUA + "You are leaving the " + ChatColor.DARK_GRAY + "Infested Castle.";
			config.Server = getServer();
			
			_castleSiege = new Infestation(this, config, _zoneServer);
			_zoneServer.addListener(_castleSiege);
			getServer().getPluginManager().registerEvents(_castleSiege, this);
			getCommand("cs").setExecutor(new InfestationCommander(_castleSiege, "cs"));
		
		}
		
		if (_castleSiege != null) {
			logger.info("[Scenarios] Infested Castle created");
		}
	}
	
	private void releaseCastleSiege() {
		_zoneServer.removeListener(_castleSiege);
	}

	private void createRuinsPlateform() {
		// configuration de la plate forme
		if (_ruinsPlateform == null) {
			InfestationConfiguration config = new InfestationConfiguration();
			config.InfestedZone = "26,-75,118,26,90,128";
			config.BurnDuringDaylight = false;
			config.RegenerateExplodedBlocks = true;
			config.DelayBeforeRegeneration = 300;
			config.UseInfestedZoneAsRadius = false;
			config.SpawnerMonsters = "zombie,skeleton,spider";
			config.SpawnerQuantity = 10;
			config.ProbabilityToSpawn = 1;
			config.MaxMonstersPerSpawn = 5;
			config.RareDropMultiplier = 2.0;
			config.HealthModifier = 0.0;
			config.IntervalBetweenSpawn = 1500;
			config.SpawnerRadiusActivation = 20;
			config.DelayBeforeRespawn = 30000;
			config.EnterZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.YELLOW + "You are entering an infested zone. " + ChatColor.RED + "Watch your back!";
			config.LeaveZoneMessage = ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.DARK_AQUA + "You are leaving the infested zone";
			config.Server = getServer();
			
			_ruinsPlateform = new Infestation(this, config, _zoneServer);
			_zoneServer.addListener(_ruinsPlateform);
			getServer().getPluginManager().registerEvents(_ruinsPlateform, this);
			getCommand("pl").setExecutor(new InfestationCommander(_ruinsPlateform, "pl"));
		}
		
		if (_ruinsPlateform != null) {
			logger.info("[Scenarios] Ruins Plateform is created");
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
			_defaultConfig.HealthModifier = 0.0;
			_defaultConfig.ActivationZoneCoords = "366,181,369,184,0,128";				// zone d'Activation
			_defaultConfig.ScenarioZoneCoords = "347,168,386,201,0,128";				// zone du scenario
			_defaultConfig.ZoneServer = _zoneServer;
			
			_hauntedOutpost = new MonsterFury(this, _defaultConfig, new DefaultEventListener(), null);
			
			// creation de plusieurs wave
			List<EntityWave> waves = new ArrayList<EntityWave>();
			WorldZone scenarioZone = new WorldZone(getServer().getWorld("world"), _defaultConfig.ScenarioZoneCoords);
			List<Location> locations = scenarioZone.getFourSide(2);
			
			for (int i = 0; i < _defaultConfig.NumberOfWaves; i++) {
				EntityWave wave = new RegularSpawnWave(1000, _hauntedOutpost, _defaultConfig.MonstersPerWave, _defaultConfig.HealthModifier);
				wave.setMonsters("skeleton,zombie,spider");
				wave.setSpawnLocation(locations);
				waves.add(wave);
			}
			
			_hauntedOutpost.setEntityWaves(waves);
			ScenarioService.getInstance().addScenario(_hauntedOutpost);	
		}
		
		if (_hauntedOutpost != null) {
			logger.info("[Scenarios] Haunted Outpost is created");
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