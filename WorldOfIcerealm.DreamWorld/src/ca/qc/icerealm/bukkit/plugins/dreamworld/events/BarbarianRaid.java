package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.omg.stub.java.rmi._Remote_Stub;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.ArtilleryShelling;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.BlockRestore;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.TimeFormatter;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class BarbarianRaid extends BaseEvent implements Runnable, ZoneObserver {

	private Logger _logger = Logger.getLogger("Minecraft");
	private int MAX_WAVE = 5;
	private int MONSTER_PER_LOCATION = 1;
	protected long INTERVAL_BETWEEN_ATTACK = 7200; // 7200 sec = 2 heures
	private long INTERVAL_BETWEEN_WAVE = 10; // 10 secondes;
	private boolean USE_ARTILLERY = true;
	private int NB_ARTILLERY_SHOT = 5;
	private String LIST_MONSTER = "";
	
	private List<Location> _locations;
	private String[] _monsters = new String[] { "creeper", "zombie", "spider", "cavespider", "pigzombie" };
	protected int _waveDone = 0;
	private World _world;
	private HashSet<Integer> _monstersContainer;
	private WorldZone _activationZone;
	protected List<Player> _players;
	protected boolean _activated;
	private boolean _started;
	private Loot _loot;
	private BlockRestore _blockRestore;
	protected long _timeForReactivation;
	private HashSet<Monster> _monstersEntity;
		
	public BarbarianRaid() {
		_monstersContainer = new HashSet<Integer>();
		_monstersEntity = new HashSet<Monster>();
		_locations = new ArrayList<Location>();
		_players = new ArrayList<Player>();
		_activated = true;
		_started = false;
	}
		
	@Override
	public void run() {
			
		if (_activated) {
			for (Location loc : _locations) {
					
				double modifier = ScenarioService.getInstance().calculateHealthModifierWithFrontier(loc, _world.getSpawnLocation()) + ((double)_waveDone / (double)MAX_WAVE);
				
				for (int i = 0; i < MONSTER_PER_LOCATION; i++) {
					String monster = _monsters[RandomUtil.getRandomInt(_monsters.length)];
					Entity e = ScenarioService.getInstance().spawnCreature(_world, loc, EntityUtilities.getEntityType(monster), modifier, false);
					_monstersContainer.add(e.getEntityId());
					_monstersEntity.add((Monster)e);
					
					
					if (_players.size() > 0) {
						Collections.shuffle(_players);
						Monster m = (Monster)e;
						m.setTarget(_players.get(0));
					}
				}
			}	
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void playerDies(PlayerDeathEvent event) {
		Player p = event.getEntity();
		_players.remove(p);
		
		if (_players.size() == 0 && _started) {
			EventActivator activator = new EventActivator(this);
			activator.run();
			
			if (_blockRestore != null) {
				_blockRestore.run();
			}
			for (Monster m : _monstersEntity) { 
				m.remove();
			}
			_monstersContainer.clear();
			_started = false;
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void playerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		_players.remove(p);
		
		if (_players.size() == 0 && _started) {
			processEndEvent();
			/*
			for (Monster m : _monstersEntity) { 
				m.remove();
			}
			_monstersContainer.clear();
			*/
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void monsterDies(EntityDeathEvent event) {
		processKill(event.getEntity().getEntityId());
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void monsterDies(EntityExplodeEvent event) {
		if (event.getEntity() != null) {
			processKill(event.getEntity().getEntityId());
		}
	}
	
	private void processKill(int id) {
		
		if (_monstersContainer.contains(id)) {
			_monstersContainer.remove(id);
			
			if (_monstersContainer.size() == 0) {
				_waveDone++;
				
				for (Player p : _players) {
					p.sendMessage(ChatColor.YELLOW + "This wave has been wiped out!");
				}
				
				if (_waveDone < MAX_WAVE) {
					if (USE_ARTILLERY) {
						Executors.newSingleThreadScheduledExecutor().schedule(new ArtilleryShelling(_activationZone, NB_ARTILLERY_SHOT), INTERVAL_BETWEEN_WAVE - 3, TimeUnit.SECONDS);	
					}
					
					Executors.newSingleThreadScheduledExecutor().schedule(this, INTERVAL_BETWEEN_WAVE, TimeUnit.SECONDS);
					for (Player p : _players) {
						p.sendMessage(ChatColor.RED + "Another wave is coming... " + ChatColor.GOLD + " They look stronger!");
					}
				}
				else {
					generateLoot();
					processEndEvent();
				}
			}
		}
		
		
	}
	
	protected void processEndEvent() {
		_activated = false;
		_started = false;
		_timeForReactivation = System.currentTimeMillis() + INTERVAL_BETWEEN_ATTACK * 1000;
		Executors.newSingleThreadScheduledExecutor().schedule(new EventActivator(this), INTERVAL_BETWEEN_ATTACK, TimeUnit.SECONDS);
		Executors.newSingleThreadScheduledExecutor().schedule(_blockRestore, INTERVAL_BETWEEN_ATTACK, TimeUnit.SECONDS);
		_players.clear();
	}
	
	protected void welcomeMessage(Player arg0) {
		if (!_activated) {
			arg0.sendMessage(ChatColor.YELLOW + "This camp is deserted, " + ChatColor.GOLD + "come back in " + TimeFormatter.readableTime(_timeForReactivation - System.currentTimeMillis()));
		}
		else {
			arg0.sendMessage(ChatColor.YELLOW + "This place looks like a" + ChatColor.GOLD + " barbarian camp...");	
		}
	}
	
	protected void generateLoot() {
		Location lootLocation = getRandomLocation(_lootPoints);
		double lootModifier = ScenarioService.getInstance().calculateHealthModifierWithFrontier(lootLocation, _world.getSpawnLocation());
		_loot = LootGenerator.getFightingRandomLoot(lootModifier); 
		_loot.generateLoot(lootLocation);
		
		for (Player p : _players) {
			p.sendMessage(ChatColor.YELLOW + "You survived this attack, " + ChatColor.GREEN + "take the loot" + ChatColor.YELLOW + " and get the fuck out!");
		}
	}
	
	protected Location getRandomLocation(List<PinPoint> pts) {
		if (pts.size() > 0) {
			Collections.shuffle(pts);
			return new Location(_world, _source.getX() + pts.get(0).X, _source.getY() + pts.get(0).Y, _source.getZ() + pts.get(0).Z);
		}
		return _source;
	}

	@Override
	public void playerEntered(Player arg0) {
		if (!_players.contains(arg0)) {
			_players.add(arg0);
			welcomeMessage(arg0);
		}
		
		
		
		if (_activated && !_started)  {
			_started = true;
			
			if (USE_ARTILLERY) {
				Executors.newSingleThreadScheduledExecutor().schedule(new ArtilleryShelling(_activationZone, NB_ARTILLERY_SHOT), INTERVAL_BETWEEN_WAVE - 5, TimeUnit.SECONDS);	
			}
			Executors.newSingleThreadScheduledExecutor().schedule(this, INTERVAL_BETWEEN_WAVE, TimeUnit.SECONDS);
		}
		
		
	}

	@Override
	public void playerLeft(Player arg0) {
		if (!_activated && !_started) {
			_players.remove(arg0);	
		}
		
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
	public void activateEvent() {
		_world = this._source.getWorld();
		
		for (int i = 0; i < _pinPoints.size(); i++) {
			Location l = new Location(_world, _source.getX() + _pinPoints.get(i).X, _source.getY() + _pinPoints.get(i).Y, _source.getZ() + _pinPoints.get(i).Z);
			_locations.add(l);
		}
		
		for (int i = 0; i < _zones.size(); i++) {
			List<PinPoint> zone = _zones.get(i);
			
			if (zone.size() == 2) {
				Location lower = new Location(_world, _source.getX() + zone.get(0).X, _source.getY() + zone.get(0).Y, _source.getZ() +zone.get(0).Z);
				Location higher = new Location(_world, _source.getX() + zone.get(1).X, _source.getY() + zone.get(1).Y, _source.getZ() +zone.get(1).Z);
				_activationZone = new WorldZone(lower, higher);
				ZoneServer.getInstance().addListener(this);
				_blockRestore = BlockRestore.getBlockRestoreFromWorldZone(_activationZone);
			}
		}
		
		// appliquer la config
		applyConfiguration();
		
	}
	
	protected void applyConfiguration() {
		String[] config = getConfiguration().split(",");
		if (config.length > 6) {
			NB_ARTILLERY_SHOT = Integer.parseInt(config[0]);
			USE_ARTILLERY = Boolean.parseBoolean(config[1]);
			MAX_WAVE = Integer.parseInt(config[2]);
			MONSTER_PER_LOCATION = Integer.parseInt(config[3]);
			INTERVAL_BETWEEN_ATTACK = Long.parseLong(config[4]);
			INTERVAL_BETWEEN_WAVE = Long.parseLong(config[5]);
			_monsters = config[6].split(" ");
		}
		else {
			_logger.info("Barbarian raid will use default settings");
		}
	}

	@Override
	public void releaseEvent() {
		ZoneServer.getInstance().removeListener(this);
		if (_loot != null) {
			_loot.removeLoot();	
		}
		if (_blockRestore != null) {
			_blockRestore.run();	
		}
		
	}

	@Override
	public String getName() {
		return "barbarian";
	}

	@Override
	public Server getCurrentServer() {
		return this._server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _activationZone;
	}


	@Override
	public void setWorldZone(WorldZone arg0) {
		_activationZone = arg0;
	}
	
	public void setActivate(boolean b) {
		if (_loot != null) {
			_loot.removeLoot();	
		}
		
		_waveDone = 0;
		_activated = b;
		_logger.info("setActivate: " + b);
	}
}


class EventActivator implements Runnable {

	private Logger _logger = Logger.getLogger("Minecraft");
	private BarbarianRaid _raid;
	
	public EventActivator(BarbarianRaid r) {
		_raid = r;
	}
	
	@Override
	public void run() {
		_logger.info("setActivate event activator");
		_raid.setActivate(true);
	}
	
}