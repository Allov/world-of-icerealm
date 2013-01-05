package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class ZoneTrigger implements ZoneObserver {

	private Logger _logger = Logger.getLogger("Minecraft");
	private List<MonsterSpawner> _runnable;
	private Server _server;
	private WorldZone _zone;
	
	public ZoneTrigger(List<MonsterSpawner> run, Server s) {
		_runnable = run;
		_server = s;		
		//_players = new ArrayList<Player>();
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
		// on fait spawner les monstres
		for (MonsterSpawner s : _runnable) {
			s.run();
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		
	}

	@Override
	public void setWorldZone(WorldZone arg0) {
		_zone = arg0;
	}
	
	public void setActivate(boolean a) {
		for (MonsterSpawner s : _runnable) {
			s.setActivate(a);
		}
	}

}
