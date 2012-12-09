package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;

public class MonsterSpawner implements Runnable {

	private Logger _logger = Logger.getLogger("Minecraft");
	private Location _location;
	private World _world;
	private String _name;
	private String[] _monsters = new String[] { "skeleton", "zombie", "spider", "cavespider", "pigzombie" };
	private boolean _done = false;
	private List<LivingEntity> _entity;
	private long _coolDownInHours = 2;
	private SpawnerActivator _activator;
	private ScheduledExecutorService _schedule;
	
	public MonsterSpawner(Location loc, String name, List<LivingEntity> entity) {
		_world = loc.getWorld();
		_location = loc;
		
		Block b = _world.getBlockAt(_location);
		while (b.getType() != Material.AIR) {
			_location = new Location(_world, _location.getX(), _location.getY() + 1, _location.getZ());
			b = _world.getBlockAt(_location);
		}
		
		_name = name;
		_entity = entity;		
	}
	
	@Override
	public void run() {
		if (!_done) {
			_done = true;
			double modifier = ScenarioService.getInstance().calculateHealthModifierWithFrontier(_location, _world.getSpawnLocation());
			EntityType creature = EntityUtilities.getEntityType(_monsters[RandomUtil.getRandomInt(_monsters.length)]);
			LivingEntity entity = (LivingEntity)ScenarioService.getInstance().spawnCreature(_world, _location, creature, modifier, false);
			_entity.add(entity);
			
			/*
			_activator = new SpawnerActivator(this);
			_schedule = Executors.newSingleThreadScheduledExecutor();
			//_schedule.schedule(_activator, _coolDownInHours, TimeUnit.HOURS);
			_schedule.schedule(_activator, 2, TimeUnit.MINUTES);
			*/
		}
	}
	
	public void setActivate(boolean b) {
		_done = b;
		if (!_done) { // c'est un reset
			//_logger.info("monsterspawner setActvivate : " + b);
			//_schedule.shutdown();
		}
	}
	
	public String getName() {
		return _name;
	}
	
}

class SpawnerActivator implements Runnable {

	private MonsterSpawner _spawner;
	
	public SpawnerActivator(MonsterSpawner spawner) {
		_spawner = spawner;
	}
	
	@Override
	public void run() {
		_spawner.setActivate(false);
	}
	
}
