package ca.qc.icerealm.bukkit.plugins.scenarios.obsidian;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ObsidianMission implements Listener {

	private Logger _logger = Logger.getLogger("Minecraft");
	private List<BreakBlockSession> _breaks;
	private ZoneSubject _zoneServer;
	private int _blocksLeft;
	private int[] _rewardObject;
	
	
	public ObsidianMission(ZoneSubject zone) {
		_zoneServer = zone;
	}
	
	public void setReward(int[] ids) {
		_rewardObject = ids;
	}
	
	public void giveReward(Location l, int qty) {
		
		int id = _rewardObject[RandomUtil.getRandomInt(_rewardObject.length)];
		l.getWorld().dropItemNaturally(l, new ItemStack(id, qty));
		_blocksLeft = _breaks.size();
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
		for (BreakBlockSession s : _breaks) {
			if (s.onBlockBroken(event)) {
				_blocksLeft--;
				
				if (_blocksLeft == 0) {
					giveReward(s.getBlockLocation(), 1);
					_logger.info("obsidian mission is over!");
				}
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
