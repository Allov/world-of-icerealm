package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldClock;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class BloodMoon extends JavaPlugin {
	private final Logger logger = Logger.getLogger(("Minecraft"));
	
	// nombre constrant de millisecond dans une minute
	private final long _msInMinecraftHour = 50; 
	// il y a une chance sur 10 qu'un blood moon soit activé
	private int _probability = 10;
	// le nombre de millisecond avant de spawner autour du joueur
	private long _delay = 20000; // 20 sec

	// le id des blocks valides
	// 79 = snow
	// 2 = grass
	// 12 = sand
	// 3 = dirt
	// 18 = leaf
	// 31 = tall grass
	// 13 = gravel
	// 80 = snow block
	private final int[] _validBlockRaw = new int[] { 79, 2, 12, 3, 18, 31, 13, 80 };
		
	// variable du serveur
	private World _world;
	private int _attemptDone = 0;
	private boolean _cumulative = false;
	private boolean _isActive = false;
	private Listener _listener;
	private BloodMoonStarter _starter;
	private BloodMoonStopper _stopper;
	private BloodMoonDraw _draw;
	
	public boolean isCumulative() {
		return _cumulative;
	}
	
	public void setCumulative(boolean v) {
		_cumulative = v;
	}
	
	public int getAttemptDone() {
		return _attemptDone;
	}
	
	public void setAttemptDone(int a) {
		_attemptDone = a;
	}
	
	public long getDelay() {
		return _delay;
	}
	
	public void setDelay(long s) {
		_delay = s;
	}
	
	public void setProbability(int n) {
		_probability = n;
	}
	
	public int getProbability() {
		return _probability;
	}
	
	@Override
	public void onDisable() {
		this.logger.info("[Blood Moon] - removing active listeners");
		TimeServer.getInstance().removeListener(_starter);
		TimeServer.getInstance().removeListener(_stopper);
		TimeServer.getInstance().removeListener(_draw);
	}

	@Override
	public void onEnable() {
		/*
		for (int i = 0; i < 500; i++) {
			TimeServer.getInstance().addListener(new UselessTimer(i), 2000);
		}
		*/
		getCommand("bm").setExecutor(new BloodMoonCommander(this));
		initializeTimer();
		
		if (_listener == null) {
			_listener = new MonsterSpawnListener(this);
			getServer().getPluginManager().registerEvents(_listener, this);
		}
	}
	
	public void initializeTimer() {
		// on arrete le bloodmoon et reinitialise les timers
		TimeServer.getInstance().removeListener(_starter);
		TimeServer.getInstance().removeListener(_stopper);
		TimeServer.getInstance().removeListener(_draw);
		_isActive = false;
		_attemptDone = 0;
		
		// calcul du temps présent et la prochaine alarm
		_world = this.getServer().getWorld("world");
		long hour = _world.getTime();
		long alarm = 0;
		
		if (hour < 12000) {
			alarm = (12000 - hour) * _msInMinecraftHour;
		}
		else if (hour >= 12000) {
			alarm = ((24000 - hour) + 12000) * _msInMinecraftHour;
		}
		
		displayListenerAddition("init timer", alarm);
		_draw = new BloodMoonDraw(this);
		TimeServer.getInstance().addListener(_draw, alarm);
	}
	
	private void displayListenerAddition(String event, long a) {
		this.logger.info("[BloodMoon] " + event + " - Time is: " + _world.getTime() + " and will be poked in " + a + " ms");
	}

	public boolean drawBloodMoon() {
		int modifiedProb = _probability - _attemptDone;
		boolean draw = RandomUtil.getDrawResult(modifiedProb);
		
		if (!draw && _cumulative) {
			_attemptDone++;
		}
		
		return draw;
	}
	
	public void startBloodMoon() {
		
		getServer().broadcastMessage(ChatColor.DARK_RED + "The Blood Moon " + ChatColor.DARK_GREEN + "is rising!");
		// le starter pour spawner les monstres apres 10 sec
		_starter = new BloodMoonStarter(this);
		TimeServer.getInstance().addListener(_starter, _delay);
		// le stopper pour arreter le bloodmoon dans 12h minecraft
		_stopper  = new BloodMoonStopper(this);
		TimeServer.getInstance().addListener(_stopper, 12000 * _msInMinecraftHour);
		// reset les essais
		_attemptDone = 0;
		_isActive = true;
	}
	
	public void stopBloodMoon() {
		initializeTimer();
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
