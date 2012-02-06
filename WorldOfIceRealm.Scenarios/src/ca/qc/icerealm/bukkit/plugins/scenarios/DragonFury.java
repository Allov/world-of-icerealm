package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class DragonFury extends Scenario {

	private long _lastDragonDefeat = 0;
	private long _coolDown;
	private boolean showRetreatMessage = true;
	private boolean isActive = false;
	private LivingEntity _theDragon;
	private List<LivingEntity> _ghasts;
	private boolean isComplete;
	private int _ghastSpawningProb = 0; // fail safe, si la config est chié!
	private int _maxNumberOfGhast = 0; // fail safe, si la config est chié!
	
	public DragonFury(int ghastSpawn, int ghastMax, long coolDown) {
		_ghasts = new ArrayList<LivingEntity>();
		_ghastSpawningProb = ghastSpawn;
		_maxNumberOfGhast = ghastMax;
		_coolDown = coolDown;
	}
	
	public void setLastDragonDefeat(long now) {
		_lastDragonDefeat = now;
		isComplete = true;
		isActive = false;
		showRetreatMessage = false;
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
		
		isComplete = false;
		showRetreatMessage = true;
		
		// on spawn le dragon dans le milieu de la zone!
		CreatureType dragon = CreatureType.ENDER_DRAGON;
		WorldZone zone = this.getZone();
		_theDragon = getWorld().spawnCreature(zone.getCentralPointAt(80), dragon);
		getServer().broadcastMessage(ChatColor.RED + "The Dragon has been awaken!");
		
		if (RandomUtil.getDrawResult(_ghastSpawningProb)) {
			CreatureType ghast = CreatureType.GHAST;
			int nbGhast = RandomUtil.getRandomInt(_maxNumberOfGhast);
			for (int i = 0; i < nbGhast; i++) {
				LivingEntity l = getWorld().spawnCreature(zone.getCentralPointAt(70), ghast);
				_ghasts.add(l);
			}
			
			if (_ghasts.size() > 0) {
				getServer().broadcastMessage(ChatColor.RED + "Ghasts are joining the battle!");	
			}
			
		}
		
		// register le onDeathListener
		getServer().getPluginManager().registerEvents(new DragonDeathListener(_theDragon, this, _ghasts), getPlugin());
		isActive = true;
	}

	@Override
	public void abortScenario() {
		// si les joueurs quittent, on annule le tout!
		if (_theDragon != null) {
			_theDragon.remove();
			
			if (showRetreatMessage) {
				getServer().broadcastMessage("The Dragon retreated to his hideout!");	
			}
		}
		
		if (_ghasts != null && _ghasts.size() > 0) {
			for (LivingEntity l : _ghasts) {
				l.remove();
			}
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
	private LivingEntity _dragon;
	private DragonFury _scenario;
	private List<LivingEntity> _ghasts;
	
	public DragonDeathListener(LivingEntity d, DragonFury s, List<LivingEntity> ghasts) {
		_dragon = d;
		_scenario = s;
		_ghasts = ghasts;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDragonDeath(EntityDeathEvent event) {
		if (event.getEntity().getEntityId() == _dragon.getEntityId()) {
			_scenario.getServer().broadcastMessage(ChatColor.GREEN + "The Dragon has been defeated!");
			_scenario.setLastDragonDefeat(System.currentTimeMillis());
		}
		else/* if (event.getEntity() instanceof Ghast)*/{
			for (int i = 0; i < _ghasts.size(); i++) {
				if (event.getEntity().getEntityId() == _ghasts.get(i).getEntityId()) {
					_ghasts.remove(i);
					break;
				}
			}
		}		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void dragonExplodeBlocks(EntityExplodeEvent event) {
		if (_dragon != null && event.getEntity().getEntityId() == _dragon.getEntityId()){
			event.setCancelled(true);
		}
	}
	
}