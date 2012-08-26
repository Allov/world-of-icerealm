package ca.qc.icerealm.bukkit.plugins.scenarios.obsidian;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ObsidianMission implements Listener {

	private Logger _logger = Logger.getLogger("Minecraft");
	private List<BreakBlockSession> _breaks;
	private ZoneSubject _zoneServer;
	private int _blocksLeft;
	private int[] _rewardObject;
	private int[] _enchantmentPossible;
	private long _coolDownTime;
	
	
	public ObsidianMission(ZoneSubject zone) {
		_zoneServer = zone;
		_coolDownTime = 0;
	}
	
	public void setReward(int[] ids) {
		_rewardObject = ids;
	}
	
	public void setPossibleEnchantment(int[] id) {
		_enchantmentPossible = id;
	}
	
	public Item giveReward(Location l, int qty) {
		
		int id = _rewardObject[RandomUtil.getRandomInt(_rewardObject.length)];
		Item i = l.getWorld().dropItemNaturally(l, new ItemStack(id, qty));
		return i;
	}
	
	public void setCooldownTime(long time) {
		_coolDownTime = time;
	}

	
	public void setBreakBlockSession(List<BreakBlockSession> s) {
		_breaks = s;
		_blocksLeft = _breaks.size();
	}
	
	public void activateBlockZone()  {
		for (BreakBlockSession s : _breaks) {
			_zoneServer.addListener(s);
		}
	}
	
	public void removeBlockZone() {
		for (BreakBlockSession s : _breaks) {
			_zoneServer.removeListener(s);
			World w = s.getBlockLocation().getWorld();
			Block b = w.getBlockAt(s.getBlockLocation());
			b.setType(Material.AIR);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		boolean mustReinitialise = false;
		for (BreakBlockSession s : _breaks) {
			if (s.onBlockBroken(event)) {
				_blocksLeft--;
				s.setCoolDownActive(true);
				if (_blocksLeft == 0) {
					Item i = giveReward(s.getBlockLocation(), 1);
					notifyPlayersAround(event.getPlayer(), 20, "A " + i.toString() + " has been dropped for reward");
					_logger.info("obsidian mission is over!");
					_blocksLeft = _breaks.size();
					mustReinitialise = true;
				}
			}
			
			
		}
		
		if (mustReinitialise) {
			for (BreakBlockSession s : _breaks) {
				s.resetToInitialState();
				if (_coolDownTime > 0) {
					s.setCooldownTime(_coolDownTime);
					s.activateCooldownTimer();
				}
			}
		}
	}
	
	private void notifyPlayersAround(Player p, double radius, String msg) {
		
		List<org.bukkit.entity.Entity> entities = p.getNearbyEntities(radius, radius, radius);
		for (org.bukkit.entity.Entity e : entities) {
			if (e instanceof Player) {
				((Player)e).sendMessage(msg);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamaged(EntityDamageEvent event) {

		
		for (BreakBlockSession s : _breaks) {
			s.onMonsterDamaged(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDamaged(BlockDamageEvent event) {

		for (BreakBlockSession s : _breaks) {
			s.onBlockDamage(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDied(EntityDeathEvent event) {
		for (BreakBlockSession s : _breaks) {
			s.onMonsterDies((Monster)event.getEntity());
		}
	}

}
