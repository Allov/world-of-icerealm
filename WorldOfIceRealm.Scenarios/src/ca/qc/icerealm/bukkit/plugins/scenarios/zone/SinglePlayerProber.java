package ca.qc.icerealm.bukkit.plugins.scenarios.zone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class SinglePlayerProber implements Runnable {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Player _player;
	private ZoneSubject _subject;
	private ScenarioZoneProber _prober;

	
	public SinglePlayerProber(Player p, ZoneSubject s, ScenarioZoneProber prober) {
		_player = p;
		_subject = s;
		_prober = prober;
	}
	
	@Override
	public void run() {
		//this.logger.info("checkgin playuer: " + _player.getName());
		try {
			Collection<ZoneObserver> observers = Collections.unmodifiableCollection(_subject.getObservers());
			
			for (ZoneObserver zone : observers) {
				
				if (zone.getWorldZone().isInside(_player.getLocation())) {
	
					if (_prober.getZoneObservers().containsKey(zone) && _prober.getZoneObservers().get(zone) != null) {
						
						if (!_prober.getZoneObservers().get(zone).contains(_player)) {
							//this.logger.info("must add plauyer");
							Executors.newSingleThreadExecutor().execute((new PlayerEnteredExecutor(zone, _player)));
							//zone.playerEntered(_player);
							_prober.getZoneObservers().get(zone).add(_player);
						}
	
					}
					else if (_prober.getZoneObservers().get(zone) == null) {
						//this.logger.info("the list of player need to be created!");
						Executors.newSingleThreadExecutor().execute((new PlayerEnteredExecutor(zone, _player)));
						//zone.playerEntered(_player);
						List<Player> list = new ArrayList<Player>();
						list.add(_player);
						_prober.getZoneObservers().put(zone, list);
					}
				}
				
				if (_prober.getZoneObservers() != null &&  _prober.getZoneObservers().get(zone) != null && _prober.getZoneObservers().get(zone).size() > 0) {
					
					List<Player> listcopy = new ArrayList<Player>();
					for (Player copy : _prober.getZoneObservers().get(zone)) {
						listcopy.add(copy);
					}
					
					for (Player p : listcopy) {
						if (!zone.getWorldZone().isInside(p.getLocation())) {
							Executors.newSingleThreadExecutor().execute((new PlayerLeftExecutor(zone, p)));
							//zone.playerLeft(p);
							//this.logger.info("Must remove player from zone");
							_prober.getZoneObservers().get(zone).remove(p);
							//this.logger.info("After remove player from zone");
						}
					}
				}
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
}
