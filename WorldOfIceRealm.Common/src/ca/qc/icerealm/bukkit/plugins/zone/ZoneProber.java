package ca.qc.icerealm.bukkit.plugins.zone;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class ZoneProber implements Runnable {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _stop = false;
	private HashMap<Player, ZoneObserver> _playersInZone;
	
	public ZoneProber() {
		_playersInZone = new HashMap<Player, ZoneObserver>();
	}
		
	@Override
	public void run() {
		
		while (!_stop) {
					
			// detecte l'entrée dans une zone par un joueur
			for (ZoneObserver zone : ZoneServer.getInstance().getObservers()) {
				Player[] players = zone.getCurrentServer().getOnlinePlayers();
				
				for (Player p : players) {
				
					if (zone.getWorldZone().isInside(p.getLocation()) && !_playersInZone.containsKey(p)) {
						zone.playerEntered(p);
						_playersInZone.put(p, zone);
						this.logger.info(String.valueOf((_playersInZone.size())));
					}
				}
			}
			
			// détecte la sortie d'un joueur d'une zone
			for (Player p : _playersInZone.keySet()) {
				ZoneObserver zone = _playersInZone.get(p);
				if (zone != null && !zone.getWorldZone().isInside(p.getLocation())) {
					zone.playerLeft(p);
					_playersInZone.remove(p);
					this.logger.info(String.valueOf((_playersInZone.size())));
				}
			}
			
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
