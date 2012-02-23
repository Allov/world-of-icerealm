package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class Spawner implements TimeObserver, Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm;
	private Location _location;
	private CreatureType _type;
	private long _interval;
	private int _prob;
	private int _maxMonster = 20;
	private int _maxHealth;
	private List<LivingEntity> _entities;
	
	public Spawner(Location l, long interval, CreatureType t, int prob, int maxHealth) {
		_location = l;
		_interval = interval;
		_prob = prob;
		_type = t;
		_entities = new ArrayList<LivingEntity>();
		_maxHealth = maxHealth;
		TimeServer.getInstance().addListener(this, _interval);
	}
	
	
	
	@Override
	public void timeHasCome(long time) {
		boolean draw = RandomUtil.getDrawResult(_prob);		
		if (draw && _entities.size() < _maxMonster) {
			_entities.add(ScenarioService.getInstance().spawnCreature(_location.getWorld(), _location, _type, _maxHealth, false));
		}
		TimeServer.getInstance().addListener(this, _interval);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void monsterDie(EntityDeathEvent event) {
		for (LivingEntity e : _entities) {
			if (e.getEntityId() == event.getEntity().getEntityId()) {
				_entities.remove(e);
				break;
			}
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
