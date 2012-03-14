package ca.qc.icerealm.bukkit.plugins.scenarios.obsidian;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CoolDown;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CoolDownTimer;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityHelper;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class BreakBlockSession implements ZoneObserver, CoolDown {
	private Logger _logger = Logger.getLogger("Minecraft");
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
	private CoolDownTimer _coolDownTimer;
	private long _coolDownDueTimestamp;
	private String _header;
	private boolean _resetWhenPlayerLeaving;
	private String _msgWhenBlockBroken;
	private List<Player> _players;
	
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
		_coolDownTime = 0;
		_header = ChatColor.LIGHT_PURPLE + "[" + ChatColor.DARK_PURPLE + "Obsidian" + ChatColor.LIGHT_PURPLE + "] ";
		_resetWhenPlayerLeaving = false;
		_players = new ArrayList<Player>();
	}
	
	public void setCooldownTime(long time) {
		_coolDownTime = time;
	}
	
	public void setResetWhenPlayerLeaving(boolean b) {
		_resetWhenPlayerLeaving = b;
	}
	
	public Location getBlockLocation() {
		return _locations;
	}
	
	public Material getBlockMaterial() {
		return _material;
	}
	
	public void activateBreakingZone() {
		if (!_isCooldownActive && _blockBroken) {
			_logger.info("activting");
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
		else {
			_logger.info("not activting");	
		}
		
		
		
		
	}
	
	public void resetToInitialState() {
		for (Monster m : _spawnedMonsters) {
			m.remove();
		}
		org.bukkit.block.Block b = _world.getBlockAt(_locations);
		b.setType(Material.AIR);	
		_blockBroken = true;
	}

	public boolean onBlockBroken(BlockBreakEvent event) {
		if (event.getBlock().getLocation().equals(_locations)) {
			_blockBroken = true;
			return true;
		}
		return false;
	}
	
	public void onMonsterDamaged(EntityDamageEvent event) {
		
		Monster monster = null;
		for (Monster m : _spawnedMonsters) {
			if (m.getEntityId() == event.getEntity().getEntityId()) {
				monster = m;
				break;
			}
		}
		
		
		
		if (monster != null) {
			
			if (event.getCause() == DamageCause.FIRE_TICK) {
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
				// lette, hack, shame on me
				return;
			}
			
			if (!_blockBroken) {
				monster.setHealth(monster.getMaxHealth());
				//_logger.info("" + monster.getHealth());	
			}
			else {
				monster.damage(event.getDamage());
				//_logger.info("" + monster.getHealth() + " got damaged!!!");
			}
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
		
		_players.add(p);
		
		if (_isCooldownActive) {
			p.sendMessage(ChatColor.GOLD + "Active in " + (_coolDownDueTimestamp - System.currentTimeMillis() + " ms"));
		}
		else {
			
			if (_blockBroken) {
				p.sendMessage(_header + ChatColor.GREEN + "Break the Obsidian block");
				activateBreakingZone();	
			}
			else {
				p.sendMessage(ChatColor.GRAY + "entering the zone");
			}
			
			if (_coolDownTime > 0) {
				setCoolDownActive(true);
				_coolDownTimer = new CoolDownTimer(this);
				TimeServer.getInstance().addListener(_coolDownTimer, _coolDownTime);
				_coolDownDueTimestamp = System.currentTimeMillis() + _coolDownTime;
				_logger.info(_coolDownDueTimestamp + "cool down to be");
			}
			
			
			
		}
	}
	
	public void playerLeft(Player p) {
		_players.remove(p);
		if (_resetWhenPlayerLeaving && _players.size() <= 0) {
			p.sendMessage(_header + ChatColor.RED + "You cancelled the project...");
			resetToInitialState();	
		}
		
		p.sendMessage(ChatColor.GRAY + "leaving the zone");
		
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

