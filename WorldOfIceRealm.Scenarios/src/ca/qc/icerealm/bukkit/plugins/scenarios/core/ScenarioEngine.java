package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.material.Furnace;
import org.bukkit.plugin.java.JavaPlugin;


class ScenarioEngine {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private final String CMD_IDENTIFIER = "sc";
	private final String NORMAL_WORLD_IDENTIFIER = "world";
	private final long PROBING_INTERVAL = 100;
	
	private Server _currentServer;
	private boolean _isInitialized = false;
	private List<Scenario> _scenarios;
	private World _normalWorld;
	private ProbingWorker _probing;
	private ScenarioBuilder _builder;
	
	private static ScenarioEngine _instance = null;
	public ScenarioEngine() {
		_scenarios = new ArrayList<Scenario>();
	}
	
	public static ScenarioEngine getInstance() {
		if (_instance == null) {
			_instance = new ScenarioEngine();
		}
		return _instance;
	}
	
	public boolean isScenarioActive() {
		return true;
	}
	
	public Scenario findScenarioByPlayer(Player p) {
		for (Scenario s : _scenarios) {
			if (s.getPlayers().contains(p)) {
				return s;
			}
		}
		return null;
	}
	
	public Scenario findScenarioAtLocation(Location p) {
		for (Scenario s : _scenarios) {
			if (s.getZone().isInside(p)) {
				return s;
			}
		}
		return null;
	}
	
	public List<Scenario> findAllScenarioAtLocation(Location p) {
		List<Scenario> list = new ArrayList<Scenario>();
		for (Scenario s : _scenarios) {
			if (s.getZone().isInside(p)) {
				list.add(s);
			}
		}
		return list;
	}
	
	public void initializeScenarioEngine(JavaPlugin plugin) {
		_currentServer = plugin.getServer();
		
		if (!_isInitialized) {
			plugin.getCommand(CMD_IDENTIFIER).setExecutor(new ScenarioCommander(this, _currentServer));
			_normalWorld = _currentServer.getWorld(NORMAL_WORLD_IDENTIFIER);
			
			_builder = new ScenarioBuilder(plugin.getDataFolder() + "scenarios.yml", _currentServer, _normalWorld, plugin);
			_scenarios = _builder.getScenariosFromConfigFile();			
			
			// starting the prober
			_probing = new ProbingWorker(_currentServer, this, PROBING_INTERVAL);
			Thread t = new Thread(_probing);
			t.start();
			
			// the scenario engine is now functional!
			_isInitialized = true;
		}
	}
	
	public ScenarioBuilder getBuilder() {
		return _builder;
	}
	
	public void shutdownScenarioEngine() {
		_probing.stopProbing();
	}
	
	public List<Scenario> getScenarios() {
		if (_scenarios == null) {
			_scenarios = new ArrayList<Scenario>();
		}
		return _scenarios;
	}
	
	public void killAllMonsters() {
		for (Entity e : _normalWorld.getEntities()) {
			e.remove();
		}
		
	}
}
