package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.FourCornerWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;



public class ScenarioPlugin extends JavaPlugin {
	
	private MonsterFury _monster;
    private MonsterFury _moontemple;
	private MonsterFuryConfiguration _defaultConfig;
    private MonsterFuryConfiguration _mtConfig;

	@Override
	public void onDisable() {
		_monster.removeAllListener();
        _moontemple.removeAllListener();
	}

	@Override
	public void onEnable() {
		getCommand("sc").setExecutor(new ScenarioCommander());


		// set les sc�narios pr� enregistr� ici!
		_defaultConfig = new MonsterFuryConfiguration();
		_defaultConfig.ExperienceReward = 5;								// 100 level d'exp
		_defaultConfig.CoolDownTime = 100000; 								// 10 min
		_defaultConfig.InitialTimeBeforeFirstWave = 10000;					// 10 sec
		_defaultConfig.TimeoutWhenLeaving = 30000;							// 30 sec
		_defaultConfig.MinimumPlayer = 1;									// 1 joueur requis
		_defaultConfig.MonstersPerWave = 25;								// 10 monstres par wave
		_defaultConfig.Name = "hp";											// le nom du sc�nario 
		_defaultConfig.NumberOfWaves = 5;									// 3 waves
		_defaultConfig.TimeBetweenWave = 10000;								// 10 sec
		_defaultConfig.ActivationZoneCoords = "-180,134,-177,137,0,128";	// zone d'Activation
		_defaultConfig.ScenarioZoneCoords = "-189,127,-168,140,0,128";		// zone du scenario
		
		_monster = new MonsterFury(this, _defaultConfig);

		//_monster.getEntityWaves().add(0, new FourCornerWave(_monster.getWorldZone(), _monster.getWorld(), _monster, 24, 2, 1500));
		
		ScenarioService.getInstance().addScenario(_monster);



        // Moon Temple
        _mtConfig = new MonsterFuryConfiguration();
        _mtConfig.ExperienceReward = 5;								// 100 level d'exp
        _mtConfig.CoolDownTime = 100000; 								// 10 min
        _mtConfig.InitialTimeBeforeFirstWave = 10000;					// 10 sec
        _mtConfig.TimeoutWhenLeaving = 30000;							// 30 sec
        _mtConfig.MinimumPlayer = 1;									// 1 joueur requis
        _mtConfig.MonstersPerWave = 32;								// 10 monstres par wave
        _mtConfig.Name = "moontemple";											// le nom du sc�nario
        _mtConfig.NumberOfWaves = 3;									// 3 waves
        _mtConfig.TimeBetweenWave = 10000;								// 10 sec
        _mtConfig.ActivationZoneCoords = "398,60,404,66,80,90";	// zone d'Activation
        _mtConfig.ScenarioZoneCoords = "380,43,421,84,64,90";		// zone du scenario


        _moontemple = new MonsterFury(this, _mtConfig);

        ScenarioService.getInstance().addScenario(_moontemple);



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