package ca.qc.icerealm.bukkit.plugins.scenarios.ambush;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;


public class AmbushExecutor implements Runnable {

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
	private String _monsters = "spider,skeleton,zombie,pigzombie,enderman";
	private String[] _monstersArray;
	private World _world;
	private Player _player = null;
	private int _radius = 32;
	private int _numberMonster = 0;
	
	public AmbushExecutor(int radius, Player player, int numberMonster) {
		_player = player;
		_monstersArray = _monsters.split(",");
		_world = player.getWorld();
		_radius = radius;
		_numberMonster = numberMonster;
	}
	
	@Override
	public void run() {
		
		_player.sendMessage(ChatColor.YELLOW + "The monster are getting closer...");
		
		WorldZone exclusion = new WorldZone(_player.getLocation(), 7.0);
		WorldZone area = new WorldZone(_player.getLocation(), _radius);
		
		for (int i = 0; i < _numberMonster; i++) {
			int maxTry = 0;
			Location newLoc = area.getRandomLocationOutsideThisZone(_player.getLocation().getWorld(), exclusion);
			
			while (maxTry < 3 && !validLocationForMonster(newLoc)) {
				newLoc = area.getRandomLocationOutsideThisZone(_player.getLocation().getWorld(), exclusion);
				maxTry++;
			}
			
			if (maxTry < 3) {
				EntityType creature = EntityUtilities.getEntityType(_monstersArray[RandomUtil.getRandomInt(_monstersArray.length)]);
				double health = ScenarioService.getInstance().calculateHealthModifierWithFrontier(newLoc, _world.getSpawnLocation());
				Monster m = (Monster)ScenarioService.getInstance().spawnCreature(_world, newLoc, creature, health, false);
				m.setTarget(_player);
			}	
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
