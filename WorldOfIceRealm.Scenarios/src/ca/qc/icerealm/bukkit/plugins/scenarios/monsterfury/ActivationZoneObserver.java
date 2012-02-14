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
	
	public ActivationZoneObserver(Server s, MonsterFury fury) {
		_server = s;
		_fury = fury;
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
		if (!_fury.isActive() && !_fury.isCoolDownActive()) {
			_fury.addPlayerToScenario(p);	
		}
		
	}

	@Override
	public void playerLeft(Player p) {
		if (!_fury.isActive()) {
			_fury.removePlayerFromScenario(p);
		}
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return _server;
	}
}
