package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneProber;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioServerProxy {

	private ZoneSubject _zoneServer = null;
	private static ScenarioServerProxy _instance = null;
	
	protected ScenarioServerProxy() {
		_zoneServer = new ScenarioZoneServer(Bukkit.getServer());
		ScenarioZoneProber prober = new ScenarioZoneProber(_zoneServer);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(prober, 0, 20, TimeUnit.MILLISECONDS);
	}
	
	public static ScenarioServerProxy getInstance() {
		if (_instance == null) {
			_instance = new ScenarioServerProxy();
		}
		return _instance;
	}
	
	public ZoneSubject getZoneServer() throws RuntimeException {
		if (_zoneServer != null) {
			return _zoneServer;
		}
		else {
			throw new RuntimeException("Zone server not initialised!");
		}
	}
}
