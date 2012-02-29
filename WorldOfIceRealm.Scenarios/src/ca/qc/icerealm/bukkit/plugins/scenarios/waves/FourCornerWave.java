package ca.qc.icerealm.bukkit.plugins.scenarios.waves;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class FourCornerWave implements EntityWave, TimeObserver {

	private WorldZone _zone;
	private World _world;
	private List<LivingEntity> _monsters;
	private Scenario _scenario;
	private String[] possibleMonsters = new String[] { "zombie", "skeleton", "spider" };
	private int _maxMonster;
	private int _radius;
	private long _alarm;
	private long _delayBetweenSpawn;
	
	public FourCornerWave(WorldZone z, World w, Scenario s, int maxMonster, int radius, long delay) {
		_zone = z;
		_world = w;
		_scenario = s;
		_monsters = new ArrayList<LivingEntity>();
		_maxMonster = maxMonster;
		_radius = radius;
		_delayBetweenSpawn = delay;
	}
	
	@Override
	public void spawnWave() {
		
		if (_monsters.size() <= _maxMonster) {
			List<Location> corners = _zone.getFourCorner(_radius);
			for (int i = 0; i < corners.size(); i++) {
				CreatureType type = EntityUtilities.getCreatureType(possibleMonsters[RandomUtil.getRandomInt(possibleMonsters.length)]);		
				_monsters.add(_world.spawnCreature(corners.get(i), type));
			}
			
			// c'est une wave qui spawn des mosntres a chaque demi secon (500ms)
			TimeServer.getInstance().addListener(this, _delayBetweenSpawn);
		}
	}

	@Override
	public void processEntityDeath(Entity e) {
		// TODO Auto-generated method stub
		if (_scenario.isActive()) {
			if (e instanceof Monster) {
				_monsters.remove((LivingEntity)e);
			}
		}
		
		
	}

	@Override
	public void processDamage(EntityDamageEvent e) {
		if (_scenario.isActive()) {
			if (_monsters != null && _monsters.contains(e.getEntity())) {
				if (e.getCause() == DamageCause.FIRE_TICK) {
					e.setCancelled(true);
				}	
			}
		}
	}

	@Override
	public void cancelWave() {
		// TODO Auto-generated method stub
		if (_scenario.isActive()) {
			for (LivingEntity e : _monsters) {
				e.remove();
			}
		}
		
		TimeServer.getInstance().removeListener(this);
	}
	
	@Override
	public void setSpawnLocation(List<Location> l) {
		// les location sont généré lors de la creation des waves
	}

	@Override
	public int getNbOfEntities() {
		return _monsters.size();
	}


	@Override
	public void timeHasCome(long time) {
		this.spawnWave();
	}


	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}


	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}

	@Override
	public void setMonsters(String monsters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxNbOfEntities() {
		// TODO Auto-generated method stub
		return _maxMonster;
	}

	@Override
	public String[] getMonsters() {
		return possibleMonsters;
	}
}
