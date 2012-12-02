package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class KillingSpree implements Event {

	private World _world;
	private Location _location;
	private HashMap<String, MonsterSpawner> _spawners;
	private List<ZoneObserver> _zoneObservers;
	private Server _server;
	private String _name;
	
	@Override
	public void setSourceLocation(Location source) {
		// TODO Auto-generated method stub
		_location = source;
		_world = source.getWorld();
		_spawners = new HashMap<String, MonsterSpawner>();
		_zoneObservers = new ArrayList<ZoneObserver>();
	}
	
	@Override
	public void setPinPoints(List<PinPoint> points) {
		
		for (PinPoint p : points) {
			Location l = new Location(_location.getWorld(), _location.getX() + p.X, _location.getY() + p.Y, _location.getZ() + p.Z);
			MonsterSpawner spawner = new MonsterSpawner(l);
			_spawners.put(p.Name, spawner);
		}
		
		
	}

	@Override
	public void setLootPoints(List<PinPoint> loots) {
		
	}

	@Override
	public void setActivateZone(List<List<PinPoint>> zones) {
		
		for (List<PinPoint> points : zones) {
			if (points.size() == 2) {
				Location lower = new Location(_world, _location.getX() + points.get(0).X, _location.getY() + points.get(0).Y, _location.getZ() + points.get(0).Z);
				Location higher = new Location(_world, _location.getX() + points.get(1).X, _location.getY() + points.get(1).Y, _location.getZ() + points.get(1).Z);
				
				String name = points.get(0).Name;
				WorldZone zone = new WorldZone(lower, higher);
				
				Runnable spawner =_spawners.get(name);
				
				if (spawner != null && _server != null) {
					ZoneTrigger trigger = new ZoneTrigger(spawner, _server);
					trigger.setWorldZone(zone);
					_zoneObservers.add(trigger);
					ZoneServer.getInstance().addListener(trigger);
				}
			}			
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
	public void setServer(Server s) {
		_server = s;
	}

	@Override
	public void activateEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return _name;
	}
}
