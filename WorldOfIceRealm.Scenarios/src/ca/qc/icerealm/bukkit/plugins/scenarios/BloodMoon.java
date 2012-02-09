package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.SpawnChangeEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldClock;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class BloodMoon extends Scenario {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _active;
	private MonsterSpawnListener listener;	
	
	@Override
	public boolean isTriggered() {
		return _active;
	}

	@Override
	public void triggerScenario() {
		// TODO Auto-generated method stub
		for (Player p : getServer().getOnlinePlayers()) {
			spawnMonsterCloseToPlayer(p.getLocation());
			spawnMonsterCloseToPlayer(p.getLocation());
			
		}
		
		if (listener == null) {
			listener = new MonsterSpawnListener(this);
			getServer().getPluginManager().registerEvents(listener, getPlugin());
		}
		
		
		
		
		getServer().broadcastMessage(ChatColor.RED + "THE BLOOD MOON IS RISING");
		getServer().broadcastMessage(ChatColor.GREEN + "GET READY FOR A LOT OF MONSTERS");
		_active = true;
		
	}

	@Override
	public void abortScenario() {
		getPlayers().clear();
		getServer().broadcastMessage("BloodMoon has been cancelled");
		
	}

	@Override
	public boolean abortWhenLeaving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeTriggered() {
		/*if (WorldClock.getHour(getWorld()) < 24 && WorldClock.getHour(getWorld()) > 12) {
			
			
		}*/
		return false;
	}

	@Override
	public boolean mustBeStop() {
		return !canBeTriggered();
	}

	@Override
	public void terminateScenario() {
		// TODO Auto-generated method stub
		getServer().broadcastMessage(ChatColor.GREEN + "The light is back!");
		_active = false;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void progressHandler() {
		// TODO Auto-generated method stub
		
	}

	
	public void spawnMonsterCloseToPlayer(Location l) {
		String[] monsters = new String[] { "zombie", "skeleton", "spider" };
		WorldZone exclusion = new WorldZone(l, 10.0);
		WorldZone area = new WorldZone(l, 20.0);
		Location newLoc = area.getRandomLocationOutsideThisZone(l.getWorld(), exclusion);
		
		CreatureType type = EntityUtilities.getCreatureType(monsters[RandomUtil.getRandomInt(monsters.length)]);
		getWorld().spawnCreature(newLoc, type);
	}
}

class MonsterSpawnListener implements Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private BloodMoon _moon;
	private boolean _avoid = false;
	
	public MonsterSpawnListener(BloodMoon moon) {
		_moon = moon;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterSpawn(CreatureSpawnEvent event) {
		if (_moon.isTriggered() && !_avoid) {
			_avoid = true;
			_moon.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
			_avoid = false;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDeath(EntityDeathEvent event) {
		if (_moon.isTriggered()) {
			_moon.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
		}
		
	}
}
