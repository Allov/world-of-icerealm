package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;

public class MonsterSpawner implements Runnable {

	private Location _location;
	private World _world;
	private String _name;
	private String[] _monsters = new String[] { "skeleton", "zombie", "spider", "cavespider", "pigzombie" };
	private boolean _done = false;
	private List<LivingEntity> _entity;
	
	public MonsterSpawner(Location loc, String name, List<LivingEntity> entity) {
		_location = loc;
		_world = loc.getWorld();
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
			List<Entity> nearby = entity.getNearbyEntities(30, 30, 30);
			List<Player> players = new ArrayList<Player>();
			
			for (Entity e : nearby) {
				if (e instanceof Player) {
					players.add((Player)e);
				}
			}
			
			if (players.size() > 0) {
				if (entity instanceof Monster) {
					Monster m = (Monster)entity;
					Collections.shuffle(players);
					m.setTarget(players.get(0));
				}	
			}*/
		}
	}
	
	public String getName() {
		return _name;
	}
	
}
