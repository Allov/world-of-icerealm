package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.server.EntityItem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class AmbushScenario extends Scenario {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _isActive = false;
	private boolean _isComplete = false;
	private double _radius;
	private int _quantity;
	private String _monster;
	List<LivingEntity> monsters;
	
	public AmbushScenario(int qty, String monster, double radius) {
		_radius = radius;
		_quantity = qty;
		_monster = monster;
		this.logger.info(_radius + "RADIUS IS!!!");
	}
	
	@Override
	public boolean abortWhenLeaving() {
		return true;
	}

	@Override
	public boolean isTriggered() {
		return _isActive;
	}

	@Override
	public void triggerScenario() {
		
		getServer().broadcastMessage("Icerealm fighters has been ambushed by monsters!!!!");
		
		Random r = new Random();
		Location loc = null;
		
		// on pogne un joueur au hasard!
		if (getPlayers().size() == 1) {
			loc = this.getPlayers().get(0).getLocation();
		}
		else if (getPlayers().size() > 1) {
			int index = r.nextInt(this.getPlayers().size() - 1);
			loc = this.getPlayers().get(index).getLocation();
		}

		int i = 0;
		
		List<Location> locations = getRandomLocation(getZone(), _quantity);
		monsters = new ArrayList<LivingEntity>();
		while (i < _quantity) {
			this.logger.info(locations.get(i).toString());
			LivingEntity e = getWorld().spawnCreature(locations.get(i), CreatureType.ZOMBIE);
			monsters.add(e);
			i++;
		}
		
		Listener deathListener = new AmbushScenarioListener(this, monsters);
		getServer().getPluginManager().registerEvents(deathListener, getPlugin());
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
			
			this.logger.info("X: " + tlX + " Z: " + tlZ);
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
		// TODO Auto-generated method stub
		return true;
	}

}

class AmbushScenarioListener implements Listener {
	
	private AmbushScenario _scenario;
	private List<LivingEntity> _list;
	
	public AmbushScenarioListener(AmbushScenario s, List<LivingEntity> list) {
		_scenario = s;
		_list = list;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDeath(EntityDeathEvent event) {
		
		int id = event.getEntity().getEntityId();
		
		for (LivingEntity l : _list) {
			if (id == l.getEntityId()) {
				_list.remove(l);
				displayKill();
				break;
			}
		}
		
		if (_list.size() == 0) {
			_scenario.getServer().broadcastMessage(ChatColor.GOLD + "Icerealm fighers fought to defend their freedom. The monsters has been defeated!!!");
		}
	
	}
	
	private void displayKill() {

		for (Player p : _scenario.getPlayers()) {
			if (_list.size() == 0) {
				p.sendMessage(ChatColor.GOLD + "No monsters left, good job!");
			}
			else {
				p.sendMessage(ChatColor.GREEN + "Kill confirmed, " + _list.size() + " left");	
			}
		}
	}
}
