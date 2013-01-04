package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ca.qc.icerealm.bukkit.plugins.scenarios.spawners.ApocalypseSpawner;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.TimeFormatter;

public class Mayhem extends BaseEvent {
	private Logger _logger = Logger.getLogger("Minecraft");
	private boolean _switchActivated = false;
	private long _totalForPreparation = 60000; // 1 minute
	private long _incrementForPreparation = 15000; // 15 sec
	private long _delayBetweenPoison = 60000; // 1 minute
	private long _delayBetweenWaveSpawn = 10000; // 10 sec
	private static boolean _apocalypseOn = false;
	private static boolean _sequenceStarted = false;
	private int _monsterDead = 0;
	private int _maxMonsters = 200;
	private int _poisonDuration = 55; // 3 sec
	private int _poisonAmplifier = 1;
	private boolean _completed = false;
	private boolean _spawnMutex = false;
	protected ScheduledExecutorService _executor;
	protected ApocalypseSpawner _spawner;
	protected Random _random = new Random();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void redstoneSwitch(BlockRedstoneEvent event) {
		
		if (event.getBlock().getLocation().getX() == (_source.getX() -1) &&
			event.getBlock().getLocation().getY() == _source.getY() &&
			event.getBlock().getLocation().getZ() == _source.getZ() && !_switchActivated) {

				_switchActivated = true;
				activateEvent();
		}		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void monsterSpawn(CreatureSpawnEvent event) {
		
		if (_apocalypseOn && _random.nextBoolean() && !_spawnMutex) {
			try {
				_spawnMutex = true;
				_spawner.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
				_spawnMutex = false;
			}
			catch (Exception ex) {
				// on s'en fou!
			}
			
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void monsterDies(EntityDeathEvent event) {
		
		_logger.info("Monster dies: " + event.getEntityType());
		if (_apocalypseOn && event.getEntity().getLastDamageCause().getCause() != DamageCause.FIRE_TICK) {
			if (event.getEntity() instanceof Monster) {
				_monsterDead++;
				_logger.info("_monsterDead " + _monsterDead);
			}
			
			if (_monsterDead >= _maxMonsters) {
				_logger.info("reseting the event?");
				_completed = true;
				resetEvent();
			}		
			
			try {
				_spawner.spawnMonsterCloseToPlayer(event.getEntity().getLocation());	
			}
			catch (Exception ex) {
				// peut pas passer l'event, on s'en fou de ca!
			}
			
		}
	}
	
	
	@Override
	protected long getCoolDownInterval() {
		return 500;
	}

	@Override
	protected void resetEvent() {
		// affiche le message
		if (_apocalypseOn && _completed) {
			_server.broadcastMessage(ChatColor.AQUA + "The World of IceRealm has been saved. You survived the apocalypse.");
		}
		else if (_apocalypseOn) {
			_server.broadcastMessage(ChatColor.AQUA + "The monsters are retreating. You survived the apocalypse.");
		}
		else if (!_apocalypseOn) {
			if (_server != null) _server.broadcastMessage(ChatColor.AQUA + "The Gods spared us from an apocalypse!");
		}
		
		_monsterDead = 0;
		_switchActivated = false;
		_apocalypseOn = false;
		_sequenceStarted = false;
		_completed = false;
		if (_executor != null) _executor.shutdownNow();
	
	}

	@Override
	public void activateEvent() {
						
		if (!_apocalypseOn && _switchActivated && !_sequenceStarted) {
			 _executor = Executors.newSingleThreadScheduledExecutor();
			_server.broadcastMessage(ChatColor.RED + "The End Of The World is near. Apocalypse is due in " + TimeFormatter.readableTime(_totalForPreparation));
			_server.broadcastMessage(ChatColor.YELLOW + "You have to kill " + ChatColor.LIGHT_PURPLE + _maxMonsters + " monsters" + ChatColor.YELLOW + " to " + ChatColor.LIGHT_PURPLE + "end this apocalypse");
			_server.broadcastMessage(ChatColor.YELLOW + "Any player above" + ChatColor.LIGHT_PURPLE + " sea level" + ChatColor.YELLOW + " will be" + ChatColor.DARK_GREEN +  " poisonned");
			_executor.schedule(new PreparationTimer(_totalForPreparation, _incrementForPreparation, this), _incrementForPreparation, TimeUnit.MILLISECONDS);
			_sequenceStarted = true;
			_completed = false;
			_monsterDead = 0;
		}

	}

	@Override
	public void releaseEvent() {
		resetEvent();
	}

	@Override
	public String getName() {
		return "mayhem";
	}
	
	protected void startApocalypse() {
		_random = new Random();
		_server.broadcastMessage(ChatColor.DARK_RED + "APOCALYPSE NOW!");
		
		PoisonPlayer poisonPlayer = new PoisonPlayer(_server.getOnlinePlayers(), _poisonDuration, _poisonAmplifier);
		_executor.scheduleAtFixedRate(poisonPlayer, _delayBetweenPoison, _delayBetweenPoison, TimeUnit.MILLISECONDS);
		
		_spawner = new ApocalypseSpawner();
		_executor.scheduleAtFixedRate(_spawner, _delayBetweenWaveSpawn, _delayBetweenWaveSpawn, TimeUnit.MILLISECONDS);
		
		_apocalypseOn = true;
	}
	
	@Override
	public void setConfiguration(String config) {
		super.setConfiguration(config);
		applyConfiguration();		
	}
	
	private void applyConfiguration() {
		try {
			_logger.info("applying new config: " + _config);
			String[] values = _config.split(",");
			if (values.length > 2) {
				
				_delayBetweenPoison = Integer.parseInt(values[0]);
				_poisonDuration = Integer.parseInt(values[1]);
				_poisonAmplifier = Integer.parseInt(values[2]);
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

}

class PreparationTimer implements Runnable {

	private long _timeLeft = 0;
	private long _increment; // minunte
	private Mayhem _mayhem;
	
	public PreparationTimer(long timeLeft, long increment, Mayhem m) {
		_timeLeft = timeLeft;
		_mayhem = m;
		_increment = increment;
	}
	
	@Override
	public void run() {
		_timeLeft -= _increment;

		if (_timeLeft <= 0) {
			_mayhem.startApocalypse();
		}
		else {
			Bukkit.getServer().broadcastMessage(ChatColor.RED + "Apocalypse starts in " + ChatColor.YELLOW + TimeFormatter.readableTime(_timeLeft));
			_mayhem._executor.schedule(this, _increment, TimeUnit.MILLISECONDS);
		}
	}
	
}

class PoisonPlayer implements Runnable {
	
	private Player[] _players;
	private int _duration = 50;
	private int _amplifier = 0;
	
	public PoisonPlayer(Player[] players, int duration, int amplifier) {
		_players = players;
		_duration = duration;
		_amplifier = amplifier;
	}

	@Override
	public void run() {
		
		//Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "Gigantic waves of radiation are hitting the ground!!!");
		for (Player p : _players) {
			if (p.getLocation().getY() > p.getLocation().getWorld().getSeaLevel()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, _duration, _amplifier));
			}
			
		}
	}
}
