package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.server.EntityItem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.*;


public class AmbushScenario extends Scenario {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _isActive = false;
	private boolean _isComplete = false;
	private boolean _immune;
	private int _quantity;
	private String[] _monstersType;
	List<LivingEntity> monsters;
	
	public AmbushScenario(int qty, String monster, boolean immune) {
		_immune = immune;
		_quantity = qty;
		_monstersType = monster.split(",");
	}
	
	@Override
	public boolean abortWhenLeaving() {
		_isActive = false;
		return false;
	}

	@Override
	public boolean isTriggered() {
		return _isActive;
	}

	@Override
	public void triggerScenario() {
		
		getServer().broadcastMessage(ChatColor.RED + "Icerealm fighters has been ambushed by monsters!!!!");
		
		int i = 0;
		List<Location> locations = getZone().getRandomLocation(getWorld(), _quantity);
		monsters = new ArrayList<LivingEntity>();
		while (i < _quantity) {
			CreatureType creature = EntityUtilities.getCreatureType(_monstersType[RandomUtil.getRandomInt(_monstersType.length - 1)]);			
			LivingEntity e = getWorld().spawnCreature(locations.get(i), creature);
			monsters.add(e);
			i++;
		}
		
		Listener deathListener = new AmbushScenarioListener(this, monsters, _immune);
		getServer().getPluginManager().registerEvents(deathListener, getPlugin());
		_isActive = true;	
		
	}
	
	public void setComplete(boolean c) {
		_isComplete = c;
		_isActive = false;
	}
	
	public boolean isComplete() {
		return _isComplete;
	}

	@Override
	public void abortScenario() {
		// TODO Auto-generated method stub
		getServer().broadcastMessage(ChatColor.RED + "The fighter retreated!!!");
		getServer().broadcastMessage(ChatColor.RED + "The monsters are still there, be careful!");
		for (LivingEntity e : monsters) {
			e.remove();
		}
	}

	@Override
	public boolean canBeTriggered() {
		return true;
	}

	@Override
	public boolean mustBeStop() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void terminateScenario() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void progressHandler() {
		// TODO Auto-generated method stub
		
	}
}

class AmbushScenarioListener implements Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private AmbushScenario _scenario;
	private List<LivingEntity> _list;
	private boolean _immune;
	
	public AmbushScenarioListener(AmbushScenario s, List<LivingEntity> list, boolean immune) {
		_scenario = s;
		_list = list;
		_immune = immune;
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFireDamage(EntityDamageEvent event) {
		
		if (!_immune) {
			LivingEntity l = getEntity(event.getEntity().getEntityId());
			if (event.getCause() == DamageCause.FIRE_TICK) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDeath(EntityDeathEvent event) {
		
		int id = event.getEntity().getEntityId();
		_list.remove(getEntity(id));
		
		if (_list.size() == 0) {
			_scenario.getServer().broadcastMessage(ChatColor.GOLD + "Icerealm fighters defeated the monsters!");
		}
	
	}

	
	private LivingEntity getEntity(int id) {
		
		for (LivingEntity l : _list) {
			if (id == l.getEntityId()) {
				return l;				
			}
		}
		return null;
	}
	
}
