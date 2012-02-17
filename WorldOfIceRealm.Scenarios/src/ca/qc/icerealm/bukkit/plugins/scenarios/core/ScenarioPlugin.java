package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryConfiguration;



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
		_defaultConfig.CoolDownTime = 600000; 								// 10 sec
		_defaultConfig.InitialTimeBeforeFirstWave = 10000;					// 10 sec
		_defaultConfig.MinimumPlayer = 1;									// 1 joueur requis
		_defaultConfig.MonstersPerWave = 35;								// 10 monstres par wave
		_defaultConfig.Name = "1";											// le nom du scénario 
		_defaultConfig.NumberOfWaves = 5;									// 3 waves
		_defaultConfig.TimeBetweenWave = 10000;								// 10 sec
		_defaultConfig.ActivationZoneCoords = "366,181,369,184,0,128";		// zone d'Activation
		_defaultConfig.ScenarioZoneCoords = "347,168,386,201,0,128";		// zone du scenario
		
		
		_monster = new MonsterFury(this, _defaultConfig);
		ScenarioService.getInstance().addScenario(_monster);
		
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