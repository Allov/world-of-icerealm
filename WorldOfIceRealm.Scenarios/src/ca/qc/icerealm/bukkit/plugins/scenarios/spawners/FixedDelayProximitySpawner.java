package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class FixedDelayProximitySpawner implements Runnable, ZoneObserver {

	private long _cooldownInterval = 60000; // 1 minute
	private int _radius = 10;
	private int _nbOfMonsters = 5;
	private Location _location; 
	private Server _server;	
	private boolean _coolDownActive = false;
	private WorldZone _activationZone;
	private String[] _monsters = { "zombie", "spider", "skeleton" }; // le set de monstres par defaut
	private List<LivingEntity> _monstersList;
	private double modifier = 1.0;
	
	
	public FixedDelayProximitySpawner(Server s) {
		_server = s;
		_monstersList = new ArrayList<LivingEntity>();
	}
	
	public void setConfiguration(String config) {
		// on parse la string!
		
	}
	
	public void setLocation(Location l) {
		_location = l;
		modifier = Frontier.getInstance().calculateGlobalModifier(l);
	}

	@Override
	public void run() {
		_coolDownActive = false;
	}

	@Override
	public void setWorldZone(WorldZone z) {
		_activationZone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _activationZone;
	}

	@Override
	public void playerEntered(Player p) {
		if (!_coolDownActive && _location != null) {
			_coolDownActive = true;
			World w = _location.getWorld();
			
			// on spawn les mosntres!
			for (int i = 0; i < _nbOfMonsters; i++) {
				LivingEntity monster = (LivingEntity) ScenarioService.getInstance().spawnCreature(w, _location, pickRandomMonster(), modifier, false);
				_monstersList.add(monster);
			}
			
			// on reactive ce spawner la!
			Executors.newSingleThreadScheduledExecutor().schedule(this, _cooldownInterval, TimeUnit.MILLISECONDS);			
		}
	}

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	private String[] parseMonsters(String m) {
		String[] parsed = m.split("+");
		if (parsed.length > 0) {
			return parsed;
		}
		return new String[] { "zombie", "spider", "skeleton" };
	}
	
	private EntityType pickRandomMonster() {
		return EntityUtilities.getEntityType(_monsters[RandomUtil.getRandomInt(_monsters.length)]);
	}
}
