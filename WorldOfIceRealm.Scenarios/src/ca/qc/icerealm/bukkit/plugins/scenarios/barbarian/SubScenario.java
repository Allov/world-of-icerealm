package ca.qc.icerealm.bukkit.plugins.scenarios.barbarian;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class SubScenario {
	
	private WorldZone _spawnZone;
	private WorldZone _activation;
	private World _world;	
	private Server _server;
	private boolean _isActive;
	private int _quantity;
	private EntityType _creature;
	private ZoneObserver _activationObserver;
	
	public SubScenario(Server s, World w, WorldZone scenario, WorldZone activation, int qty, EntityType t) {
		_server = s;
		_spawnZone = scenario;
		_activation = activation;
		_world = w;
		_quantity = qty;
		_creature = t;
		_isActive = false;
		
		if (_activation != null) {
			//_activationObserver = new ActivationZone(this, _activation);
		}
	}
	
	public boolean isActive() {
		return _isActive;
	}
	
	public void reset() {
		_isActive = false;
	}
		
	public void trigger() {
		if (!_isActive) {
			_isActive = true;
			List<Location> loc = _spawnZone.getRandomLocation(_world, _quantity);
			for (Location l : loc) {
				ScenarioService.getInstance().spawnCreature(_world, l, _creature);
			}	
		}
	}
	
	public Server getServer() {
		return _server;
	}
	
}
