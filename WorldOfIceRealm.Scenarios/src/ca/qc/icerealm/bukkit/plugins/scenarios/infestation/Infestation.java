package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class Infestation implements ZoneObserver, Listener {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _zone;
	private List<Player> _players;
	private Server _server;
	private int _quantity;
	private List<FixedSpawner> _spawners;
	private String[] _monsters;
	private World _world;
	private JavaPlugin _plugin;
	private InfestationConfiguration _config;
	
	public Infestation(JavaPlugin j, InfestationConfiguration config) {
		_plugin = j;
		_server = j.getServer();
		_players = new ArrayList<Player>();
		_spawners = new ArrayList<FixedSpawner>();
		_world = _server.getWorld("world");
		_zone = new WorldZone(_world, config.InfestedZone);
		_quantity = config.SpawnerQuantity;
		_monsters = config.SpawnerMonsters.split(",");
		_config = config;
	}
	
	private void createRandomSpawners() {
		for (int i = 0; i < _quantity; i++) {	
			Location l = _zone.getRandomLocation(_world);
			FixedSpawner spawner = new FixedSpawner(l, _config, _players);
			TimeServer.getInstance().addListener(spawner, _config.IntervalBetweenSpawn);
			_spawners.add(spawner);
		}
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
	public void playerEntered(Player p) {
		p.sendMessage(_config.EnterZoneMessage);
		_players.add(p);
		if (_spawners.size() == 0) {
			createRandomSpawners();
		}
	}

	@Override
	public void playerLeft(Player p) {
		_players.remove(p);
		
		if (_players.size() == 0) {
			for (FixedSpawner s : _spawners) {
				s.clearRemainingMonsters();
				TimeServer.getInstance().removeListener(s);
			}
			_spawners.clear();
		}
		
		p.sendMessage(_config.LeaveZoneMessage);
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		playerLeft(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDestroy(EntityExplodeEvent  event) {
		if (_config.RegenerateExplodedBlocks && _zone.isInside(event.getEntity().getLocation())) {
			HashMap<Location, BlockContainer> _blocks = new HashMap<Location, BlockContainer>();
			for (Block b : event.blockList()) {
				BlockContainer bc = new BlockContainer();
				bc.TypeId = b.getTypeId();
				bc.TypeData = b.getData();
				_blocks.put(b.getLocation(), bc);
			}
			
			BlockRestore restore = new BlockRestore(_world, _blocks);
			TimeServer.getInstance().addListener(restore, _config.DelayBeforeRegeneration);
		}		
	}
}
