package ca.qc.icerealm.bukkit.plugins.scenarios.zone;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioZoneServer implements ZoneSubject, Runnable {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Server _server;
	private List<ZoneObserver> _observers;

	public ScenarioZoneServer(Server s) {
		_server = s;
		_observers = new ArrayList<ZoneObserver>();
	}
	
	@Override
	public void addListener(ZoneObserver obs) {
		if (obs != null) {
			_observers.add(obs);
			logger.info("add observer sc zone server");
		}
	}

	@Override
	public void removeListener(ZoneObserver obs) {
		if (obs != null && _observers.contains(obs)) {
			_observers.remove(obs);
			logger.info("remove observer sc zone server");
		}
	}

	@Override
	public void stopListening() {

	}

	@Override
	public void startListening() {

	}
	
	
	
	@Override
	public void run() {
				
	}

	@Override
	public List<ZoneObserver> getObservers() {
		return _observers;
	}

	@Override
	public Server getServer() {
		return _server;
	}

	
	
}
