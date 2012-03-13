package ca.qc.icerealm.bukkit.plugins.scenarios.obsidian;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CoolDown;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityHelper;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class BreakBlockSession implements ZoneObserver, CoolDown {
	private Logger _logger = ScenarioPlugin.logger;
	private Location _locations;
	private Material _material;
	private double _radius;
	private int _qty;
	private String _monsters;
	private WorldZone _zone;
	private Server _server;
	private List<Monster> _spawnedMonsters;
	private World _world;
	private boolean _blockBroken;
	private boolean _isCooldownActive;
	private long _coolDownTime;
	
	public BreakBlockSession(Location l, Material m, double r, int qty, String monsters) {
		_locations = l;
		_material = m;
		_radius = r;
		_qty = qty;
		_monsters = monsters;
		_zone = new WorldZone(l, r);
		_world = l.getWorld();
		_spawnedMonsters = new ArrayList<Monster>();
		_blockBroken = true;
		_isCooldownActive = false;
	}
	
	public Location getBlockLocation() {
		return _locations;
	}
	
	public Material getBlockMaterial() {
		return _material;
	}
	
	public void activateBreakingZone() {
		for (int i = 0; i < _qty; i++) {
			Location loc = _locations.clone();
			loc.setY(loc.getY() + 2);
			CreatureType t = EntityHelper.getRandomEntity(_monsters);
			LivingEntity l = _world.spawnCreature(loc, t);
			if (l instanceof Monster) {
				_spawnedMonsters.add((Monster)l);
				_logger.info(l.getEntityId() + " spawned at " + l.getLocation());
			}
		}
		
		org.bukkit.block.Block b = _world.getBlockAt(_locations);
		b.setType(_material);		
		_blockBroken = false;
		
	}
	
	public void resetToInitialState() {
		for (Monster m : _spawnedMonsters) {
			m.remove();
		}
		org.bukkit.block.Block b = _world.getBlockAt(_locations);
		b.setType(Material.AIR);	
	}

	public void onBlockBroken(BlockBreakEvent event) {
		if (event.getBlock().getLocation().equals(_locations)) {
			_blockBroken = true;
		}
	}
	
	public void onMonsterDamaged(EntityDamageEvent event) {
		Monster monster = null;
		for (Monster m : _spawnedMonsters) {
			if (m.getEntityId() == event.getEntity().getEntityId()) {
				monster = m;
				break;
			}
		}
		
		if (monster != null && !_blockBroken) {
			monster.setHealth(monster.getMaxHealth());
		}
		else if (monster != null) {
			monster.damage(event.getDamage());
		}
	}
	
	public void onMonsterDies(Monster m) {
		if (_spawnedMonsters.contains(m)) {
			_spawnedMonsters.remove(m);
		}
	}
	
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.getBlock().getLocation().equals(_locations)) {
			for (Monster m : _spawnedMonsters) {
				m.setTarget(event.getPlayer());
			}
		}
		
	}
	
	public void playerEntered(Player p) {
		p.sendMessage("break the block of obsidian");
		activateBreakingZone();
	}
	
	public void playerLeft(Player p) {
		p.sendMessage("you are leaving!");
		resetToInitialState();
	}
	
	public Server getCurrentServer() {
		return _server;
	}
	
	public void setCurrentServer(Server s) {
		_server = s;
	}

	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;		
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void setCoolDownActive(boolean v) {
		_isCooldownActive = v;
		_logger.info("break block cool down is " + v);
	}

	@Override
	public boolean isCoolDownActive() {
		return _isCooldownActive;
	}
}

