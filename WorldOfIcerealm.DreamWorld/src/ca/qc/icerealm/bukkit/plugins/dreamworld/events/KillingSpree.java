package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.minecraft.server.PlayerDistanceComparator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.TimeFormatter;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class KillingSpree implements Event {

	private Logger _logger = Logger.getLogger("Minecraft");
	private World _world;
	private Location _location;
	private List<MonsterSpawner> _spawners;
	private List<ZoneObserver> _zoneObservers;
	private Server _server;
	private String _name = "killingspree";
	private List<List<PinPoint>> _zones;
	private List<PinPoint> _loots;
	private List<PinPoint> _pin;
	private List<LivingEntity> _monsters;
	private Integer _monsterKilled = 0;
	private double _maxMonster = 0;
	private boolean _lootCreated = false;
	private long _lootDisapearInHours = 7200000;
	private Loot _loot;
	private String _config;
	private List<Player> _players;
	private List<ZoneTrigger> _triggers;
	private long _reactivationIn = 0;
	private int _monsterKilledThreshold = 0;

	public KillingSpree() {
		_players = new ArrayList<Player>();
		_triggers = new ArrayList<ZoneTrigger>();		
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void monsterDies(EntityDeathEvent event) {

		if (_monsters.contains(event.getEntity())) {
			_monsters.remove(event.getEntity());
			_monsterKilled++;
			
			if (_maxMonster > 0) {
				double percentKilled = _monsterKilled / _maxMonster;	
				
				if (_monsterKilled % _monsterKilledThreshold == 0) {
					Integer percentMsg = (int)(percentKilled * 100);
					
					for (Player p : _players) {
						p.sendMessage(ChatColor.GOLD + percentMsg.toString() + "%" + ChatColor.RED + " monsters killed!");
					}
				}
				
				if (percentKilled > 0.8 && !_lootCreated) { // 80% de monstres tuées
					
					generateLoot();
					_logger.info("loot created! " + _lootCreated);
					for (Player p : _players) {
						p.sendMessage(ChatColor.GREEN + "The" + ChatColor.GOLD + " chest loot " + ChatColor.GREEN + "just appeared!");
					}
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void playerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		_players.remove(player);
		//_logger.info("loot created! " + _lootCreated);
		if (_players.size() == 0 && !_lootCreated) {
			//_logger.info("player was last to leave!! and the loot not created");
			Executors.newSingleThreadScheduledExecutor().schedule(new ResetKillingSpree(_loot, _triggers, this), _lootDisapearInHours, TimeUnit.MILLISECONDS);
			_reactivationIn = System.currentTimeMillis() + _lootDisapearInHours;
			for (ZoneTrigger z : _triggers) {
				z.setCoolDown(_reactivationIn);
				z.setLootCreated(true);
			}
		}
		
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void playerDies(PlayerDeathEvent event) {
		Player player = event.getEntity();
		_players.remove(player);
		//_logger.info("player removed! size: " + _players.size());
		if (_players.size() == 0 && !_lootCreated) {
			for (ZoneTrigger ob : _triggers) {
				//_logger.info("player size=0; zonetrigger activate to FALSE");
				ob.setActivate(false);
			}
			
			for (LivingEntity m : _monsters) {
				m.remove();
			}
		}
	}
	
	private void generateLoot() {
		if (_loots.size() > 0 && !_lootCreated) {
			Collections.shuffle(_loots);
			PinPoint lootPt = _loots.get(0);
			
			Location location = new Location(_world, _location.getX() + lootPt.X, _location.getY() + lootPt.Y, _location.getZ() + lootPt.Z);
			_loot = LootGenerator.getFightingRandomLoot(ScenarioService.getInstance().calculateHealthModifierWithFrontier(location, _world.getSpawnLocation()));
			_loot.generateLoot(location);
			_lootCreated = true;
			//_logger.info("will reset in 50 sec");
			//Executors.newSingleThreadScheduledExecutor().schedule(new ResetKillingSpree(_loot, _triggers, this), 50, TimeUnit.SECONDS);
			
			Executors.newSingleThreadScheduledExecutor().schedule(new ResetKillingSpree(_loot, _triggers, this), _lootDisapearInHours, TimeUnit.MILLISECONDS);
			_reactivationIn = System.currentTimeMillis() + _lootDisapearInHours;
			for (ZoneTrigger z : _triggers) {
				z.setCoolDown(_reactivationIn);
				z.setLootCreated(true);
			}
			
			//_logger.info("loot created at: " + _location.toString());
			/*
			Block b = _world.getBlockAt(location);
			b.setType(Material.CHEST);
			
			if (b.getType() == Material.CHEST) {
				Chest chest = (Chest)b.getState();
				Inventory inv = chest.getInventory();
				inv.addItem(new ItemStack(Material.DIAMOND, 4));
				_lootCreated = true;
				Executors.newSingleThreadScheduledExecutor().schedule(new LootDisapearer(location), _lootDisapearInHours, TimeUnit.HOURS);
				
			}
			*/
		}
	}
	
	@Override
	public void setSourceLocation(Location source) {
		// TODO Auto-generated method stub
		_location = source;
		_world = source.getWorld();
		_spawners = new ArrayList<MonsterSpawner>();
		_zoneObservers = new ArrayList<ZoneObserver>();
		_monsters = new ArrayList<LivingEntity>();
	}
	
	@Override
	public void setPinPoints(List<PinPoint> points) {
		_pin = points;
		_maxMonster = _pin.size();
		_monsterKilledThreshold = (int)_maxMonster / 4;
	}

	@Override
	public void setLootPoints(List<PinPoint> loots) {
		_loots = loots;
	}

	@Override
	public void setActivateZone(List<List<PinPoint>> zones) {
		_zones = zones;
	}

	@Override
	public void setWelcomeMessage(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEndMessage(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServer(Server s) {
		_server = s;
	}
	

	@Override
	public void activateEvent() {

		for (PinPoint p : _pin) {
			Location l = new Location(_location.getWorld(), _location.getX() + p.X, _location.getY() + p.Y, _location.getZ() + p.Z);
			MonsterSpawner spawner = new MonsterSpawner(l, p.Name, _monsters);
			_spawners.add(spawner);
		}
		
		
		for (List<PinPoint> points : _zones) {
			if (points.size() == 2) {
				Location lower = new Location(_world, _location.getX() + points.get(0).X, _location.getY() + points.get(0).Y, _location.getZ() + points.get(0).Z);
				Location higher = new Location(_world, _location.getX() + points.get(1).X, _location.getY() + points.get(1).Y, _location.getZ() + points.get(1).Z);
				
				String name = points.get(0).Name;
				WorldZone zone = new WorldZone(lower, higher);
				
				List<MonsterSpawner> list = new ArrayList<MonsterSpawner>();
				for (MonsterSpawner sp : _spawners) {
					if (sp.getName().equalsIgnoreCase(name)) {
						list.add(sp);
					}
				}
				
				if (list.size() > 0 && _server != null) {
					ZoneTrigger trigger = new ZoneTrigger(list, _server);
					trigger.setWorldZone(zone);
					trigger.setPlayerList(_players);
					_zoneObservers.add(trigger);
					_triggers.add(trigger);
					ZoneServer.getInstance().addListener(trigger);
				}
			}			
		}
	}

	@Override
	public void releaseEvent() {
		for (ZoneObserver ob : _zoneObservers) {
			ZoneServer.getInstance().removeListener(ob);
		}
		
		for (LivingEntity l : _monsters) {
			l.remove();
		}
		
	}

	@Override
	public String getName() {
		return _name;
	}
	
	@Override
	public String getConfiguration() {
		return _config;
	}
	
	@Override
	public void setConfiguration(String config) {
		_config = config;
	}
	
	public void clearMonsterKilled() {
		_monsterKilled = 0;
		_players.clear();
		_lootCreated = false;
	}

}

class ResetKillingSpree implements Runnable {
	
	private Logger _logger = Logger.getLogger("Minecraft");
	private Loot _loot;
	private List<ZoneTrigger> _triggers;
	private KillingSpree _spree;
	
	
	public ResetKillingSpree(Loot loot, List<ZoneTrigger> t, KillingSpree ks) {
		_loot = loot;
		_triggers = t;
		_spree = ks;
	}

	@Override
	public void run() {
		
		try {
			if (_loot != null) {
				_loot.removeLoot();
			}	
			
			_logger.info("removing loot!");
			for (ZoneTrigger ob : _triggers) {
				ob.setActivate(false);
				ob.setLootCreated(false);
			}
						
			_spree.clearMonsterKilled();
		}
		catch (NullPointerException ex) {
			// on etouffe l'exception
		}
		
	}
}
