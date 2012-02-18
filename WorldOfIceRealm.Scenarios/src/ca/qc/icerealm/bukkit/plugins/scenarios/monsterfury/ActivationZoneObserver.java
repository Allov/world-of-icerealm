package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;


import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class ActivationZoneObserver implements ZoneObserver {

	private final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _world;
	private Server _server;
	private MonsterFury _fury;
	private int _playerPresent;
	
	public ActivationZoneObserver(Server s, MonsterFury fury) {
		_server = s;
		_fury = fury;
		_playerPresent = 0;
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
		this.logger.info("you entering in the activation zone");
		_playerPresent++;
		if (_playerPresent >= _fury.getMinimumPlayer() && !_fury.isCoolDownActive() && !_fury.isActive()) {
			_fury.triggerScenario();
		}
		
	}

	@Override
	public void playerLeft(Player p) {
		_playerPresent--;
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return _server;
	}
}
