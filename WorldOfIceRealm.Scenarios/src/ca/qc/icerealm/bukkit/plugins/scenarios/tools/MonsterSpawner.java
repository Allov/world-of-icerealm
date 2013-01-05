package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.List;
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
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;

public class MonsterSpawner implements Runnable {

	private Logger _logger = Logger.getLogger("Minecraft");
	private Location _location;
	private World _world;
	private String _name;
	private String[] _monsters = new String[] { "skeleton", "zombie", "spider", "cavespider", "pigzombie" };
	private boolean _canSpawn = true;
	private List<LivingEntity> _entity;
	
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
		if (_canSpawn) {
			_canSpawn = false;
			double modifier = Frontier.getInstance().calculateGlobalModifier(_location);
			EntityType creature = EntityUtilities.getEntityType(_monsters[RandomUtil.getRandomInt(_monsters.length)]);
			LivingEntity entity = (LivingEntity)ScenarioService.getInstance().spawnCreature(_world, _location, creature, modifier, false);
			_entity.add(entity);
		}
	}
	
	public void setActivate(boolean b) {
		_canSpawn = b;
	}
	
	public String getName() {
		return _name;
	}
}
