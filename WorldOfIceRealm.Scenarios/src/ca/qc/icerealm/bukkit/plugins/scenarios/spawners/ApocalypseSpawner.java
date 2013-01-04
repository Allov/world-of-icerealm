package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;

public class ApocalypseSpawner implements Runnable {

	// le id des blocks valides
	// 79 = snow
	// 2 = grass
	// 12 = sand
	// 3 = dirt
	// 18 = leaf
	// 31 = tall grass
	// 13 = gravel
	// 80 = snow block
	// 82 = clay block
	// 110 = mycelinum
	// 111 = lilly pad
	private final int[] _validBlockRaw = new int[] { 79, 2, 12, 3, 18, 31, 13, 80, 82, 110, 111 };
	private Player[] _players = Bukkit.getOnlinePlayers();
	private Random _random;
	
	@Override
	public void run() {
		_random = new Random();
		for (Player p : _players) {
			if (p.getLocation().getY() > p.getLocation().getWorld().getSeaLevel()) {
				spawnMonsterCloseToPlayer(p.getLocation());
				if (_random.nextBoolean()) {
					spawnMonsterCloseToPlayer(p.getLocation());
				}
			}
		}
	}
	
	public void spawnMonsterCloseToPlayer(Location l) {
		double modifier = Frontier.getInstance().calculateGlobalModifier(l);
		String[] monsters = new String[] { "zombie", "skeleton", "spider" };
		WorldZone exclusion = new WorldZone(l, 7.0);
		WorldZone area = new WorldZone(l, 15.0);
		int maxTry = 0;
		Location newLoc = area.getRandomLocationOutsideThisZone(l.getWorld(), exclusion);
		while (maxTry < 3 && !validLocationForMonster(newLoc)) {
			newLoc = area.getRandomLocationOutsideThisZone(l.getWorld(), exclusion);
			maxTry++;
		}
		
		if (maxTry < 3) {
			EntityType creature = EntityUtilities.getEntityType(monsters[RandomUtil.getRandomInt(monsters.length)]);
			ScenarioService.getInstance().spawnCreature(l.getWorld(), newLoc, creature, modifier, false);
		}	
	}
	
	private boolean validLocationForMonster(Location l) {
		Location under = new Location(l.getWorld(), l.getX(), l.getY() - 1.0, l.getZ());
		org.bukkit.block.Block b = under.getBlock();
		for (int id : _validBlockRaw) {
			if (b.getTypeId() == id) {
				return true;
			}
		}
		return false;
	}
	

	
} 
