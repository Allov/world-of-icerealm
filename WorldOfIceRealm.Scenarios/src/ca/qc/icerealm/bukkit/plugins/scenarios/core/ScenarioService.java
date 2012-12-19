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
	private static ScenarioService _instance;
	
	protected ScenarioService() {
		_registeredScenario = new ArrayList<Scenario>();
		_customMonster = new CustomMonsterListener();
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
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		int maxHealth = creature.getMaxHealth() + (int)(modifier * creature.getMaxHealth());
		EntityReflection.setEntityPropertyValue(creature, EntityReflection.HEALTH, maxHealth);
		_customMonster.updateFireproofMonster(creature, burn);
		_customMonster.updateDamageModifierMonster(creature, modifier);
		return creature;
	}
	
	public Entity spawnCreature(World w, Location l, EntityType t, int maxHealth, boolean burn) {
		LivingEntity creature = (LivingEntity)this.spawnCreature(w, l, t);
		EntityReflection.setEntityPropertyValue(creature, EntityReflection.HEALTH, maxHealth);
		_customMonster.updateFireproofMonster(creature, burn);
		return creature;
	}

	public void updateHealthModifier(LivingEntity e, double modifier) {
		int currentHealth = EntityReflection.getEntityPropertyValue(e, EntityReflection.HEALTH);
		int newHealth = currentHealth + (int)(currentHealth * modifier);
		EntityReflection.setEntityPropertyValue(e, EntityReflection.HEALTH, newHealth);
	}
	
	public void updateDamageModifier(LivingEntity e, double damage) {
		_customMonster.updateDamageModifierMonster(e, damage);
	}
	
	public void updateFireproof(LivingEntity e, boolean burn) {
		_customMonster.updateFireproofMonster(e, burn);
	}

	public void attachRareDropMultiplierToEntity(int id, double d) {
		if (_plugin.getServer().getPluginManager().isPluginEnabled("WoI.RareDrops")) {
			RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(id, new RareDropsMultipliers(d));	
		}
	}

	public void logInfo(String m) {
		this.logger.info(m);
	}

	@Deprecated
	public void removeEntityFromCustomMonster(int id) {
	}
}
