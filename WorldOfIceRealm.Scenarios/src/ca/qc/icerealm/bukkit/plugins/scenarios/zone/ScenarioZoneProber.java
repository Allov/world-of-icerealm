package ca.qc.icerealm.bukkit.plugins.scenarios.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioZoneProber implements Runnable {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private ZoneSubject _subject;
	private HashMap<ZoneObserver, List<Player>> _zoneObservers;
	
	public ScenarioZoneProber(ZoneSubject subject) {
		_subject = subject;
		_zoneObservers = new HashMap<ZoneObserver, List<Player>>();
	}
	
	public synchronized HashMap<ZoneObserver, List<Player>> getZoneObservers() {
		return _zoneObservers;
	}
	
	@Override
	public void run() {
		Timer t = new Timer(true);
		for (Player player : _subject.getServer().getOnlinePlayers()) {
			Executors.newSingleThreadExecutor().execute(new SinglePlayerProber(player, _subject, this));
		}
		t.finish();
		//logger.info("prob: " + t.getResult() + " ms");

	}
}
