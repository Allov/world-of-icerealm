package ca.qc.icerealm.bukkit.plugins.scenarios.waves;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class RegularSpawnWave implements EntityWave, TimeObserver {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _timeBetweenSpawn;
	private long _alarm;
	private List<Location> _locations;
	private List<Entity> _monsters;
	private String _typeMonsters;
	private String[] _arrayPossibleMonster;
	private Scenario _scenario;
	private int _maxMonster;
	private double _healthModifier;
	
	public RegularSpawnWave(long timeBetweenSpawn, Scenario s, int maxMonster, double healthmodifier) {
		_timeBetweenSpawn = timeBetweenSpawn;
		_scenario = s;
		_maxMonster = maxMonster;
		_monsters = new ArrayList<Entity>();
		_healthModifier = healthmodifier;
	}
	
	
	@Override
	public void timeHasCome(long time) {
		spawnWave();
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}

	@Override
	public void spawnWave() {
		if (_scenario.isActive() && _locations != null && _locations.size() > 0 && 
			_arrayPossibleMonster.length > 0 && getNbOfEntities() < _maxMonster) {
			
			for (Location l : _locations) {
				
				if (getNbOfEntities() < _maxMonster) {
					EntityType c = EntityUtilities.getEntityType(_arrayPossibleMonster[RandomUtil.getRandomInt(_arrayPossibleMonster.length)]);
					Entity e = ScenarioService.getInstance().spawnCreature(l.getWorld(), l, c, _healthModifier, false);
					_monsters.add(e);
					if (e instanceof Monster) {
						Monster m = (Monster)e;
						m.setTarget(_scenario.pickRandomPlayer());
					}
				}
			}
			
			if (_timeBetweenSpawn > 50) {
				TimeServer.getInstance().addListener(this, _timeBetweenSpawn);
			}
			
		}
		
	}

	@Override
	public void processEntityDeath(Entity e) {
		if (_scenario.isActive()) {
			if (e instanceof Monster) {
				_monsters.remove((LivingEntity)e);
				
				if (_scenario.getEventListener() != null) {
					_scenario.getEventListener().monsterDied(e, _monsters.size());
				}
			}
			
			if (_monsters.size() == 0) {
				TimeServer.getInstance().removeListener(this);
				_scenario.waveIsDone();
			}
		}
				
		
	}

	@Override
		public void processDamage(EntityDamageEvent e) {
		/*
		if (_scenario.isActive() &&_monsters != null && _monsters.contains(e.getEntity())) {
			if (e.getCause() == DamageCause.FIRE_TICK) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
			}						
		}
		*/
	}

	@Override
	public void cancelWave() {
		for (Entity l : _monsters) {
			l.remove();
		}
		
		_monsters.clear();
		
		TimeServer.getInstance().removeListener(this);
		
	}

	@Override
	public int getNbOfEntities() {
		return _monsters.size();
	}

	@Override
	public void setSpawnLocation(List<Location> l) {
		_locations = l;
	}

	@Override
	public void setMonsters(String monsters) {
		_typeMonsters = monsters;
		_arrayPossibleMonster = _typeMonsters.split(",");
	}


	@Override
	public int getMaxNbOfEntities() {
		return _maxMonster;
	}


	@Override
	public String[] getMonsters() {
		return _arrayPossibleMonster;
	}
	
	@Override
	public void setNbMonster(int nb) {
		_maxMonster = nb;
	}

}
