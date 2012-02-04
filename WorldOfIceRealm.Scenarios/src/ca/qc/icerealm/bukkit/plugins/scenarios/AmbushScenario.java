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

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;

public class AmbushScenario extends Scenario {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _isActive = false;
	private boolean _isComplete = false;
	private boolean _immune;
	private int _quantity;
	private String _monster;
	List<LivingEntity> monsters;
	private Date _lastRun;
	
	public AmbushScenario(int qty, String monster, boolean immune) {
		_immune = immune;
		_quantity = qty;
		_monster = monster;
	}
	
	@Override
	public boolean abortWhenLeaving() {
		_isActive = false;
		return true;
	}

	@Override
	public boolean isTriggered() {
		return _isActive;
	}

	@Override
	public void triggerScenario() {
		
		getServer().broadcastMessage(ChatColor.RED + "Icerealm fighters has been ambushed by monsters!!!!");
		
		int i = 0;
		List<Location> locations = getRandomLocation(getZone(), _quantity);
		monsters = new ArrayList<LivingEntity>();
		while (i < _quantity) {
			this.logger.info(locations.get(i).toString());
			LivingEntity e = getWorld().spawnCreature(locations.get(i), CreatureType.ZOMBIE);
			monsters.add(e);
			i++;
		}
		
		Listener deathListener = new AmbushScenarioListener(this, monsters, _immune);
		getServer().getPluginManager().registerEvents(deathListener, getPlugin());
		_isActive = true;	
		
	}
	
	private List<Location> getRandomLocation(ScenarioZone z, int qty) {
		List<Location> list = new ArrayList<Location>();
		double topLeftX = 0;
		double topLeftZ = 0;
		double bottomRightX = getZone().getRelativeBottomRight().getX();
		double bottomRightZ = getZone().getRelativeBottomRight().getZ();
		for (int i = 0; i < qty; i++) {
			
			double tlX = RandomUtil.getRandomDouble(topLeftX, bottomRightX);
			double tlZ = RandomUtil.getRandomDouble(topLeftZ, bottomRightZ);
			tlX += getZone().getTopLeft().getX();
			tlZ += getZone().getTopLeft().getZ();
			list.add(new Location(getWorld(), tlX, 70, tlZ));
		}
		return list;
	}

	@Override
	public boolean isComplete() {
		return _isComplete;
	}
	
	public void setComplete(boolean c) {
		_isComplete = c;
		_isActive = false;
	}

	@Override
	public void abortScenario() {
		// TODO Auto-generated method stub
		getServer().broadcastMessage(ChatColor.RED + "The Ambush Scenario has been aborted by the fighters, cowards!!!!");
		for (LivingEntity e : monsters) {
			e.remove();
		}
	}

	@Override
	public boolean canBeTriggered() {
		return true;
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
		displayKill();
		
		if (_list.size() == 0) {
			_scenario.getServer().broadcastMessage(ChatColor.GOLD + "Icerealm fighters defeated the monsters!");
		}
	
	}
	
	private void displayKill() {
		Player p = _scenario.getPlayers().get(0);
		//p.sendMessage()
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

class MonsterOnFire implements Listener {
	
}
