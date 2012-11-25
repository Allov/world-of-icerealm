package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CustomMonsterListener;

public class ScenarioService {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private List<Scenario> _registeredScenario;
	private JavaPlugin _plugin;
	private CustomMonsterListener _customMonster;
	private Frontier _frontier;
	
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
		_customMonster = new CustomMonsterListener();
		j.getServer().getPluginManager().registerEvents(_customMonster, _plugin);
	}
	
	public void setFrontier(Frontier f) {
		_frontier = f;
	}
	
	public double calculateHealthModifierWithFrontier(Location l, Location spawn) {
		return _frontier.calculateHealthModifier(l, spawn);
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
	
	public Entity spawnCreature(World w, Location l, EntityType t) {
		return w.spawnEntity(l, t);
	}
	
	public Entity spawnCreature(World w, Location l, EntityType t, int maxHealth) {
		return spawnCreature(w, l, t, maxHealth, true);
	}
	
	public Entity spawnCreature(World w, Location l, EntityType t, double modifier) {
		return spawnCreature(w, l, t, modifier, true);
	}
	
	public Entity spawnCreature(World w, Location l, EntityType t, double modifier, boolean burn) {
		_frontier.setActivated(false);
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		_frontier.setActivated(true);
		
		int maxHealth = creature.getMaxHealth() + (int)(modifier * creature.getMaxHealth());
		if (_customMonster != null && ((!burn) || (maxHealth != creature.getMaxHealth()))) {
			_customMonster.addMonster(creature.getEntityId(), maxHealth, burn);
		}
		
		return creature;
	}
	
	public Entity spawnCreature(World w, Location l, EntityType t, int maxHealth, boolean burn) {
		_frontier.setActivated(false);
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		_frontier.setActivated(true);
		
		if (_customMonster != null && ((!burn) || (maxHealth != creature.getMaxHealth()))) {
			_customMonster.addMonster(creature.getEntityId(), maxHealth, burn);
		}
		return creature;
	}
	
	public void addExistingEntity(Integer id, int health) {
		this.addExistingEntity(id, health, true);
	}
	
	public void addExistingEntity(Integer id, int health, boolean burn) {
		_customMonster.addMonster(id, health, burn, false, 0.0);
	}
	
	public void addExistingEntity(Integer id, int health, boolean burn, double damage) {
		if (_customMonster != null) {
			_customMonster.addMonster(id, health, burn, false, damage);
		}
	}
	
	public void attachRareDropMultiplierToEntity(int id, double d) {
		
		if (_plugin.getServer().getPluginManager().isPluginEnabled("WoI.RareDrops")) {
			RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(id, d);	
		}
		
	}
	
	public boolean monsterAlreadyPresent(Integer id) {
		return _customMonster.monsterAlreadyAdded(id);
	}
	
	public void logInfo(String m) {
		this.logger.info(m);
	}
	
}
