package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.ScenarioServerProxy;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class KillingSpree extends BaseEvent {

	private Logger _logger = Logger.getLogger("Minecraft");
	private World _world;
	private List<MonsterSpawner> _spawners;
	private List<ZoneObserver> _zoneObservers;
	private String _name = "killingspree";
	private List<LivingEntity> _monsters;
	private Integer _monsterKilled = 0;
	private double _maxMonster = 0;
	private boolean _lootCreated = false;
	private long _lootDisapearInHours = 7200000; //2 heures de cooldown
	//private long _lootDisapearInHours = 60000; //2 heures de cooldown
	private Loot _loot;
	private List<Player> _players;
	private List<ZoneTrigger> _triggers;
	private long _reactivationIn = 0;
	private int _monsterKilledThreshold = 0;
	private GlobalZoneTrigger _globalTrigger;
	private double _percentNecessary = 0.8;
	private double _additionalPlayerModifier = 0.25;
	private ZoneSubject _zoneServer;
	private String _welcomeMsg;
	private String _endMessage;

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
				
				if (percentKilled > _percentNecessary && !_lootCreated) { // 80% de monstres tuées					
					generateLoot();
					this.sendEventCompleted(_players, Frontier.getInstance().calculateGlobalModifier(_source));
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
		boolean playerRemoved = _players.remove(player);

		// c'est le dernier joueur a se deconnecté, cool down devient effectif.
		if (playerRemoved && _players.size() == 0 && !_lootCreated) {
		
			Executors.newSingleThreadScheduledExecutor().schedule(new ResetKillingSpree(_loot, _globalTrigger, this), _lootDisapearInHours, TimeUnit.MILLISECONDS);
			_reactivationIn = System.currentTimeMillis() + _lootDisapearInHours;
			_globalTrigger.setCoolDown(_reactivationIn);
			_globalTrigger.setLootCreated(true);
			_globalTrigger.setStarted(false);
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void playerDies(PlayerDeathEvent event) {
		Player player = event.getEntity();
		boolean playerRemoved = _players.remove(player);
		
		// c'est le dernier joueur a mourir sans avoir réussi le scenario
		if (playerRemoved && _players.size() == 0 && !_lootCreated) {
			
			for (ZoneTrigger ob : _triggers) {
				_globalTrigger.setStarted(false);
				ob.setActivate(true);
			}
			
			for (LivingEntity m : _monsters) {
				m.remove();
			}
		}
	}
	
	private void generateLoot() {
		if (_lootPoints.size() > 0 && !_lootCreated) {
			Collections.shuffle(_lootPoints);
			PinPoint lootPt = _lootPoints.get(0);
			
			// création du loot
			Location location = new Location(_world, _source.getX() + lootPt.X, _source.getY() + lootPt.Y, _source.getZ() + lootPt.Z);
			_loot = LootGenerator.getFightingRandomLoot(Frontier.getInstance().calculateGlobalModifier(location));
			_loot.generateLoot(location);
			_lootCreated = true;

			// on part un thread lorsque le cool down est terminé. Cela reset le scenario
			Executors.newSingleThreadScheduledExecutor().schedule(new ResetKillingSpree(_loot, _globalTrigger, this), _lootDisapearInHours, TimeUnit.MILLISECONDS);
			_reactivationIn = System.currentTimeMillis() + _lootDisapearInHours;
			_globalTrigger.setCoolDown(_reactivationIn);
			_globalTrigger.setLootCreated(true);
			_globalTrigger.setStarted(false);
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
		_zoneServer = ScenarioServerProxy.getInstance().getZoneServer();
		_world = _source.getWorld();
		_spawners = new ArrayList<MonsterSpawner>();
		_zoneObservers = new ArrayList<ZoneObserver>();
		_monsters = new ArrayList<LivingEntity>();
		
		_maxMonster = _pinPoints.size();
		_monsterKilledThreshold = (int)_maxMonster / 4;
		
		for (PinPoint p : _pinPoints) {
			Location l = new Location(_source.getWorld(), _source.getX() + p.X, _source.getY() + p.Y, _source.getZ() + p.Z);
			MonsterSpawner spawner = new MonsterSpawner(l, p.Name, _monsters);
			_spawners.add(spawner);
		}
		
		
		for (List<PinPoint> points : _zones) {
			if (points.size() == 2 && !points.get(0).Name.equalsIgnoreCase("general")) {
				Location lower = new Location(_world, _source.getX() + points.get(0).X, _source.getY() + points.get(0).Y, _source.getZ() + points.get(0).Z);
				Location higher = new Location(_world, _source.getX() + points.get(1).X, _source.getY() + points.get(1).Y, _source.getZ() + points.get(1).Z);
				
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
					_zoneObservers.add(trigger);
					_triggers.add(trigger);
					_zoneServer.addListener(trigger);
				}
			}
			
		}
		
		// on pogne la config!
		if (getConfiguration() != null && !getConfiguration().equalsIgnoreCase("")) {
			String configData[] = getConfiguration().split(",");
			if (configData.length > 1) {
				try {
					_percentNecessary = Double.parseDouble(configData[0]);
					_additionalPlayerModifier = Double.parseDouble(configData[1]);
					
					if (configData.length > 2) {
						_welcomeMsg = configData[2];
					}
					
					if (configData.length > 3) {
						_endMessage = configData[3];
					}
				}
				catch (Exception ex) {
					_percentNecessary = 0.8;
					_additionalPlayerModifier = 0.25;
				}
			}
		}
		
		for (List<PinPoint> points : _zones) {
			if (points.size() == 2 && points.get(0).Name.equalsIgnoreCase("general")) {
				Location lower = new Location(_world, _source.getX() + points.get(0).X, _source.getY() + points.get(0).Y, _source.getZ() + points.get(0).Z);
				Location higher = new Location(_world, _source.getX() + points.get(1).X, _source.getY() + points.get(1).Y, _source.getZ() + points.get(1).Z);
				
				WorldZone zone = new WorldZone(lower, higher);
				_globalTrigger = new GlobalZoneTrigger(_triggers, _server, _percentNecessary, _additionalPlayerModifier);
				_globalTrigger.setWorldZone(zone);
				_globalTrigger.setEntities(_monsters);
				_globalTrigger.setPlayerList(_players);
				
				if (_welcomeMsg != null && _welcomeMsg != "") {
					_globalTrigger.setStartMessage(_welcomeMsg);
				}
				
				if (_endMessage != null && _endMessage != "") {
					_globalTrigger.setEndMessage(_endMessage);
				}
				
				_zoneObservers.add(_globalTrigger);
				_zoneServer.addListener(_globalTrigger);
			}
		}
		
		
	}

	@Override
	public void releaseEvent() {
		for (ZoneObserver ob : _zoneObservers) {
			_zoneServer.removeListener(ob);
		}
		for (LivingEntity l : _monsters) {
			l.remove();
		}
	}

	@Override
	public String getName() {
		return _name;
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
	private GlobalZoneTrigger _trigger;
	private KillingSpree _spree;
	
	
	public ResetKillingSpree(Loot loot, GlobalZoneTrigger trigger, KillingSpree ks) {
		_loot = loot;
		_trigger = trigger;
		_spree = ks;
	}

	@Override
	public void run() {
		
		try {
			
			// enleve le loot si présent
			if (_loot != null) {
				_loot.removeLoot();
			}	
			
			_trigger.setStarted(false);
			_trigger.setActivate(true);
			_trigger.setLootCreated(false);
						
			// on fait le ménage dans les stats
			_spree.clearMonsterKilled();
		}
		catch (NullPointerException ex) {
			// on etouffe l'exception
		}
		
	}
}
