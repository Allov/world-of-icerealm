package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.EntityWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;

public class BasicMonsterWave implements EntityWave {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private int _nbMonsters = 0;
	private double _armorModifier = 0.0;
	private Set<Entity> _monstersTable;
	private MonsterFury _scenario;
	private WorldZone _exclude;
	private String[] possibleMonsters = new String[] { "zombie", "skeleton", "spider" };
	private ScenarioEventsListener _eventsListener;
	
	public BasicMonsterWave(int qty, double armorModifier, MonsterFury s, WorldZone greater, WorldZone exclude, ScenarioEventsListener listener) {
		_scenario = s;
		_monstersTable = new HashSet<Entity>();
		_nbMonsters = qty;
		_armorModifier = armorModifier;
		_exclude = exclude;
		_eventsListener = listener;
	}
	
	public int getNbOfEntities() {
		return _monstersTable.size();
	}
	
	public void spawnWave() {
		if (_scenario.isActive()) {
			for (int i = 0; i < _nbMonsters; i++) {
				// creation de la location et du monstre
				Location loc = _scenario.getWorldZone().getRandomLocationOutsideThisZone(_scenario.getWorld(), _exclude);
				CreatureType type = EntityUtilities.getCreatureType(possibleMonsters[RandomUtil.getRandomInt(possibleMonsters.length)]);			
				LivingEntity living = _scenario.getWorld().spawnCreature(loc, type);
				
				if (living instanceof Monster) {
					Monster m = (Monster)living;
					m.setTarget(_scenario.pickRandomPlayer());
				}
				
				// adding to the table
				_monstersTable.add(living);
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
	
	@Override
	public void setSpawnLocation(List<Location> l) {
		// les location sont g�n�r� lors de la creation des waves
	}

	
	public void cancelWave() {
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

	@Override
	public void processEntityDeath(Entity e) {
		if (_scenario.isActive()) {
			if (_monstersTable != null && _monstersTable.contains(e)) {
				_monstersTable.remove(e);
				
				if (_eventsListener != null) {
					_eventsListener.monsterDied(e, _monstersTable.size());
				}
				
				if (_monstersTable.size() == 0) {
					_scenario.waveIsDone();
				}
			}
		}
	}

	@Override
	public void setMonsters(String monsters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxNbOfEntities() {
		// TODO Auto-generated method stub
		return _nbMonsters;
	}

	@Override
	public String[] getMonsters() {
		return possibleMonsters;
	}
}
