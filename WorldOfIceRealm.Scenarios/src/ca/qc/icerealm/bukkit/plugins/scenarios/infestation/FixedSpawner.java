package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class FixedSpawner implements TimeObserver {

	private final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm;
	private Location _spawningLocation;
	private String[] _monsters;
	private long _delayBetweenMonster;
	private List<Monster> _monsterSpawned;
	private int _maxMonster;
	private double _healthModifier;
	private boolean _burn;
	private List<Player> _players;
	private WorldZone _spawningZone;
	
	
	public FixedSpawner(Location l, InfestationConfiguration config, List<Player> players) {
		_spawningLocation = l;
		_monsters = config.SpawnerMonsters.split(",");
		_delayBetweenMonster = config.IntervalBetweenSpawn;
		_monsterSpawned = new ArrayList<Monster>();
		_maxMonster = config.MaxMonstersPerSpawn;		
		_healthModifier = config.HealthModifier;
		_burn = config.BurnDuringDaylight;
		_players = players;
		_spawningZone = new WorldZone(_spawningLocation, 3);
	}
	
	@Override
	public void timeHasCome(long time) {
		if (_monsterSpawned.size() < _maxMonster) {
			Location random = _spawningZone.getRandomLocation(_spawningLocation.getWorld());
			CreatureType creature = EntityUtilities.getCreatureType(_monsters[RandomUtil.getRandomInt(_monsters.length)]);
			Monster m = (Monster)ScenarioService.getInstance().spawnCreature(_spawningLocation.getWorld(), random, creature, _healthModifier, _burn);
			m.setTarget(_players.get(RandomUtil.getRandomInt(_players.size())));
			
			this.logger.info("Spawner at: " + _spawningLocation.getX() + "," + _spawningLocation.getZ() + " high: " + _spawningLocation.getY() + " - " + creature.toString() + " -> " + m.getTarget().getEntityId());
			
			_monsterSpawned.add(m);
			TimeServer.getInstance().addListener(this, _delayBetweenMonster);
		}
		
		
	}
	
	public void clearRemainingMonsters() {
		for (Monster m : _monsterSpawned) {
			m.remove();
		}
		
		_monsterSpawned.clear();
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
