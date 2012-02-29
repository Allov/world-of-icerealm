package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.plugin.java.JavaPlugin;

public class ScenarioService {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private List<Scenario> _registeredScenario;
	private JavaPlugin _plugin;
	
	private static ScenarioService _instance;
	
	protected ScenarioService() {
		_registeredScenario = new ArrayList<Scenario>();
	}

	public static ScenarioService getInstance() {
		if (_instance == null) {
			_instance = new ScenarioService();
		}
		return _instance;
	}
	
	public void addScenario(Scenario s) {
		if (s != null) {
			_registeredScenario.add(s);
		}
	}
	
	public void removeScenario(Scenario s) {
		if (s != null) {
			_registeredScenario.remove(s);
		}
	}
	
	public JavaPlugin getPlugin() {
		return _plugin;
	}
	
	public void setPlugin(JavaPlugin j) {
		_plugin = j;
	}
	
	public List<Scenario> getScenarios() {
		return _registeredScenario;
	}
	
	public List<Scenario> findScenarios(Scenario criteria) {
		List<Scenario> list = new ArrayList<Scenario>();
		for (Scenario s : _registeredScenario) {
			if (s.getName().equalsIgnoreCase(criteria.getName())) {
				list.add(s);
			}
		}
		return list;
	}
	
	public List<Scenario> findScenarios(String name) {
		List<Scenario> list = new ArrayList<Scenario>();
		for (Scenario s : _registeredScenario) {
			if (s.getName().equalsIgnoreCase(name)) {
				list.add(s);
			}
		}
		return list;
	}
	
	public LivingEntity spawnCreature(World w, Location l, CreatureType t) {
		return w.spawnCreature(l, t);
	}
	
	public LivingEntity spawnCreature(World w, Location l, CreatureType t, boolean burn) {
		LivingEntity creature = this.spawnCreature(w, l, t);
		//StrongerMonster m = new StrongerMonster(creature, creature.getMaxHealth(), burn);
		//_plugin.getServer().getPluginManager().registerEvents(m, _plugin);
		return creature;
	}
	
	public LivingEntity spawnCreature(World w, Location l, CreatureType t, int maxHealth) {
		return spawnCreature(w, l, t, maxHealth, true);
	}
	
	public LivingEntity spawnCreature(World w, Location l, CreatureType t, double modifier) {
		return spawnCreature(w, l, t, modifier, true);
	}
	
	public LivingEntity spawnCreature(World w, Location l, CreatureType t, double modifier, boolean burn) {
		LivingEntity creature = this.spawnCreature(w, l, t);
		int maxHealth = creature.getMaxHealth() + (int)(modifier * creature.getMaxHealth());
		//StrongerMonster m = new StrongerMonster(creature, maxHealth, burn);
		//_plugin.getServer().getPluginManager().registerEvents(m, _plugin);
		return creature;
	}
	
	public LivingEntity spawnCreature(World w, Location l, CreatureType t, int maxHealth, boolean burn) {
		LivingEntity creature = this.spawnCreature(w, l, t);
		//StrongerMonster m = new StrongerMonster(creature, maxHealth, burn);
		//_plugin.getServer().getPluginManager().registerEvents(m, _plugin);
		return creature;
	}
	
}
