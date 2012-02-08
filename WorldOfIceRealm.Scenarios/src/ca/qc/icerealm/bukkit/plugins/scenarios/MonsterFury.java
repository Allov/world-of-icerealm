package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
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
		
		if (_waves != null) {
			_waves.clear();
		}
		else {
			_waves = new ArrayList<MonsterWave>();
		}
					
		// creation des listeners
		if (_listener == null) {
			_listener = new MonsterFuryListener(this);
		}
		
		// creation des waves
		MonsterWave wave = new MonsterWave(5, 10.0, this, _activationZone);
		_waves.add(wave);
		
		// on active la premiere wave et le scénario
		_listener.setMonsterWave(_waves.get(nbWaveDone));		
		_active = true;
	}

	@Override
	public void abortScenario() {
		// enleve les joueurs du scénario, enleve les waves
		getPlayers().clear();
		if (_waves != null) {
			_waves.clear();
		}
	}

	@Override
	public boolean abortWhenLeaving() {
		// si un joueur se pousse, il est exclu, il perd tt sa progression
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		int count = getPlayers().size();
		boolean allPlayerInside = true;
		for (Player p : getPlayers()) {
			if (!_activationZone.isInside(p.getLocation())) {
				allPlayerInside = false;
				break;
			}
		}
		return (count >= _minimumPlayerCount) && allPlayerInside;
	}

	@Override
	public boolean mustBeStop() {
		return (getPlayers().size() == 0);
	}

	@Override
	public void terminateScenario() {
		// donne le XP, du health au joueur
		
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
		try {
			this.wait(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // trente seconde!!!
		
		_listener.setMonsterWave(_waves.get(nbWaveDone));
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
		
	}
	
	public void spawnWave() {
		// spawn les monstres a partir de la liste des locations
		// on les place dans le hashmap
		// lorsqu'ils sont tués, on les enleve du hashmap et on vérifie
		// combien il en reste
	}
	
	public void processEntityDeath(Entity e) {
		if (_monstersTable.containsKey(e)) {
			_monstersTable.remove(e);
			
			if (_monstersTable.size() == 0) {
				_scenario.waveIsDone();
			}
		}
	}
	

}
