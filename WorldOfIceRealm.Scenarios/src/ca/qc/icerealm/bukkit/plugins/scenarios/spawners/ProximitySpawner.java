package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.SpawnerZoneActivator;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CoolDown;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CoolDownTimer;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ProximitySpawner implements TimeObserver, Listener, Spawner, CoolDown {

	private long _alarm;
	private long _interval;
	private int _prob;
	private int _maxMonster;
	private List<Entity> _entities;
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
	private boolean _isCoolDownActive;
	private CoolDownTimer _coolDownTimer;
	
	public ProximitySpawner(WorldZone z, InfestationConfiguration config, ZoneSubject zone) {
		_zone = z;
		_startingLocation = _zone.getRandomLocation(_zone.getWorld());
		_interval = config.IntervalBetweenSpawn;
		_prob = config.ProbabilityToSpawn;
		_entities = new ArrayList<Entity>();
		_burn = config.BurnDuringDaylight;
		_healthModifier = config.HealthModifier;
		_maxMonster = config.MaxMonstersPerSpawn;
		_config = config;
		_zoneServer = zone;
		_isCoolDownActive = false;
		
		if (config.UseInfestedZoneAsRadius) {
			_zoneActivator = _zone;
		}
		else {
			_zoneActivator = new WorldZone(_startingLocation, _config.SpawnerRadiusActivation);	
		}
		
		_activator = new SpawnerZoneActivator(_zoneActivator, config.Server, this);
		_zoneServer.addListener(_activator);
		_monstersToSpawn = _config.SpawnerMonsters.split(",");
		ScenarioPlugin.logger.fine("new spawner: " + _startingLocation.getX() + ","  + _startingLocation.getY() + "," + _startingLocation.getZ());
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
		
		if (draw && !_isCoolDownActive && _entities.size() < _maxMonster) {
			Location random = _zoneActivator.getRandomLocation(_zoneActivator.getWorld());
			random.setY(random.getY() + 2);
			EntityType creature = EntityUtilities.getEntityType(_monstersToSpawn[RandomUtil.getRandomInt(_monstersToSpawn.length)]);
			Entity l = ScenarioService.getInstance().spawnCreature(_zone.getWorld(), random, creature, _healthModifier);
			
			if (_config.RareDropMultiplier != 1.0) {
				ScenarioService.getInstance().attachRareDropMultiplierToEntity(l.getEntityId(), _config.RareDropMultiplier);
			}
			
			if (l instanceof Monster) {
				Monster m = (Monster)l;
				m.setTarget(_target);
			}
			_entities.add(l);
			ScenarioPlugin.logger.fine(creature.toString() + " - " + l.getEntityId()  + " from spawner " +_startingLocation.getX() + "," + _startingLocation.getY() + "," + _startingLocation.getZ());
		}

		
		if (_entities.size() == _maxMonster) {
			if (_config.DelayBeforeRespawn > 0) {
				_entities.clear();
				_coolDownTimer = new CoolDownTimer(this);
				TimeServer.getInstance().addListener(_coolDownTimer, _config.DelayBeforeRespawn);
				_isCoolDownActive = true;
				ScenarioPlugin.logger.fine("cool down timer started! at " + _startingLocation.getX() + ","  + _startingLocation.getY() + "," + _startingLocation.getZ());
			}
			else {
				moveSpawnerToAnotherLocation();
				_isCoolDownActive = false;
				ScenarioPlugin.logger.fine("cool down is not activated, delay respawn is 0");
			}
		}
		else {
			TimeServer.getInstance().addListener(this, _interval);
		}
	}
	
	public void removeListener() {
		if (_config.ResetWhenPlayerLeave) {
			for (Entity l : _entities) {
				l.remove();
			}
		}
		
		_entities.clear();
		TimeServer.getInstance().removeListener(this);
		TimeServer.getInstance().removeListener(_coolDownTimer);
		_zoneServer.removeListener(_activator);
	}
	
	public void moveSpawnerToAnotherLocation() {
		if (_config.DelayBeforeRespawn == 0) {
			_entities.clear();
			TimeServer.getInstance().removeListener(this);
			_zoneServer.removeListener(_activator);
			_startingLocation = _zone.getRandomLocation(_zone.getWorld());
			WorldZone activator = new WorldZone(_startingLocation, _config.SpawnerRadiusActivation);
			_zoneActivator = activator;
			
			if (_config.UseInfestedZoneAsRadius) {
				_zoneActivator = _zone;
			}
			
			_activator = new SpawnerZoneActivator(_zoneActivator, _config.Server, this);
			_zoneServer.addListener(_activator);
			ScenarioPlugin.logger.fine("moving spawner elsewhere! at " + _startingLocation.getX() + "," + _startingLocation.getZ());
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

	@Override
	public void setCoolDownActive(boolean v) {
		_isCoolDownActive = v;
		ScenarioPlugin.logger.fine("cool down changed to " + v + " at " + _startingLocation.getX() + "," + _startingLocation.getY() + _startingLocation.getZ());
		if (!_isCoolDownActive) {
			timeHasCome(System.currentTimeMillis());
		}
			
	}

	@Override
	public boolean isCoolDownActive() {
		return _isCoolDownActive;
	}

	@Override
	public Location getLocation() {
		return _startingLocation;
	}
	

}
