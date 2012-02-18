package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;



public class ScenarioPlugin extends JavaPlugin {
	
	private MonsterFury _monster;
	private MonsterFuryConfiguration _defaultConfig;
	
	@Override
	public void onDisable() {
		_monster.removeAllListener();
	}

	@Override
	public void onEnable() {
		getCommand("sc").setExecutor(new ScenarioCommander());
		
		
		
		
		// set les scénarios pré enregistré ici!
		_defaultConfig = new MonsterFuryConfiguration();
		_defaultConfig.ExperienceReward = 5;								// 100 level d'exp
		_defaultConfig.CoolDownTime = 20000; 								// 10 sec
		_defaultConfig.InitialTimeBeforeFirstWave = 10000;					// 10 sec
		_defaultConfig.TimeoutWhenLeaving = 30000;							// 30 sec
		_defaultConfig.MinimumPlayer = 1;									// 1 joueur requis
		_defaultConfig.MonstersPerWave = 2;								// 10 monstres par wave
		_defaultConfig.Name = "1";											// le nom du scénario 
		_defaultConfig.NumberOfWaves = 3;									// 3 waves
		_defaultConfig.TimeBetweenWave = 10000;								// 10 sec
		_defaultConfig.ActivationZoneCoords = "-180,134,-177,137,0,128";	// zone d'Activation
		_defaultConfig.ScenarioZoneCoords = "-189,127,-168,140,0,128";		// zone du scenario
		
		
		_monster = new MonsterFury(this, _defaultConfig);
		ScenarioService.getInstance().addScenario(_monster);
		
		ZoneTester tester = new ZoneTester(this, "-185,131,-183,133,0,128");
		ZoneServer.getInstance().addListener(tester);
		
	}
}

class ZoneTester implements ZoneObserver {

	private WorldZone _world;
	private Server _server;
	
	public ZoneTester(JavaPlugin j, String zone) {
		_server = j.getServer();
		_world = new WorldZone(_server.getWorld("world"), zone);
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		// TODO Auto-generated method stub
		_world = z;
	}

	@Override
	public WorldZone getWorldZone() {
		// TODO Auto-generated method stub
		return _world;
	}

	@Override
	public void playerEntered(Player p) {
		// TODO Auto-generated method stub
		_server.broadcastMessage(p.getName() + " zonetester entered!");
	}

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		_server.broadcastMessage(p.getName() + " zonetester left!");
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return _server;
	}


	
}





/*
//_defaultConfig.ActivationZoneCoords = "366,181,369,184,0,128";	// zone d'Activation
		//_defaultConfig.ScenarioZoneCoords = "347,168,386,201,0,128";		// zone du scenario

//_activationZone = new WorldZone(_world, "-180,134,-177,137,0,128");
//_scenarioZone = new WorldZone(_world, "-189,127,-168,140,0,128");
//_activationZone = new WorldZone(_world, "147,-405,149,-403,0,128");
//_scenarioZone = new WorldZone(_world, "137,-416,155,-399,0,128");
*/