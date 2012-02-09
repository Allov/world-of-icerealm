package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class MonsterFury extends Scenario {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private int _minimumPlayerCount = 1;
	private boolean _active = false;
	private WorldZone _activationZone;
	private MonsterFuryListener _listener;
	private List<MonsterWave> _waves;
	private int nbWaveDone = 0;
	private long _coolDown = 30000;
	private long _lastRun = 0;
	private double _radius = 3.0;
	private int _nbWave = 3;
	private int _nbMonsters = 3;
	private int _experience = 0;
	private int _money = 0;
	private double _damageModifier = 0.0;
	private double _armorIncrement = 0.0;
	private boolean _allPlayerInside = true;

	public MonsterFury(int minPlayer, long coolDown, double protectRadius, int wave, int monster, int exp, int money, double armor) {
		_minimumPlayerCount = minPlayer;
		_coolDown = coolDown;
		_radius = protectRadius;
		_nbWave = wave;
		_nbMonsters = monster;
		_experience = exp;
		_money = money;
		_armorIncrement = armor;
	}
	
	@Override
	public void terminateInit() {
		Location middle = getZone().getCentralPointAt(getZone().getRightBottom().getY());
		_activationZone = new WorldZone(middle,_radius);
	}
	
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
			getServer().getPluginManager().registerEvents(_listener, getPlugin());
		}
		
		// creation des waves
		for (int i = 0; i < _nbWave; i++) {
			MonsterWave wave = new MonsterWave(_nbMonsters, _damageModifier, this, _activationZone);
			_damageModifier += _armorIncrement;
			_waves.add(wave);
		}
		

		// on active la premiere wave et le scénario
		getServer().broadcastMessage(ChatColor.RED + "First wave is coming!!!");
		_listener.setMonsterWave(_waves.get(nbWaveDone));		
		_active = true;
	}

	@Override
	public void abortScenario() {
		// enleve les joueurs du scénario, enleve les waves
		if (_active) {
			getServer().broadcastMessage("You retreated into safety");
			getPlayers().clear();
			
			if (_waves != null) {
				for (MonsterWave wave : _waves) {
					wave.removeMonsters();
				}
				_waves.clear();
			}
		}
		
		
		_active = false;
	}

	@Override
	public boolean abortWhenLeaving() {
		// si un joueur se pousse, il est exclu, il perd tt sa progression
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		boolean coolDown = (_lastRun + _coolDown) <= System.currentTimeMillis();
		return coolDown && getPlayers().size() >= _minimumPlayerCount;
	}

	@Override
	public boolean mustBeStop() {
		return (getPlayers().size() == 0);
	}

	@Override
	public void terminateScenario() {
		// donne le XP, du health au joueur
		getServer().broadcastMessage(ChatColor.RED +"The ennemy has defeated!");
		int xp = _experience / getPlayers().size();
		for (Player p : getPlayers()) {
			p.setLevel(p.getLevel() + xp);
		}
		
		_lastRun = System.currentTimeMillis();
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		_currentWave.processDamage(event);
	}
	
}

class MonsterWave {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private int _nbMonsters = 0;
	private double _armorModifier = 0.0;
	private Set<Entity> _monstersTable;
	private MonsterFury _scenario;
	private WorldZone _exclude;
	private String[] possibleMonsters = new String[] { "zombie", "skeleton", "spider" };
	
	public MonsterWave(int qty, double armorModifier, MonsterFury s, WorldZone exclude) {
		_scenario = s;
		_monstersTable = new HashSet<Entity>();
		_nbMonsters = qty;
		_armorModifier = armorModifier;
		_exclude = exclude;
	}
	
	public void spawnWave() {
		for (int i = 0; i < _nbMonsters; i++) {
			// creation de la location et du monstre
			Location loc = _scenario.getZone().getRandomLocationOutsideThisZone(_scenario.getWorld(), _exclude);
			CreatureType type = EntityUtilities.getCreatureType(possibleMonsters[RandomUtil.getRandomInt(possibleMonsters.length)]);			
			LivingEntity living = _scenario.getWorld().spawnCreature(loc, type);
			// adding to the table
			_monstersTable.add(living);
		}
		
	}
	
	public void processEntityDeath(Entity e) {
		if (_monstersTable.contains(e)) {
			_monstersTable.remove(e);
			
			if (_monstersTable.size() == 0) {
				_scenario.waveIsDone();
			}
		}
	}
	
	public void processDamage(EntityDamageEvent e) {
		if (_monstersTable.contains(e.getEntity())) {
			
			if (e.getCause() == DamageCause.FIRE_TICK) {
				e.setCancelled(true);
			}
						
		}
	}
	
	public void removeMonsters() {
		if (_monstersTable.size() > 0) {
			 for (Entity l : _monstersTable) {
				 l.remove();
			 }
		}
	}
	

}
