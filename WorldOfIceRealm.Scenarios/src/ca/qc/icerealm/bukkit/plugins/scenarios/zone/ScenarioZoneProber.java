package ca.qc.icerealm.bukkit.plugins.scenarios.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class ScenarioZoneProber implements Runnable {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private ZoneSubject _subject;
	private HashMap<ZoneObserver, List<Player>> _zoneObservers;
	private Player _player;
	
	public ScenarioZoneProber(ZoneSubject subject) {
		_subject = subject;
		_zoneObservers = new HashMap<ZoneObserver, List<Player>>();

	}
	
	@Override
	public void run() {
		
		for (Player player : _subject.getServer().getOnlinePlayers()) {
			for (ZoneObserver zone : _subject.getObservers()) {
	
				if (zone.getWorldZone().isInside(player.getLocation())) {
	
					if (_zoneObservers.containsKey(zone) && _zoneObservers.get(zone) != null) {
						
						if (!_zoneObservers.get(zone).contains(player)) {
							this.logger.info("must add plauyer");
							zone.playerEntered(player);
							_zoneObservers.get(zone).add(player);
						}
	
					}
					else if (_zoneObservers.get(zone) == null) {
						this.logger.info("the list of player need to be created!");
						zone.playerEntered(player);
						List<Player> list = new ArrayList<Player>();
						list.add(player);
						_zoneObservers.put(zone, list);
					}
				}
				
				if (_zoneObservers != null &&  _zoneObservers.get(zone) != null && _zoneObservers.get(zone).size() > 0) {
					
					List<Player> listcopy = new ArrayList<Player>();
					for (Player copy : _zoneObservers.get(zone)) {
						listcopy.add(copy);
					}
					
					for (Player p : listcopy) {
						if (!zone.getWorldZone().isInside(p.getLocation())) {
							zone.playerLeft(p);
							this.logger.info("Must remove player from zone");
							_zoneObservers.get(zone).remove(p);
							this.logger.info("After remove player from zone");
						}
					}
				}
			}
		}
		
			
		
	}
}
