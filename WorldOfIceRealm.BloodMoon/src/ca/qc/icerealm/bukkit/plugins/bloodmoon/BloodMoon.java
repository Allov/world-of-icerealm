package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldClock;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class BloodMoon extends JavaPlugin implements TimeObserver {
	private final Logger logger = Logger.getLogger(("Minecraft"));
	
	// nombre constrant de millisecond dans une minute
	private final long _msInMinecraftHour = 50000; 
	// il y a une chance sur 10 qu'un blood moon soit activé
	private final int _probability = 1;
	// le nombre de millisecond avant de spawner autour du joueur
	private final long _delay = 20000; // 20 sec
	
	
	
	// le id des blocks valides
	// 2 = grass
	// 12 = sand
	// 3 = dirt
	// 18 = leaf
	// 31 = tall grass
	// 13 = gravel
	private final int[] _validBlockRaw = new int[] { 2, 12, 3, 18, 31, 13 };
		
	// variable du serveur
	private World _world;
	private long _timeAlarm = 0;
	private boolean _isActive = false;
	private Listener _listener;
	private BloodMoonStarter _starter;
	
	
	

	@Override
	public void onDisable() {
		TimeServer.getInstance().removeListener(this);
		TimeServer.getInstance().removeListener(_starter);
	}

	@Override
	public void onEnable() {
		getCommand("bm").setExecutor(new BloodMoonCommander(this));
		initializeTimer();
	}

	@Override
	public void timeHasCome(long time) {
		long newAlarm = 0;
		
		if (_isActive) {
			stopBloodMoon();
			newAlarm = 24 * _msInMinecraftHour;
			// on veut se faire poker a la fin de la journée
			displayListenerAddition("blood moon stopping", newAlarm);
		}
		else {
			boolean draw = RandomUtil.getDrawResult(_probability);
			if (draw) {
				startBloodMoon();
				
				// on veut se faire pocker a la fin de la nuit
				newAlarm =  12 * _msInMinecraftHour;
				displayListenerAddition("blood moon started", newAlarm);
			}
			else {
				// on veut se faire poker lors de la prochaine nuit
				newAlarm = 24 * _msInMinecraftHour;
				displayListenerAddition("no moon, next night", newAlarm);
			}
		}

		TimeServer.getInstance().addListener(this, newAlarm);
	}
	
	public void initializeTimer() {
		// calcul du temps présent et la prochaine alarm
		_world = this.getServer().getWorld("world");
		int hour = WorldClock.getHour(_world);
		long alarm = 0;
		
		if (hour <= 12) {
			int untilNight = 12 - hour;
			alarm = untilNight * _msInMinecraftHour;
		}
		else if (hour > 12) {
			int untilNight = (24 - hour) + 12;
			alarm = untilNight * _msInMinecraftHour;
		}
		
		displayListenerAddition("init timer", alarm);
		TimeServer.getInstance().addListener(this, alarm);
	}
	
	private void displayListenerAddition(String event, long a) {
		this.logger.info(event + " - Time is: " + _world.getTime() + " and will be poked in " + a + " ms");
	}

	@Override
	public void setAlaram(long time) {
		_timeAlarm = time;
	}

	@Override
	public long getAlarm() {
		return _timeAlarm;
	}
	
	public void startBloodMoon() {
		
		getServer().broadcastMessage(ChatColor.DARK_RED + "The Blood Moon " + ChatColor.DARK_GREEN + "is rising!");
		_starter = new BloodMoonStarter(this);
		TimeServer.getInstance().addListener(_starter, _delay);
				
		if (_listener == null) {
			_listener = new MonsterSpawnListener(this);
			getServer().getPluginManager().registerEvents(_listener, this);
		}	
		
		_isActive = true;
	}
	
	public void stopBloodMoon() {
		_isActive = false;
		getServer().broadcastMessage(ChatColor.DARK_GREEN + "The Blood Moon is now over!");
		
		
	}
	
	public boolean isActive() {
		return _isActive;
	}
	
	public void setActive(boolean active) {
		_isActive = active;
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
			_world.spawnCreature(newLoc, type);	
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
