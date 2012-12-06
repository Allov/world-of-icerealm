package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;

import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;

public abstract class BaseEvent implements Event {

	protected Location _source;
	protected List<PinPoint> _pinPoints;
	protected List<PinPoint> _lootPoints;
	protected List<List<PinPoint>> _zones;
	protected Server _server;

	@Override
	public abstract void setWelcomeMessage(String s);

	@Override
	public abstract void setEndMessage(String s);

	@Override
	public abstract void activateEvent();

	@Override
	public abstract void releaseEvent();

	@Override
	public abstract String getName();
	
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
	public void setActivateZone(List<List<PinPoint>> zones) {
		_zones = zones;
	}



}
