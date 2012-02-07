package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;


class ScenarioBuilder {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private File _configFile;
	private static FileConfiguration _scenarioConfig;
	private World _world;
	private Server _server;
	private JavaPlugin _plugin;
	
	public ScenarioBuilder(String config, Server s, World w, JavaPlugin plugin) {
		_configFile = new File(config);
		_scenarioConfig = YamlConfiguration.loadConfiguration(_configFile);
		
		InputStream defConfigStream = plugin.getResource("scenarios.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        _scenarioConfig.setDefaults(defConfig);
	        _scenarioConfig.options().copyDefaults(true);
	    }
		_server = s;
		_world = w;	
		_plugin = plugin;
	}
	
	public void initializeScenario(Scenario s) {
		s.setPlugin(_plugin);
		s.setWorld(_world);
		s.setServer(_server);
	}
	
	public List<Scenario> getScenariosFromConfigFile() {
		Integer i = 0;
		List<Scenario> scenarios = new ArrayList<Scenario>();
		Scenario s = null;
		
		while ((s = getScenarioType(_scenarioConfig.getString(i.toString() + ".type"))) != null) {
			
			String name = _scenarioConfig.getString(i.toString() + ".name");
			String zone = _scenarioConfig.getString(i.toString() + ".zone");
			
			s.setName(name);
			s.setZone(translateFromString(zone));
			s.setServer(_server);
			s.setWorld(_world);
			s.setPlugin(_plugin);
			scenarios.add(s);
			i++;
		}
		return scenarios;
	}
	
	public static FileConfiguration getScenarioConfiguration() {
		return _scenarioConfig;
	}
	
	private Scenario getScenarioType(String type) {
		// mini hack, si c'est null, c'est qu'on a pogné la fin du fichier
		if (type == null) {
			return null;
		}
		
		return ScenarioFactory.getInstanceOf(type);
	}
	
	private WorldZone translateFromString(String zone) {
		String[] coords = zone.split(",");
		double[] coordsDouble = null;
		
		if (coords.length == 4) {
			coordsDouble = new double[] { Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 
					   Double.parseDouble(coords[2]), Double.parseDouble(coords[3])};
		}
		else if (coords.length == 6) {
			coordsDouble = new double[] { Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 
					   Double.parseDouble(coords[2]), Double.parseDouble(coords[3]),
					   Double.parseDouble(coords[4]), Double.parseDouble(coords[5])};
		}
		
		if (coordsDouble != null) {
			Location lt = new Location(_world, coordsDouble[0], 20, coordsDouble[1]);
			Location rb = new Location(_world, coordsDouble[2], 20, coordsDouble[3]);
			return new WorldZone(lt, rb, coordsDouble[4], coordsDouble[5]);
		}
		
		return null;
		
	}
	
}
