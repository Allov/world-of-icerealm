package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.Server;

import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.ScenarioServerProxy;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public abstract class BaseEvent implements Event {

	protected Location _source;
	protected List<PinPoint> _pinPoints;
	protected List<PinPoint> _lootPoints;
	protected List<List<PinPoint>> _zones;
	protected Server _server;
	protected String _config;
	private ZoneSubject _zoneServer;

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
	
	@Override
	public String getConfiguration() {
		return _config != null ? _config : "";
	}
	
	@Override
	public void setConfiguration(String config) {
		_config = config;
	}
	
	protected ZoneSubject getZoneSubjectInstance() {
		return ScenarioServerProxy.getInstance().getZoneServer();
	}



}
