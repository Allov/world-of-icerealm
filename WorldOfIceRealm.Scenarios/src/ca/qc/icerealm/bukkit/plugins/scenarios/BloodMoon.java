package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.logging.Logger;

import net.minecraft.server.Block;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
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
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class BloodMoon extends Scenario implements TimeObserver {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _active;
	private MonsterSpawnListener listener;	
	private long coolDown = 0;
	private long started = 0; 
	private int lastTimeChecked = -1;
	private int probability = 1;
	private long _alarm = 0;

	// 2 = grass
	// 12 = sand
	// 3 = dirt
	// 18 = leaf
	// 31 = tall grass
	// 13 = gravel
	private int[] validBlockRaw = new int[] { 2, 12, 3, 18, 31, 13 };
	
	@Override
	public boolean isTriggered() {
		return _active;
	}

	@Override
	public void triggerScenario() {
		// envoi un message sur le serveur pour indiquer un blood moon
		started = System.currentTimeMillis();
		getServer().broadcastMessage(ChatColor.RED + "THE BLOOD MOON IS RISING");
		getServer().broadcastMessage(ChatColor.RED + "GET READY FOR A LOT OF MONSTERS");
		
		// 20 secondes avant de partir le spawning!
		TimeServer.getInstance().addListener(this, 20000);
		
		_active = true;
		
	}
	
	private void triggerMonstersSpawning() {
		for (Player p : getServer().getOnlinePlayers()) {
			spawnMonsterCloseToPlayer(p.getLocation());
			spawnMonsterCloseToPlayer(p.getLocation());
		}
		
		if (listener == null) {
			listener = new MonsterSpawnListener(this);
			getServer().getPluginManager().registerEvents(listener, getPlugin());
		}		
	}
	
	@Override
	public void timeHasCome(long time) {
		triggerMonstersSpawning();
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}

	@Override
	public void abortScenario() {
		getPlayers().clear();
		getServer().broadcastMessage("BloodMoon as been cancelled");
		
	}

	@Override
	public boolean abortWhenLeaving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeTriggered() {

		if (_active) {
			return false;
		}
			
		if (started + coolDown > System.currentTimeMillis()) {
			return false;
		}
						
		int currentTime = WorldClock.getHour(getWorld());
		if (lastTimeChecked == currentTime) {
			return false;
		}

		boolean draw = RandomUtil.getDrawResult(probability);
		lastTimeChecked = currentTime;
		return currentTime == 12 && draw;
	}

	@Override
	public boolean mustBeStop() {
		return _active && WorldClock.getHour(getWorld()) == 23;
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
		WorldZone exclusion = new WorldZone(l, 7.0);
		WorldZone area = new WorldZone(l, 15.0);
		int maxTry = 0;
		Location newLoc = area.getRandomLocationOutsideThisZone(l.getWorld(), exclusion);
		while (maxTry < 3 && !validLocationForMonster(newLoc)) {
			newLoc = area.getRandomLocationOutsideThisZone(l.getWorld(), exclusion);
			maxTry++;
		}
		
		if (maxTry < 3) {
			CreatureType type = EntityUtilities.getCreatureType(monsters[RandomUtil.getRandomInt(monsters.length)]);
			getWorld().spawnCreature(newLoc, type);	
		}	
	}
	
	private boolean validLocationForMonster(Location l) {
		Location under = new Location(l.getWorld(), l.getX(), l.getY() - 1.0, l.getZ());
		org.bukkit.block.Block b = under.getBlock();
		for (int id : validBlockRaw) {
			if (b.getTypeId() == id) {
				return true;
			}
		}
		return false;
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
		boolean draw = RandomUtil.getDrawResult(2);
		if (_moon.isTriggered() && !_avoid && draw && event.getEntity() instanceof Monster) {
			_avoid = true;
			_moon.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
			_avoid = false;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDeath(EntityDeathEvent event) {
		if (_moon.isTriggered() && event.getEntity() instanceof Monster) {
			_moon.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
		}
	}
}
