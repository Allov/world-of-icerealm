package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class DragonFury extends Scenario {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private String[] MONSTERS;
	private long _lastDragonDefeat = 0;
	private long _coolDown = 30000;				// 30 sec
	private boolean showRetreatMessage = true;
	private boolean isActive = false;
	private List<LivingEntity> _dragons;
	private List<LivingEntity> _ghasts;
	private List<LivingEntity> _monsters;
	private boolean isComplete;
	private int _ghastSpawningProb = 0; 	// fail safe, si la config est chié!
	private int _maxNumberOfGhast = 0; 		// fail safe, si la config est chié!
	private Integer _nbDragon;				// un seul dragon sera spawné
	private int _maxHealth = 200;			// max health par defaut
	private int _experienceDropped = 100;	// level droppé par le scénario
	private int _monsterMax = 0;
	
	
	
	public DragonFury(int ghastSpawn, int ghastMax, long coolDown, int nbDragon, 
					  int maxHealth, int experience, int monsterMax, String monster) {
		
		_ghasts = new ArrayList<LivingEntity>();
		_dragons = new ArrayList<LivingEntity>();
		_monsters = new ArrayList<LivingEntity>();
		_ghastSpawningProb = ghastSpawn;
		_maxNumberOfGhast = ghastMax;
		_coolDown = coolDown;
		_nbDragon = nbDragon;
		_maxHealth = maxHealth;
		_experienceDropped = experience;
		_monsterMax = monsterMax;
		MONSTERS = monster.split(",");
	}
	
	public void terminateScenario(long now) {
		_lastDragonDefeat = now;
		isComplete = true;
		isActive = false;
		showRetreatMessage = false;
		
		if (getPlayers().size() > 0) {
			int xpDrop = _experienceDropped / getPlayers().size();
			for (Player p : getPlayers()) {
				p.setLevel(p.getLevel() + xpDrop);
				p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.GOLD + xpDrop + " level of XP.");
			}
		}
	}
	
	@Override 
	public boolean isComplete() {
		return isComplete;
	}
	
	@Override
	public boolean isTriggered() {
		return isActive;
	}

	@Override
	public void triggerScenario() {
		this.logger.info(String.valueOf(getWorld().getFullTime()));
		isComplete = false;
		showRetreatMessage = true;
		
		// on spawn le dragon dans le milieu de la zone!
		CreatureType dragon = CreatureType.ENDER_DRAGON;
		WorldZone zone = this.getZone();
		
		for (int i = 0; i < _nbDragon; i++) {
			LivingEntity e = getWorld().spawnCreature(zone.getCentralPointAt(110), dragon);
			_dragons.add(e);
			e.setHealth(_maxHealth);
			_dragons.add(e);
		}
		
		getServer().broadcastMessage(ChatColor.RED + _nbDragon.toString() + " dragons have been awaken!");
		
		for (int i = 0; i < _maxNumberOfGhast; i++) {
			LivingEntity l = getWorld().spawnCreature(zone.getCentralPointAt(100), CreatureType.GHAST);
			_ghasts.add(l);
		}
		
		if (_ghasts.size() > 0) {
			getServer().broadcastMessage(ChatColor.RED + "Ghasts are joining the battle!");	
		}
		
		List<Location> locations = getZone().getRandomLocation(getWorld(), _monsterMax);
		for (int i = 0; i < _monsterMax; i++) {
			CreatureType type = EntityUtilities.getCreatureType(MONSTERS[RandomUtil.getRandomInt(MONSTERS.length)]);
			LivingEntity m = getWorld().spawnCreature(locations.get(i), type);
			m.setFireTicks(0);
			_monsters.add(m);
		}
		
		getServer().broadcastMessage(ChatColor.RED + "Horde of monsters is joining the battle!");	
		
		// register le onDeathListener
		getServer().getPluginManager().registerEvents(new DragonDeathListener(_dragons, this, _ghasts, _monsters), getPlugin());
		isActive = true;
	}

	@Override
	public void abortScenario() {
		
		// si les joueurs quittent, on annule le tout!
		if (_dragons != null && _dragons.size() > 0) {
			for (LivingEntity d : _dragons) {
				d.remove();	
			}
			
			
		}
		
		if (_ghasts != null && _ghasts.size() > 0) {
			for (LivingEntity l : _ghasts) {
				l.remove();
			}
		}
		
		if (_monsters != null && _monsters.size() > 0)
		{
			for (LivingEntity m : _monsters) {
				m.remove();
			}
		}
		
		if (showRetreatMessage) {
			getServer().broadcastMessage("The monsters retreated!");
		}
		
		isActive = false;
	}

	@Override
	public boolean abortWhenLeaving() {
		// on désactive le tout quand y reste pu personne
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		// vérification du cool down
		return (_lastDragonDefeat + _coolDown) <= System.currentTimeMillis();
	}

}

class DragonDeathListener implements Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private List<LivingEntity> _dragons;
	private DragonFury _scenario;
	private List<LivingEntity> _ghasts;
	private List<LivingEntity> _monsters;
	
	public DragonDeathListener(List<LivingEntity> dragons, DragonFury s, List<LivingEntity> ghasts, List<LivingEntity> monsters) {
		_dragons = dragons;
		_scenario = s;
		_ghasts = ghasts;
		_monsters = monsters;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterFireDamage(EntityDamageEvent event) {
		int entityId = event.getEntity().getEntityId();
		for (LivingEntity l : _monsters) {
			if (l.getEntityId() == entityId && event.getCause() == DamageCause.FIRE_TICK) {
				event.setCancelled(true);
				break;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDragonDeath(EntityDeathEvent event) {
		
		if (event.getEntity() instanceof EnderDragon && _dragons.size() > 0) {
			for (int i = 0; i < _dragons.size(); i++) {
				if (event.getEntity().getEntityId() == _dragons.get(i).getEntityId()) {
					event.setDroppedExp(0);
					_dragons.get(i).remove();
					_dragons.remove(i);
					
					_scenario.getServer().broadcastMessage(_dragons.size() + " dragons left!");
					break;
				}
			}
			
			if (_dragons.size() == 0) {
				_scenario.getServer().broadcastMessage(ChatColor.GREEN + "The Dragon has been defeated!");
				
				_scenario.terminateScenario(System.currentTimeMillis());
			}
		}
		else if (event.getEntity() instanceof Ghast && _ghasts.size() > 0) {
			
			for (int i = 0; i < _ghasts.size(); i++) {
				if (event.getEntity().getEntityId() == _ghasts.get(i).getEntityId()) {
					_ghasts.remove(i);
					break;
				}
			}
		}
		else if (_monsters.size() > 0){
			for (int i = 0; i < _monsters.size(); i++) {
				if (_monsters.get(i).getEntityId() == event.getEntity().getEntityId()) {
					_monsters.remove(i);
					break;
				}
			}
		}
	}

	
	@EventHandler(priority = EventPriority.NORMAL)
	public void dragonExplodeBlocks(EntityExplodeEvent event) {
		if (event.getEntity() instanceof EnderDragon) {
			event.setCancelled(true); 
		}
	}
}