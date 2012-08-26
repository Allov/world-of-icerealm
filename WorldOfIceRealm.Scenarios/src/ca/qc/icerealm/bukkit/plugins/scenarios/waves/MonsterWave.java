package ca.qc.icerealm.bukkit.plugins.scenarios.waves;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;

public class MonsterWave {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private int _nbMonsters = 0;
	private double _armorModifier = 0.0;
	private Set<Entity> _monstersTable;
	private MonsterFury _scenario;
	private WorldZone _exclude;
	private String[] possibleMonsters = new String[] { "zombie", "skeleton", "spider" };
	
	public MonsterWave(int qty, double armorModifier, MonsterFury s, WorldZone greater, WorldZone exclude) {
		_scenario = s;
		_monstersTable = new HashSet<Entity>();
		_nbMonsters = qty;
		_armorModifier = armorModifier;
		_exclude = exclude;
	}
	
	public int getMonstersSize() {
		return _monstersTable.size();
	}
	
	public void spawnWave() {
		if (_scenario.isActive()) {
			for (int i = 0; i < _nbMonsters; i++) {
				// creation de la location et du monstre
				Location loc = _scenario.getWorldZone().getRandomLocationOutsideThisZone(_scenario.getWorld(), _exclude);
				EntityType type = EntityUtilities.getEntityType(possibleMonsters[RandomUtil.getRandomInt(possibleMonsters.length)]);			
				Entity living = _scenario.getWorld().spawnEntity(loc, type);
				// adding to the table
				_monstersTable.add(living);
			}
		}
		
		
	}
	
	public void processEntityDeath(Entity e, ScenarioEventsListener l) {
		if (_scenario.isActive()) {
			if (_monstersTable != null && _monstersTable.contains(e)) {
				_monstersTable.remove(e);
				
				if (l != null) {
					l.monsterDied(e, _monstersTable.size());
				}
				
				if (_monstersTable.size() == 0) {
					_scenario.waveIsDone();
				}
			}
		}
		
	}
	
	public void processDamage(EntityDamageEvent e) {
		if (_scenario.isActive()) {
			if (_monstersTable != null && _monstersTable.contains(e.getEntity())) {
				
				if (e.getCause() == DamageCause.FIRE_TICK) {
					e.setCancelled(true);
				}
							
			}
		}
		
	}
	
	public void removeMonsters() {
		if (_monstersTable != null && _monstersTable.size() > 0) {
			 for (Entity l : _monstersTable) {
				 l.remove();
			 }
		}
	}
	
	public List<Entity> getFirstMonster(int i) {
		List<Entity> list = new ArrayList<Entity>();
		for (Entity l : _monstersTable) {
			list.add(l);
		}
		return list;
	}
}
