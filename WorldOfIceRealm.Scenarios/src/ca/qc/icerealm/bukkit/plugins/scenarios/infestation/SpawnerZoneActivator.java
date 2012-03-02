package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.spawners.ProximitySpawner;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class SpawnerZoneActivator implements ZoneObserver {
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
		ScenarioPlugin.logger.fine(p.getName() + " enter spawner zone " +  _spawner.getLocation().getX() + "," + _spawner.getLocation().getY() + _spawner.getLocation().getZ() + " cooldown: " + _spawner.isCoolDownActive());
		_spawner.setTarget(p);
		if (_countPlayer == 0) {		
			_spawner.setPlayerAround(true);
			_spawner.timeHasCome(System.currentTimeMillis());
		}
		
		_countPlayer++;
		
	}

	@Override
	public void playerLeft(Player p) {
		_countPlayer--;
		if (_countPlayer == 0) {
			_spawner.setPlayerAround(false);
			_spawner.moveSpawnerToAnotherLocation();
			
		}
		ScenarioPlugin.logger.fine(p.getName() + " leaves a spawner zone " +  _spawner.getLocation().getX() + "," + _spawner.getLocation().getY() + _spawner.getLocation().getZ() + " cooldown: " + _spawner.isCoolDownActive());
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}

}
