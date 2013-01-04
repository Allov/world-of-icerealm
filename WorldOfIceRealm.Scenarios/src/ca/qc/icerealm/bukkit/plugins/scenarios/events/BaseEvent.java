package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.BlockRestore;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.ScenarioServerProxy;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.TimeFormatter;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public abstract class BaseEvent implements Event, Runnable {

	private final Logger _logger = Logger.getLogger("Minecraft");
	protected Location _source;
	protected List<PinPoint> _pinPoints;
	protected List<PinPoint> _lootPoints;
	protected List<List<PinPoint>> _zones;
	protected Server _server;
	protected String _config;
	private List<EventListener> _eventListener;
	protected int _height;
	protected int _row;
	protected int _col;
	protected List<LivingEntity> _livingEntities = new ArrayList<LivingEntity>();
	protected List<ZoneObserver> _worldZones = new ArrayList<ZoneObserver>();
	protected boolean _coolDownActive = false;
	protected long _timeBeforeReactivation = 0;
	protected List<Player> _players = new ArrayList<Player>();
	
	protected abstract long getCoolDownInterval();
	
	protected abstract void resetEvent();
	
	@Override
	public void setWelcomeMessage(String s) {		
	}

	@Override
	public void setEndMessage(String s) {
	}
	
	@Override
	public Location getSourceLocation() {
		return _source;
	}

	@Override
	public abstract void activateEvent();

	@Override
	public abstract void releaseEvent();

	@Override
	public abstract String getName();
	
	@Override
	public void addEventListener(EventListener l) {
		if (_eventListener == null) {
			_eventListener = new ArrayList<EventListener>();
		}
		
		_eventListener.add(l);
	}
	
	protected List<EventListener> getListeners() {
		return _eventListener != null ? _eventListener : new ArrayList<EventListener>(); 
	}
	
	protected void sendEventCompleted(List<Player> player, double modifier) {
		for (EventListener event : getListeners()) {
			event.eventCompleted(player, modifier);
		}
	}
	
	@Override
	public void setSourceLocation(Location source) {
		_source = source;
	}

	@Override
	public void setPinPoints(List<PinPoint> points) {
		_pinPoints = points;
	}

	@Override
	public void setLootPoints(List<PinPoint> loots) {
		_lootPoints = loots;
	}
	
	@Override
	public void setServer(Server s) {
		_server = s;
	}
	
	@Override
	public String getConfiguration() {
		return _config != null ? _config : "";
	}
	
	@Override
	public void setConfiguration(String config) {
		_config = config;
	}
	
	@Override
	public void setActivateZone(List<List<PinPoint>> zones) {
		_zones = zones;
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		boolean removed = _players.remove(event.getPlayer());
		if (removed && _players.size() == 0) {
			activateCoolDown(getCoolDownInterval());
			resetEvent();
		}
		
		despawnLivingEntity(_livingEntities);
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerDies(PlayerDeathEvent event) {
		boolean removed = _players.remove(event.getEntity());
		if (removed && _players.size() == 0) {
			resetEvent();
		}
		
		despawnLivingEntity(_livingEntities);
	}
	
	public void sendMessageToPlayers(String msg) {
		for (Player p : _players) {
			p.sendMessage(msg);
		}
	}
	

	@Override
	public void setEventArea(int high, int row, int col) {
		_height = high;
		_row = row;
		_col = col;
	}
	
	@Override
	public void run() {
		_logger.info("disabling cooldown for event " + getName());
		_coolDownActive = false;
		for (LivingEntity e : _livingEntities) {
			e.remove();
		}	
	}
	
	public void activateCoolDown(long cooldown) {
		_logger.info("enabling cooldown for event " + getName() + " for " + TimeFormatter.readableTime(cooldown));
		_coolDownActive = true;
		_timeBeforeReactivation = cooldown + System.currentTimeMillis();
		Executors.newSingleThreadScheduledExecutor().schedule(this, cooldown, TimeUnit.MILLISECONDS);
	}
	
	protected void addLivingEntity(LivingEntity e) {
		_livingEntities.add(e);
	}
	
	protected void despawnLivingEntity(List<LivingEntity> l) {
		for (LivingEntity e : l) {
			e.remove();
		}
	}
	
	protected void addZoneObserver(ZoneObserver e) {
		_worldZones.add(e);
	}
	
	protected List<WorldZone> getWorldZoneByName(String name) {
		List<List<PinPoint>> pins = getPinPointsByName(name);
		return transformIntoLocations(pins);
	}
	
	protected List<WorldZone> transformIntoLocations(List<List<PinPoint>> pins) {
		List<WorldZone> worldZones = new ArrayList<WorldZone>();
		
		if (_source != null) {
			for (int i = 0; i < pins.size(); i++) {
				List<PinPoint> zone = pins.get(i);
				
				if (zone.size() == 2) {
					Location lower = new Location(_source.getWorld(), _source.getX() + zone.get(0).X, _source.getY() + zone.get(0).Y, _source.getZ() + zone.get(0).Z);
					Location higher = new Location(_source.getWorld(), _source.getX() + zone.get(1).X, _source.getY() + zone.get(1).Y, _source.getZ() + zone.get(1).Z);
					_logger.info(_source.getY() + " " + zone.get(0).Y + ", " +  _source.getY() + " " + zone.get(1).Y + " tranformed into locations");
					
					WorldZone z = new WorldZone(lower, higher, lower.getY(), higher.getY());
					worldZones.add(z);
					
				}
			}
		}
		
		return worldZones;
	}
	
	protected List<List<PinPoint>> getPinPointsByName(String name) {
		List<List<PinPoint>> list = new ArrayList<List<PinPoint>>();
		if (_zones != null) {
			for (List<PinPoint> l : _zones) {
				if (l.size() > 1 && l.get(0).Name.equalsIgnoreCase(name)) {
					list.add(l);
				}
			}
		}
		return list;
	}
	
	
	
	protected ZoneSubject getZoneSubjectInstance() {
		return ScenarioServerProxy.getInstance().getZoneServer();
	}
	
	protected WorldZone getGeneralZone() {
	
		for (List<PinPoint> zone : _zones) {
			if (zone.size() == 2 && zone.get(0).Name.equalsIgnoreCase("general")) {
				Location lower = new Location(_source.getWorld(), _source.getX() + zone.get(0).X, _source.getY() + zone.get(0).Y, _source.getZ() + zone.get(0).Z);
				Location higher = new Location(_source.getWorld(), _source.getX() + zone.get(1).X, _source.getY() + zone.get(1).Y, _source.getZ() + zone.get(1).Z);
				return new WorldZone(lower, higher, lower.getY(), higher.getY());
			}
		}
		return null;
	}
	
	protected WorldZone getAutomaticGeneralZone() {
		Location lower = new Location(_source.getWorld(), _source.getX(), _source.getY(), _source.getZ());
		Location higher = new Location(_source.getWorld(), _source.getX() + _col, _source.getY() + _height, _source.getZ() + _row);
		return new WorldZone(lower, higher, lower.getY(), higher.getY());
	}
	
	protected List<Location> transformPinIntoLocations(List<PinPoint> pins) {
		List<Location> locations = new ArrayList<Location>();
		
		for (PinPoint pin : pins) {
			locations.add(new Location(_source.getWorld(), _source.getX() + pin.X, _source.getY() + pin.Y, _source.getZ() + pin.Z));
		}
		
		return locations;
	}
	
	
	
	
	

}
