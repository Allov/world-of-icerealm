package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.logging.Logger;

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
		
		
		
		
		getServer().broadcastMessage("Blood Moon is rising!!!!");
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
		if (WorldClock.getHour(getWorld()) < 24 && WorldClock.getHour(getWorld()) > 12) {
			return true;
		}
		return false;
	}

	@Override
	public boolean mustBeStop() {
		return !canBeTriggered();
	}

	@Override
	public void terminateScenario() {
		// TODO Auto-generated method stub
		getServer().broadcastMessage("The light is back!!!!");
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
	
	public Location getRandomLocationAround(Location l, double min, double max) {
		Location newLoc = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
		double offsetX = 10.0;//RandomUtil.getRandomDouble(15.0, 25.0);
		double offsetZ = 10.0;//RandomUtil.getRandomDouble(15.0, 25.0);
		
		//if (RandomUtil.getDrawResult(1)) { offsetX = offsetX * -1.0; }
		//if (RandomUtil.getDrawResult(1)) { offsetZ = offsetZ * -1.0; }
		
		this.logger.info("offsetX: " + offsetX + " offsetZ: " + offsetZ);
		newLoc.setX(newLoc.getX() + offsetX);
		newLoc.setZ(newLoc.getZ() + offsetZ);
		
		double height = getWorld().getHighestBlockYAt(newLoc);
		newLoc.setY(height);
		
		this.logger.info("loc: " + newLoc.getX() + "," + newLoc.getY() + "," + newLoc.getZ());
		
		return newLoc;
	}
	
	public void spawnMonsterCloseToPlayer(Location l) {
		String[] monsters = new String[] { "zombie", "skeleton", "spider" };
		Location newLoc = getRandomLocationAround(l, 15.0, 25.0);
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
