package ca.qc.icerealm.bukkit.plugins.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class ZoneProber implements Runnable {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _stop = false;
	private HashMap<Player, ZoneObserver> _playersInZone;
	private HashMap<ZoneObserver, List<Player>> _zoneObservers;
	private boolean _mutex = false;
	
	public ZoneProber() {
		_playersInZone = new HashMap<Player, ZoneObserver>();
		_zoneObservers = new HashMap<ZoneObserver, List<Player>>();
	}
		
	@Override
	public void run() {
		
		while (!_stop) {
					
			// detecte l'entr�e dans une zone par un joueur
			for (ZoneObserver zone : ZoneServer.getInstance().getObservers()) {
				Player[] players = zone.getCurrentServer().getOnlinePlayers();
				
				for (Player p : players) {
					if (zone.getWorldZone().isInside(p.getLocation())) {
						
						
						
						if (_zoneObservers.containsKey(zone) && _zoneObservers.get(zone) != null) {
							
							if (!_zoneObservers.get(zone).contains(p)) {
								this.logger.info("must add plauyer");
								zone.playerEntered(p);
								_zoneObservers.get(zone).add(p);
							}

						}
						else if (_zoneObservers.get(zone) == null) {
							this.logger.info("the list of player need to be created!");
							zone.playerEntered(p);
							List<Player> list = new ArrayList<Player>();
							list.add(p);
							_zoneObservers.put(zone, list);
						}
						
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
			/*
			for (ZoneObserver obs : _zoneObservers.keySet()) {
				

			}
			
			*/
			
			/*
			// d�tecte la sortie d'un joueur d'une zone
			for (Player p : _playersInZone.keySet()) {
				ZoneObserver zone = _playersInZone.get(p);
				if (zone != null && !zone.getWorldZone().isInside(p.getLocation())) {
					zone.playerLeft(p);
					_playersInZone.remove(p);
				}
			}
			*/
			
			try {
				Thread.sleep(100);
			}
			catch (Exception ex) {
				_stop = true;
			}
		}
	}
	
	public void setStop(boolean stop) {
		_stop = stop;
	}
}
