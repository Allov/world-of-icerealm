package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class MonsterFury extends Scenario {

	private int _minimumPlayerCount = 1;
	private boolean _active = false;
	private WorldZone _activationZone;
	private MonsterFuryListener _listener;
	private List<MonsterWave> _waves;
	private int nbWaveDone = 0;
	
	@Override
	public boolean isTriggered() {
		return _active;
	}

	@Override
	public void triggerScenario() {
		getServer().broadcastMessage("First wave is coming!!!");
		_activationZone = getZone();
		
		if (_waves != null) {
			_waves.clear();
		}
		else {
			_waves = new ArrayList<MonsterWave>();
		}
					
		// creation des listeners
		if (_listener == null) {
			_listener = new MonsterFuryListener(this);
			getServer().getPluginManager().registerEvents(_listener, getPlugin());
		}
		
		// creation des waves
		MonsterWave wave = new MonsterWave(5, 10.0, this, _activationZone);
		MonsterWave wave1 = new MonsterWave(5, 10.0, this, _activationZone);
		MonsterWave wave2 = new MonsterWave(5, 10.0, this, _activationZone);
		_waves.add(wave);
		_waves.add(wave1);
		_waves.add(wave2);
		
		// on active la premiere wave et le scénario
		_listener.setMonsterWave(_waves.get(nbWaveDone));		
		_active = true;
	}

	@Override
	public void abortScenario() {
		// enleve les joueurs du scénario, enleve les waves
		getServer().broadcastMessage("You retreated into safety");
		getPlayers().clear();
		
		for (MonsterWave wave : _waves) {
			wave.removeMonsters();
		}
		_waves.clear();

		_active = false;
	}

	@Override
	public boolean abortWhenLeaving() {
		// si un joueur se pousse, il est exclu, il perd tt sa progression
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		return true;
		/*
		int count = getPlayers().size();
		boolean allPlayerInside = true;
		for (Player p : getPlayers()) {
			if (!_activationZone.isInside(p.getLocation())) {
				allPlayerInside = false;
				break;
			}
		}
		return (count >= _minimumPlayerCount) && allPlayerInside;*/
	}

	@Override
	public boolean mustBeStop() {
		return (getPlayers().size() == 0);
	}

	@Override
	public void terminateScenario() {
		// donne le XP, du health au joueur
		getServer().broadcastMessage("The ennemy has defeated!");
		
		for (Player p : getPlayers()) {
			p.giveExp(1000);
		}
		
		nbWaveDone = 0;
		_active = false;
	}

	@Override
	public void progressHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void waveIsDone() {
		// on load la prochaine wave!
		nbWaveDone++;
		
		
		if (nbWaveDone < _waves.size()) {	
			getServer().broadcastMessage("Another wave is coming!!!");
			_listener.setMonsterWave(_waves.get(nbWaveDone));
		}
		else {
			terminateScenario();
		}
	}

}

class MonsterFuryListener implements Listener {
	
	private MonsterWave _currentWave;
	private MonsterFury _scenario;
	
	public MonsterFuryListener(MonsterFury s) {
		_scenario = s;
	}
	
	public void setMonsterWave(MonsterWave wave) {
		_currentWave = wave;
		_currentWave.spawnWave();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			try {
				_scenario.getPlayers().remove((Player)entity);	
			}
			catch (Exception ex) { }
		}
		else {
			_currentWave.processEntityDeath(entity);
		}
		
		
	}
	
}

class MonsterWave {
	
	private int _nbMonsters = 0;
	private double healthModifier = 0.0;
	private HashMap<LivingEntity, Location> _monstersTable;
	private List<Location> _monstersLocation;
	private MonsterFury _scenario;
	private WorldZone _exclude;
	
	public MonsterWave(int qty, double healthModifier, MonsterFury s, WorldZone exclude) {
		_scenario = s;
		_monstersTable = new HashMap<LivingEntity, Location>();
	}
	
	public void spawnWave() {
		// spawn les monstres a partir de la liste des locations
		// on les place dans le hashmap
		// lorsqu'ils sont tués, on les enleve du hashmap et on vérifie
		// combien il en reste
		List<Location> l = _scenario.getZone().getRandomLocation(_scenario.getWorld(), 1);
		LivingEntity en = _scenario.getWorld().spawnCreature(l.get(0), CreatureType.ZOMBIE);
		_monstersTable.put(en, en.getLocation());
	}
	
	public void processEntityDeath(Entity e) {
		if (_monstersTable.containsKey(e)) {
			_monstersTable.remove(e);
			
			if (_monstersTable.size() == 0) {
				_scenario.waveIsDone();
			}
		}
	}
	
	public void removeMonsters() {
		if (_monstersTable.size() > 0) {
			 for (LivingEntity l : _monstersTable.keySet()) {
				 l.remove();
			 }
		}
	}
	

}
