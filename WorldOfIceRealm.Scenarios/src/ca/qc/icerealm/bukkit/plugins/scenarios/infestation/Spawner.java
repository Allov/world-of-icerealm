package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class Spawner implements TimeObserver {

	private long _alarm;
	private Location _location;
	private CreatureType _type;
	private long _interval;
	private int _prob;
	private int _maxMonster = 20;
	private List<LivingEntity> _entities;
	
	public Spawner(Location l, long interval, CreatureType t, int prob) {
		_location = l;
		_interval = interval;
		_prob = prob;
		_type = t;
		_entities = new ArrayList<LivingEntity>();
		TimeServer.getInstance().addListener(this, _interval);
	}
	
	
	
	@Override
	public void timeHasCome(long time) {
		boolean draw = RandomUtil.getDrawResult(_prob);		
		if (draw && _entities.size() > _maxMonster) {
			ScenarioService.getInstance().spawnCreature(_location.getWorld(), _location, _type, false);
		}
		TimeServer.getInstance().addListener(this, _interval);
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
