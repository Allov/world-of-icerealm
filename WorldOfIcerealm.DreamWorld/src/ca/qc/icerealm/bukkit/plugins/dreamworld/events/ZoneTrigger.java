package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class ZoneTrigger implements ZoneObserver {

	private List<Runnable> _runnable;
	private Server _server;
	private WorldZone _zone;
	private boolean _started = false; 
	
	public ZoneTrigger(List<Runnable> run, Server s) {
		_runnable = run;
		_server = s;		
	}
	
	@Override
	public Server getCurrentServer() {
		return _server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player arg0) {
		if (!_started) {
			for (Runnable r : _runnable) {
				r.run();
			}
			_started = true;
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		// nothing to do
	}

	@Override
	public void setWorldZone(WorldZone arg0) {
		_zone = arg0;
	}

}
