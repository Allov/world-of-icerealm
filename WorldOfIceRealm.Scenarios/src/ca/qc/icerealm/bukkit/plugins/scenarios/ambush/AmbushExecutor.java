package ca.qc.icerealm.bukkit.plugins.scenarios.ambush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import net.minecraft.server.EntityFireball;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;


public class AmbushExecutor implements Runnable {

	public final Logger _logger = Logger.getLogger(("Minecraft"));
	
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
	private String _monsters;
	private PotionEffectType[] _potions = { PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.SPEED };
	private String[] _monstersArray;
	private World _world;
	private Player _player = null;
	private int _radius = 32;
	private int _numberMonster = 0;
	
	public AmbushExecutor(int radius, Player player, int numberMonster, String monsters) {
		_player = player;
		_monsters = monsters;
		_monstersArray = _monsters.split(",");
		_world = player.getWorld();
		_radius = radius;
		_numberMonster = numberMonster;
	}
	
	private List<PotionEffect> getRandomPotions(double modifier) {
		
		List<PotionEffect> potions = new ArrayList<PotionEffect>();
		try {
			PotionEffectType potion = _potions[RandomUtil.getRandomInt(_potions.length + 1)];
			potions.add(new PotionEffect(potion, 600000, (int)modifier));
		}
		catch (Exception ex) {
			// rien a faire
		}
	
		return potions;
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
				double health = Frontier.getInstance().calculateGlobalModifier(newLoc);
				LivingEntity living = (LivingEntity)ScenarioService.getInstance().spawnCreature(_world, newLoc, creature, health, false);
							
				// set le monster avec le target et les potions
				List<PotionEffect> effects = this.getRandomPotions(health);
				Monster m = (Monster)living;
				m.addPotionEffects(effects);
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
