package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;

public class MonsterSpawner implements Runnable {

	private Location _location;
	private World _world;
	
	public MonsterSpawner(Location loc) {
		_location = loc;
		_world = loc.getWorld();
	}
	
	@Override
	public void run() {
		
		double modifier = ScenarioService.getInstance().calculateHealthModifierWithFrontier(_location, _world.getSpawnLocation());
		ScenarioService.getInstance().spawnCreature(_world, _location, EntityType.GIANT, modifier, false);
	}
	
}
