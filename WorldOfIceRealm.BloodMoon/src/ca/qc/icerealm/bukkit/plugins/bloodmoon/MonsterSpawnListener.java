package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;

public class MonsterSpawnListener implements Listener {

	private final Logger logger = Logger.getLogger(("Minecraft"));
	private BloodMoon _moon;
	private boolean _avoid = false;
	
	public MonsterSpawnListener(BloodMoon moon) {
		_moon = moon;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterSpawn(CreatureSpawnEvent event) {
		boolean draw = RandomUtil.getDrawResult(2);
		if (_moon.isActive() && !_avoid && draw && event.getEntity() instanceof Monster) {
			_avoid = true;
			_moon.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
			_avoid = false;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDeath(EntityDeathEvent event) {
		if (_moon.isActive() && event.getEntity() instanceof Monster) {
			_moon.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoined(PlayerJoinEvent event) {
		if (_moon.isActive()) {
			event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "This is a " + ChatColor.RED + " BLOOD MOON " + ChatColor.DARK_AQUA + "right now! Be courageous son");
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSleep(PlayerBedEnterEvent event) {
		if (_moon.isActive()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("You cannot sleep during a " + ChatColor.DARK_RED + " BLOOD MOON!");
		}
		
	}	
}
