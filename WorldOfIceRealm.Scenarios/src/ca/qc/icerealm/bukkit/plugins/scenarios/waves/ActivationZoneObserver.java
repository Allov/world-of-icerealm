package ca.qc.icerealm.bukkit.plugins.scenarios.waves;

import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class ActivationZoneObserver implements ZoneObserver {

	private final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _world;
	private Server _server;
	private boolean _canBeTrigger;
	private HashSet<Player> _players;
	private int minimumPlayers;
	private MonsterFury _fury;
	
	public ActivationZoneObserver(Server s, HashSet<Player> list, int minimum, MonsterFury fury) {
		_server = s;
		_canBeTrigger = false;
		_players = list;
		minimumPlayers = minimum;
		_fury = fury;
	}
	
	public boolean canBeTriggered() {
		this.logger.info("playrers size: " + _players.size() + " need " + minimumPlayers + " players");
		return !_fury.isCoolDownActive() && _players.size() >= minimumPlayers;
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		_world = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _world;
	}

	@Override
	public void playerEntered(Player p) {
		if (p != null && _players != null && !_players.contains(p)) {
			_players.add(p);
			if (canBeTriggered()) {
				_fury.setPlayers(_players);
				_fury.triggerScenario();
			}
		}
	}

	@Override
	public void playerLeft(Player p) {
		if (p != null && _players != null && _players.contains(p)) {
			_players.remove(p);
		}
		
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return _server;
	}
	
	

}
