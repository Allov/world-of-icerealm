package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class InfiniteSpawner implements Spawner, TimeObserver {

	private Location _location;
	private double _health;
	private boolean _burn;
	private double _damageModifier;
	private long _interval;
	private long _alarm;
	private World _world;
	private String[] _arrayMonsters;
	private List<Player> _possibleTarget;
	
	public InfiniteSpawner(Location l, String monsters, double health, double damage, boolean burn, long interval) {
		_location = l;
		_health = health;
		_burn = burn;
		_interval = interval;
		_world = l.getWorld();
		setMonsters(monsters);
	}
	
	public void setMonsters(String monsters) {
		_arrayMonsters = monsters.split(",");
	}
	
	public void setPossibleTargets(List<Player> l) {
		_possibleTarget = l;
	}
	
	public void activateSpawning() {
		timeHasCome(System.currentTimeMillis());
	}
	
	public void spawnMonster() {
		CreatureType c = getRandomMonsters();
		LivingEntity l = ScenarioService.getInstance().spawnCreature(_world, _location, c, _health);
		if (l instanceof Monster) {
			setRandomPlayerAsTarget((Monster)l);
		}
	}
	
	public void stopSpawning() {
		removeListener();
	}
	
	@Override
	public void removeListener() {
		TimeServer.getInstance().removeListener(this);
	}

	@Override
	public Location getLocation() {
		return _location;
	}

	@Override
	public void timeHasCome(long time) {
		spawnMonster();
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

	private CreatureType getRandomMonsters() {
		return EntityUtilities.getCreatureType(_arrayMonsters[RandomUtil.getRandomInt(_arrayMonsters.length)]);
	}
	
	private void setRandomPlayerAsTarget(Monster m) {
		if (_possibleTarget != null) {
			m.setTarget(_possibleTarget.get(RandomUtil.getRandomInt(_possibleTarget.size())));
		}
	}
}


