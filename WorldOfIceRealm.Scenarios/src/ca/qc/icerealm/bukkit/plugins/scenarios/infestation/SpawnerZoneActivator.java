package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.spawners.ProximitySpawner;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class SpawnerZoneActivator implements ZoneObserver {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _zone;
	private Server _server;
	private ProximitySpawner _spawner;
	private int _countPlayer = 0;
	private InfestationConfiguration _config;
	
	
	public SpawnerZoneActivator(WorldZone z, Server s, ProximitySpawner spawn, InfestationConfiguration config) {
		_zone = z;
		_server = s;
		_spawner = spawn;
		_config = config;
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		_spawner.setTarget(p);
		if (_countPlayer == 0) {		
			_spawner.setPlayerAround(true);
			_spawner.timeHasCome(System.currentTimeMillis());
		}
		
		_countPlayer++;
		ScenarioPlugin.logger.fine(p.getName() + " enter spawner zone");
	}

	@Override
	public void playerLeft(Player p) {
		_countPlayer--;
		if (_countPlayer == 0) {
			_spawner.setPlayerAround(false);
			_spawner.moveSpawnerToAnotherLocation();
			
		}
		ScenarioPlugin.logger.fine(p.getName() + " leaves a spawner zone");
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}

}
