package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.BlockContainer;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.BlockRestore;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class BarbarianRaid extends BaseEvent implements Runnable, ZoneObserver {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final int MAX_WAVE = 1;
	private final int MONSTER_PER_LOCATION = 1;
	private final long INTERVAL_BETWEEN_ATTACK = 10; // 2 heures
	private final long INTERVAL_BETWEEN_WAVE = 10; // 10 secondes;
	
	private List<BlockRestore> _restoreBlocks;
	private List<Location> _locations;
	private String[] _monsters = new String[] { "zombie" };
	private int _waveDone = 0;
	private World _world;
	private HashSet<Integer> _monstersContainer;
	private WorldZone _activationZone;
	private List<Player> _players;
	private boolean _activated;
	private boolean _started;
	private Loot _loot;
		
	public BarbarianRaid() {
		_monstersContainer = new HashSet<Integer>();
		_locations = new ArrayList<Location>();
		_players = new ArrayList<Player>();
		_activated = true;
		_started = false;
		_restoreBlocks = new ArrayList<BlockRestore>();
	}
		
	@Override
	public void run() {
		if (_activated) {
			for (Location loc : _locations) {
				String monster = _monsters[RandomUtil.getRandomInt(_monsters.length)];
				double modifier = ScenarioService.getInstance().calculateHealthModifierWithFrontier(loc, _world.getSpawnLocation()) * (_waveDone + 1);
				
				for (int i = 0; i < MONSTER_PER_LOCATION; i++) {
					Entity e = ScenarioService.getInstance().spawnCreature(_world, loc, EntityUtilities.getEntityType(monster), modifier, false);
					_monstersContainer.add(e.getEntityId());
					
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
	public void monsterDies(EntityDeathEvent event) {
		processKill(event.getEntity().getEntityId());
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void monsterDies(EntityExplodeEvent event) {
		processKill(event.getEntity().getEntityId());
		
		if (_activationZone.isInside(event.getEntity().getLocation())) {
			HashMap<Location, BlockContainer> _blocks = new HashMap<Location, BlockContainer>();
			for (Block b : event.blockList()) {
				BlockContainer bc = new BlockContainer();
				bc.TypeId = b.getTypeId();
				bc.TypeData = b.getData();
				_blocks.put(b.getLocation(), bc);
			}
			
			_restoreBlocks.add(new BlockRestore(_world, _blocks));
		}
	}
	
	private void processKill(int id) {
		
		if (_monstersContainer.contains(id)) {
			_monstersContainer.remove(id);
			
			if (_monstersContainer.size() == 0) {
				_waveDone++;
				
				for (Player p : _players) {
					p.sendMessage(ChatColor.GREEN + "This wave has been wiped out!");
				}
				
				if (_waveDone < MAX_WAVE) {
					Executors.newSingleThreadScheduledExecutor().schedule(this, INTERVAL_BETWEEN_WAVE, TimeUnit.SECONDS);
					for (Player p : _players) {
						p.sendMessage(ChatColor.RED + "Another wave is coming. " + ChatColor.YELLOW + " They look stronger!");
					}
				}
				else {
					for (Player p : _players) {
						p.sendMessage(ChatColor.GREEN + "You survived this attack, take the loot and " + ChatColor.YELLOW + "get the fuck out!");
					}
					
					_loot = LootGenerator.getRandomLoot(1.0); 
					_loot.generateLoot(getRandomLocation(_lootPoints));	
					
					_activated = false;
					_started = false;
					Executors.newSingleThreadScheduledExecutor().schedule(new EventActivator(this), INTERVAL_BETWEEN_ATTACK, TimeUnit.SECONDS);
					
					for (BlockRestore restore : _restoreBlocks) {
						Executors.newSingleThreadScheduledExecutor().schedule(restore, INTERVAL_BETWEEN_ATTACK, TimeUnit.SECONDS);
					}
				}
			}
		}
		
		
	}
	
	private Location getRandomLocation(List<PinPoint> pts) {
		if (pts.size() > 0) {
			Collections.shuffle(pts);
			return new Location(_world, _source.getX() + pts.get(0).X, _source.getY() + pts.get(0).Y, _source.getZ() + pts.get(0).Z);
		}
		return _source;
	}
	

	@Override
	public void playerEntered(Player arg0) {
		_players.add(arg0);
		arg0.sendMessage("You are in a barbarian camp!!!");
		if ( _activated && !_started)  {
			_started = true;
			Executors.newSingleThreadScheduledExecutor().schedule(this, INTERVAL_BETWEEN_WAVE, TimeUnit.SECONDS);
		}
		
		if (!_activated) {
			arg0.sendMessage(ChatColor.YELLOW + "This camp is deserted, " + ChatColor.GOLD + "come back later.");
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		_players.remove(arg0);
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
			}
		}
		
	}

	@Override
	public void releaseEvent() {
		ZoneServer.getInstance().removeListener(this);
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
		// on fait disparaitre le coffre de loot ici, c'est un reset ici
		_loot.removeLoot();
		_waveDone = 0;
		_activated = b;
	}

}

class EventActivator implements Runnable {

	private BarbarianRaid _raid;
	
	public EventActivator(BarbarianRaid r) {
		_raid = r;
	}
	
	@Override
	public void run() {
		_raid.setActivate(true);
	}
	
}