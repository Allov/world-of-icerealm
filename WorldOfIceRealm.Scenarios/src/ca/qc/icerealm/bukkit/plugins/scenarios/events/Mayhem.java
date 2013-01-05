package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.spawners.ApocalypseSpawner;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PoisonPlayer;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.SoundRepeater;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.TimeFormatter;

public class Mayhem extends BaseEvent {
	private Logger _logger = Logger.getLogger("Minecraft");
	private boolean _switchActivated = false;
	private long _totalForPreparation = 2000; 		// 60 sec
	private long _incrementForPreparation = 2000; 	// 15 sec
	private long _delayBetweenPoison = 30000; 		// 30 sec
	private long _delayBetweenWaveSpawn = 20000; 	// 20 sec
	private static boolean _apocalypseOn = false;
	private static boolean _sequenceStarted = false;
	private int _monsterDead = 0;
	private int _maxMonsters = 5;
	private int _poisonDuration = 70; 				// 3 sec
	private int _poisonAmplifier = 1;
	private boolean _completed = false;
	private boolean _spawnMutex = false;
	protected ScheduledExecutorService _executor;
	protected ApocalypseSpawner _spawner;
	protected PoisonPlayer _poison;
	protected Loot _loot;
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
				_logger.info("exception raised in Mayhem.monsterSpawn(CreatureSpawnEvent event)");
			}
			
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void monsterDies(EntityDeathEvent event) {
		
		if (_apocalypseOn && event.getEntity().getLastDamageCause().getCause() != DamageCause.FIRE_TICK) {
			if (event.getEntity() instanceof Monster) {
				_monsterDead++;
			}
			
			if (_monsterDead >= _maxMonsters) {
				_completed = true;
				distributeLoot();
				resetEvent();
			}		
			
			try {
				_spawner.spawnMonsterCloseToPlayer(event.getEntity().getLocation());
			}
			catch (Exception ex) {
				_logger.info("exception raised in Mayhem.monsterDies(EntityDeathEvent event) while using the ApocalypseSpawner");
			}
			
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerDies(PlayerDeathEvent event) {
		int playerDead = 0;
		for (Player p : _server.getOnlinePlayers()) {
			if (p.isDead()) playerDead++;
		}
		
		if (playerDead == _server.getOnlinePlayers().length) releaseEvent();
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		if (_server.getOnlinePlayers().length == 1) {
			releaseEvent();
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (_apocalypseOn) {
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "There is an" + ChatColor.DARK_RED + " Apocalypse" + ChatColor.RED + " right now!");
			_server.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + _monsterDead + ChatColor.DARK_PURPLE + "/" + ChatColor.LIGHT_PURPLE + " monsters dead.");	
		}
	}
		
	@Override
	protected long getCoolDownInterval() {
		return 60000;
	}

	@Override
	protected void resetEvent() {
		// affiche le message
		if (_apocalypseOn && _completed) {
			_server.broadcastMessage(ChatColor.GREEN + "The " + ChatColor.DARK_AQUA + "World of IceRealm" + ChatColor.GREEN + " has been saved from the" + ChatColor.DARK_RED + " Apocalypse!");
			_server.broadcastMessage(ChatColor.GREEN + "The " + ChatColor.GOLD + "loot " + ChatColor.GREEN + "is located near the switch!");
		} 
		else if (_apocalypseOn) {
			_server.broadcastMessage(ChatColor.GREEN + "The monsters are retreating, the" + ChatColor.DARK_RED + " Apocalypse" + ChatColor.GREEN + " is over");
		}
		else if (!_apocalypseOn) {
			if (_server != null) _server.broadcastMessage(ChatColor.WHITE + "The Gods spared us from the " + ChatColor.DARK_RED + "Apocalypse.");
		}
		
		_monsterDead = 0;
		_switchActivated = false;
		_apocalypseOn = false;
		_sequenceStarted = false;
		_completed = false;
		if (_executor != null) _executor.shutdownNow();
	}
	
	private void distributeLoot() {
		
		if (_lootPoints.size() > 0) {
			Collections.shuffle(_lootPoints);
			_loot = LootGenerator.getApocalypseLoot(Frontier.getInstance().calculateGlobalModifier(_source));
			_loot.generateLoot(transformPinIntoLocations(_lootPoints.get(0)));
		}
	}

	@Override
	public void activateEvent() {
						
		if (!_apocalypseOn && _switchActivated && !_sequenceStarted) {
			
			if (_loot != null) _loot.removeLoot();
			
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
		if (_loot != null) _loot.removeLoot();
	}

	@Override
	public String getName() {
		return "mayhem";
	}
	
	protected void startApocalypse() {
		_random = new Random();
		_server.broadcastMessage(ChatColor.DARK_RED + "APOCALYPSE NOW!");
		
		_poison = new PoisonPlayer(_server.getOnlinePlayers(), _poisonDuration, _poisonAmplifier);
		_executor.scheduleAtFixedRate(_poison, _delayBetweenPoison, _delayBetweenPoison, TimeUnit.MILLISECONDS);
		
		_spawner = new ApocalypseSpawner();
		_executor.scheduleAtFixedRate(_spawner, 0, _delayBetweenWaveSpawn, TimeUnit.MILLISECONDS);
		
		SoundRepeater thunder = new SoundRepeater(_executor, 20, _source, Sound.AMBIENCE_THUNDER, 10, 1);
		_executor.scheduleAtFixedRate(thunder, 0, 3000, TimeUnit.MILLISECONDS);

		SoundRepeater dragonScream = new SoundRepeater(_executor, 5, _source, Sound.ENDERDRAGON_GROWL, 20, 1);
		dragonScream.run();
		
		_apocalypseOn = true;
	}
	
	@Override
	public void setConfiguration(String config) {
		super.setConfiguration(config);
		applyConfiguration();
	}
	
	private void applyConfiguration() {
		try {
			_logger.info("applying new configto mayhem: " + _config);
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
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Apocalypse " + ChatColor.RED + "starts " + ChatColor.YELLOW + "in " + TimeFormatter.readableTime(_timeLeft));
			_mayhem._executor.schedule(this, _increment, TimeUnit.MILLISECONDS);
		}
	}
	
}
