package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.EntityCreature;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultipliers;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CustomMonster;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CustomMonsterListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityReflection;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneProber;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioService {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private List<Scenario> _registeredScenario;
	private JavaPlugin _plugin;
	private CustomMonsterListener _customMonster;
	//private Frontier _frontier;
	private ZoneSubject _zoneServer;
	private ScenarioZoneProber _prober;
	
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
		
	@Deprecated
	public double calculateHealthModifierWithFrontier(Location l, Location spawn) {
		return Frontier.getInstance().calculateHealthModifier(l, spawn);
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
		/*
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		int maxHealth = creature.getMaxHealth() + (int)(modifier * creature.getMaxHealth());
		logger.info(creature.toString() + " creature max heatlh: " + creature.getMaxHealth() + " will be at: " + maxHealth);
		if (_customMonster != null && ((!burn) || (maxHealth != creature.getMaxHealth()))) {
			_customMonster.addMonster(creature.getEntityId(), maxHealth, burn);
		}
		*/
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		int maxHealth = creature.getMaxHealth() + (int)(modifier * creature.getMaxHealth());
		EntityCreature entityCreature = EntityReflection.getEntityCreature(creature);
		EntityReflection.setEntityPropertyValue(entityCreature, entityCreature.getClass(), EntityReflection.HEALTH, maxHealth);
		EntityReflection.setEntityPropertyValue(entityCreature, entityCreature.getClass(), EntityReflection.FIRE_PROOF, !burn);
		return creature;
	}
	
	public Entity spawnCreature(World w, Location l, EntityType t, int maxHealth, boolean burn) {
		//Frontier.getInstance().setActivated(false);
		
		//Frontier.getInstance().setActivated(true);
		/*
		if (_customMonster != null && ((!burn) || (maxHealth != creature.getMaxHealth()))) {
			_customMonster.addMonster(creature.getEntityId(), maxHealth, burn);
		}
		*/
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		EntityCreature entityCreature = EntityReflection.getEntityCreature(creature);
		EntityReflection.setEntityPropertyValue(entityCreature, entityCreature.getClass(), EntityReflection.HEALTH, maxHealth);
		EntityReflection.setEntityPropertyValue(entityCreature, entityCreature.getClass(), EntityReflection.FIRE_PROOF, !burn);
		return creature;
	}
	
	@Deprecated
	public void addExistingEntity(Integer id, int health) {
		this.addExistingEntity(id, health, true);
	}
	
	@Deprecated
	public void addExistingEntity(Integer id, int health, boolean burn) {
		_customMonster.addMonster(id, health, burn, false, 0.0);
	}
	
	@Deprecated
	public void addExistingEntity(Integer id, int health, boolean burn, double damage) {
		if (_customMonster != null) {
			_customMonster.addMonster(id, health, burn, false, damage);
		}
	}
	
	@Deprecated
	public void updateExistingEntity(Integer id, double modifier, double damage) {
		if (_customMonster != null) {
			_customMonster.updateExistingMonster(id, modifier, damage);
		}
		
	}
	
	public void updateExistingEntity(LivingEntity e, double modifier, double damage) {
		EntityCreature creature = EntityReflection.getEntityCreature(e);
		int currentHealth = EntityReflection.getEntityPropertyValue(creature, creature.getClass(), EntityReflection.HEALTH);
		int newHealth = currentHealth + (int)(currentHealth * modifier);
		EntityReflection.setEntityPropertyValue(creature, creature.getClass(), EntityReflection.HEALTH, newHealth);
	}
	
	@Deprecated
	/**
	 * Utilisez Frontier.getInstance().calculateGlobalModifier(Location loc) à la place
	 */
	public Entity spawnCreatureWithPotion(World w, Location l, EntityType t, PotionEffect p, boolean burn) {
		List<PotionEffect> potions = new ArrayList<PotionEffect>();
		return this.spawnCreatureWithPotion(w, l, t, potions, burn);
	}
	
	@Deprecated 
	public Entity spawnCreatureWithPotion(World w, Location l, EntityType t, List<PotionEffect> p, boolean burn) {
		//Frontier.getInstance().setActivated(false);
		LivingEntity entity = (LivingEntity)this.spawnCreature(w, l, t);
		//Frontier.getInstance().setActivated(true);
		
		entity.addPotionEffects(p);
		if (!burn && _customMonster != null) {
			_customMonster.addMonster(entity.getEntityId(), burn);
		}
		
		return entity;
	}

	public void attachRareDropMultiplierToEntity(int id, double d) {
		
		if (_plugin.getServer().getPluginManager().isPluginEnabled("WoI.RareDrops")) {
			RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(id, new RareDropsMultipliers(d));	
		}
		
	}
	
	@Deprecated
	public CustomMonster getCustomMonsterEntity(int id) {
		if (_customMonster != null && _customMonster.monsterAlreadyAdded(id)) {
			return _customMonster.getCustomMonsterEntity(id);
		}
		return null;
	}
	
	@Deprecated
	public boolean monsterAlreadyPresent(Integer id) {
		return _customMonster.monsterAlreadyAdded(id);
	}
	
	public void logInfo(String m) {
		this.logger.info(m);
	}
	
	@Deprecated
	public void removeEntityFromCustomMonster(int id) {
		if (_customMonster != null) {
			_customMonster.removeMonster(id);	
		}
	}
}
