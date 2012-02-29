package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.SpawnerZoneActivator;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class NormalSpawner implements TimeObserver, Listener, Spawner {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm;
	private long _interval;
	private int _prob;
	private int _maxMonster;
	private List<LivingEntity> _entities;
	private boolean _burn;
	private double _healthModifier;
	private WorldZone _zone;
	private Location _startingLocation;
	private SpawnerZoneActivator _activator;
	private InfestationConfiguration _config;
	private Player _target;
	private WorldZone _zoneActivator;
	private boolean _isPlayerAround;
	private String[] _monstersToSpawn;
	private ZoneSubject _zoneServer;

	
	
	public NormalSpawner(WorldZone z, InfestationConfiguration config, ZoneSubject zone) {
		_zone = z;
		_startingLocation = _zone.getRandomHighestLocation(_zone.getWorld());
		_interval = config.IntervalBetweenSpawn;
		_prob = config.ProbabilityToSpawn;
		_entities = new ArrayList<LivingEntity>();
		_burn = config.BurnDuringDaylight;
		_healthModifier = config.HealthModifier;
		_maxMonster = config.MaxMonstersPerSpawn;
		_config = config;
		_zoneServer = zone;
		if (config.UseInfestedZoneAsRadius) {
			_zoneActivator = _zone;
		}
		else {
			_zoneActivator = new WorldZone(_startingLocation, _config.SpawnerRadiusActivation);	
		}
		
		_activator = new SpawnerZoneActivator(_zoneActivator, config.Server, this, _config);
		_zoneServer.addListener(_activator);
		_monstersToSpawn = _config.SpawnerMonsters.split(",");
	}
	
	public void setTarget(Player p) {
		_target = p;
	}

	public void setPlayerAround(boolean a) {
		_isPlayerAround = a;
	}
	
	@Override
	public void timeHasCome(long time) {
		boolean draw = RandomUtil.getDrawResult(_prob);	
		
		if (draw && _entities.size() < _maxMonster && _isPlayerAround) {
			Location random = _zoneActivator.getRandomLocation(_zoneActivator.getWorld());
			random.setY(random.getY() + 2);
			CreatureType creature = EntityUtilities.getCreatureType(_monstersToSpawn[RandomUtil.getRandomInt(_monstersToSpawn.length)]);
			LivingEntity l = ScenarioService.getInstance().spawnCreature(_zone.getWorld(), random, creature, _healthModifier, _burn);
			if (l instanceof Monster) {
				Monster m = (Monster)l;
				m.setTarget(_target);
			}
			_entities.add(l);
			//this.logger.info(creature.toString() + " at " + l.getLocation().getX() + "," + l.getLocation().getY() + "," + l.getLocation().getZ());
		}

		
		if (_entities.size() == _maxMonster) {
			_entities.clear();
			if (_config.DelayBeforeRespawn > 0) {
				_isPlayerAround = false;
				TimeServer.getInstance().addListener(this, _config.DelayBeforeRespawn);
			}
			
		
		}
		else {
			TimeServer.getInstance().addListener(this, _interval);
		}
	}
	
	public void removeListener() {
		TimeServer.getInstance().removeListener(this);
		_zoneServer.removeListener(_activator);
	}
	
	public void resetLocation() {
		if (_config.DelayBeforeRespawn == 0) {
			_entities.clear();
			TimeServer.getInstance().removeListener(this);
			_zoneServer.removeListener(_activator);
			_startingLocation = _zone.getRandomLocation(_zone.getWorld());
			WorldZone activator = new WorldZone(_startingLocation, _config.SpawnerRadiusActivation);
			_zoneActivator = activator;
			_activator = new SpawnerZoneActivator(_zoneActivator, _config.Server, this, _config);
			_zoneServer.addListener(_activator);
		}
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}
	
	

}
