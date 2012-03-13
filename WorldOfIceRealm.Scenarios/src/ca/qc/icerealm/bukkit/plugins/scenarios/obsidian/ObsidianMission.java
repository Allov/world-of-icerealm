package ca.qc.icerealm.bukkit.plugins.scenarios.obsidian;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ObsidianMission {

	private Logger _logger = Logger.getLogger("Minecraft");
	private List<BreakBlockSession> _breaks;
	private ZoneSubject _zoneServer;

	
	
	public ObsidianMission(ZoneSubject zone) {
		_zoneServer = zone;

	}

	
	public void setBreakBlockSession(List<BreakBlockSession> s) {
		_breaks = s;
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
			s.onBlockBroken(event);
		}
		/*
		Material m = _blockToBreak.get(event.getBlock().getLocation());
		if (m  != null) {
			_logger.info("you did break a block at: " + event.getBlock().getLocation());
			_blockToBreak.remove(event.getBlock().getLocation());
			_logger.info(_blockToBreak.size() + " left to break!");
		}
		*/
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamaged(EntityDamageEvent event) {

		for (BreakBlockSession s : _breaks) {
			s.onMonsterDamaged(event);
		}
		
		/*
		Monster monster = null;
		for (Monster m : _spawnedMonsters) {
			if (m.getEntityId() == event.getEntity().getEntityId()) {
				_logger.info("monster: " + m.getEntityId() + " received: " + event.getDamage() + " damage");
			}
		}
		*/
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDamaged(BlockDamageEvent event) {

		for (BreakBlockSession s : _breaks) {
			s.onBlockDamage(event);
		}
		
		/*
		Monster monster = null;
		for (Monster m : _spawnedMonsters) {
			if (m.getEntityId() == event.getEntity().getEntityId()) {
				_logger.info("monster: " + m.getEntityId() + " received: " + event.getDamage() + " damage");
			}
		}
		*/
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDied(EntityDeathEvent event) {
		for (BreakBlockSession s : _breaks) {
			s.onMonsterDies((Monster)event.getEntity());
		}
	}

}
