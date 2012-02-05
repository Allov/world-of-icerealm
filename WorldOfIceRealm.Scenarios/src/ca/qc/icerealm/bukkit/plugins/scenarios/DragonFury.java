package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class DragonFury extends Scenario {

	private boolean isActive = false;
	private LivingEntity _theDragon;
	private List<LivingEntity> _ghasts;
	private int _ghastSpawningProb = 0; // fail safe, si la config est chié!
	private int _maxNumberOfGhast = 0; // fail safe, si la config est chié!
	
	public DragonFury(int ghastSpawn, int ghastMax) {
		_ghasts = new ArrayList<LivingEntity>();
		_ghastSpawningProb = ghastSpawn;
		_maxNumberOfGhast = ghastMax;
	}
	
	@Override
	public boolean isTriggered() {
		return isActive;
	}

	@Override
	public void triggerScenario() {
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
	public boolean isComplete() {
		// quand le dragon creve!
		return false;
	}

	@Override
	public void abortScenario() {
		// si les joueurs quittent, on annule le tout!
		if (_theDragon != null) {
			_theDragon.remove();
			getServer().broadcastMessage("The Dragon retreated to his hideout!");
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
		// aucune condition spéciale pour le partir
		return true;
	}

}

class DragonDeathListener implements Listener {
	
	private LivingEntity _dragon;
	private Scenario _scenario;
	private List<LivingEntity> _ghasts;
	
	public DragonDeathListener(LivingEntity d, Scenario s, List<LivingEntity> ghasts) {
		_dragon = d;
		_scenario = s;
		_ghasts = ghasts;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDragonDeath(EntityDeathEvent event) {
		if (event.getEntity().getEntityId() == _dragon.getEntityId()) {
			_scenario.getServer().broadcastMessage(ChatColor.GREEN + "The Dragon has been defeated!");
		}
		else {
			for (int i = 0; i < _ghasts.size(); i++) {
				if (event.getEntity().getEntityId() == _ghasts.get(i).getEntityId()) {
					_ghasts.remove(i);
					break;
				}
			}
		}		
	}
	
}