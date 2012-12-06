package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.LootGenerator;
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
	private int _monsterKilled = 0;
	private double _maxMonster = 0;
	private boolean _lootCreated = false;
	private long _lootDisapearInHours = 2;

	@EventHandler (priority = EventPriority.NORMAL)
	public void monsterDies(EntityDeathEvent event) {
		
		if (_monsters.contains(event.getEntity())) {
			_monsters.remove(event.getEntity());
			_monsterKilled++;
			
			if (_maxMonster > 0) {
				double percentKilled = _monsterKilled / _maxMonster;
				_logger.info("percent killed: " + percentKilled + " killed: " + _monsterKilled + " max:" + _maxMonster);
				if (percentKilled > 0.8 && !_lootCreated) { // 80% de monstres tuées
					
					_logger.info("loot created!");
					generateLoot();
				}
			}
		}
	}
	
	private void generateLoot() {
		if (_loots.size() > 0 && !_lootCreated) {
			Collections.shuffle(_loots);
			PinPoint lootPt = _loots.get(0);
			
			Location location = new Location(_world, _location.getX() + lootPt.X, _location.getY() + lootPt.Y, _location.getZ() + lootPt.Z);
			Loot loot = LootGenerator.getFightingRandomLoot(ScenarioService.getInstance().calculateHealthModifierWithFrontier(location, _world.getSpawnLocation()));
			loot.generateLoot(location);
			_lootCreated = true;
			Executors.newSingleThreadScheduledExecutor().schedule(new LootDisapearer(location), _lootDisapearInHours, TimeUnit.HOURS);
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
	public void setConfiguration(String config) {
		// TODO Auto-generated method stub
		
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
				
				List<Runnable> list = new ArrayList<Runnable>();
				for (MonsterSpawner sp : _spawners) {
					if (sp.getName().equalsIgnoreCase(name)) {
						list.add(sp);
					}
				}
				
				if (list.size() > 0 && _server != null) {
					ZoneTrigger trigger = new ZoneTrigger(list, _server);
					trigger.setWorldZone(zone);
					_zoneObservers.add(trigger);
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
}

class LootDisapearer implements Runnable {
	
	private Location _location;
	
	public LootDisapearer(Location location) {
		_location = location;
	}

	@Override
	public void run() {
		
		try {
			if (_location != null) {
				Block b = _location.getWorld().getBlockAt(_location);
				
				if (b.getType() == Material.CHEST) {
					Chest chest = (Chest)b.getState();
					Inventory inv = chest.getInventory();
					inv.clear();
					b.setType(Material.AIR);
				}
			}	
		}
		catch (NullPointerException ex) {
			// on etouffe l'exception
		}
		
	}
}
