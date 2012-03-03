package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.spawners.ProximitySpawner;
import ca.qc.icerealm.bukkit.plugins.scenarios.spawners.Spawner;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.BlockContainer;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.BlockRestore;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class Infestation implements ZoneObserver, Listener {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _zone;
	private List<Player> _players;
	private Server _server;
	private int _quantity;
	private List<Spawner> _spawners;
	private String[] _monsters;
	private World _world;
	private JavaPlugin _plugin;
	private InfestationConfiguration _config;
	private ZoneSubject _zoneSubject;
	
	public Infestation(JavaPlugin j, InfestationConfiguration config, ZoneSubject zones) {
		_plugin = j;
		_server = j.getServer();
		_players = new ArrayList<Player>();
		_spawners = new ArrayList<Spawner>();
		_world = _server.getWorld("world");
		_zone = new WorldZone(_world, config.InfestedZone);
		_quantity = config.SpawnerQuantity;
		_monsters = config.SpawnerMonsters.split(",");
		_config = config;
		_zoneSubject = zones;
	}
	
	public int getSpawnerQuantity() {
		return _quantity;				
	}
	
	public void setSpawnerQuantity(int i) {
		_quantity = i;
	}	
	
	public InfestationConfiguration getConfig() {
		return _config;
	}
	
	
	private void createRandomSpawners() {
		ScenarioPlugin.logger.fine("creating " + _quantity + " spawners!");
		for (int i = 0; i < _quantity; i++) {		
			//Location l = _zone.getRandomLocation(_world);
			
			ProximitySpawner spawner = new ProximitySpawner(_zone, _config, _zoneSubject);
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
		ScenarioPlugin.logger.fine(p.getName() + " is entering the infestion");
	}

	@Override
	public void playerLeft(Player p) {
		if (_players.contains(p)) {
			_players.remove(p);

			ScenarioPlugin.logger.fine("playuer removed ");
			resetSpawners();
			p.sendMessage(_config.LeaveZoneMessage);	
		}
		
		ScenarioPlugin.logger.fine(p.getName() + " is leaving the infestion");
		
	}
	
	public boolean isActive() {
		return _spawners.size() > 0;
	}
	
	public void stopInfestation() {
		resetSpawners();
	}
	
	public void startInfestation() {
		if (_players.size() > 0) {
			createRandomSpawners();
		}
	}
	
	public void resetInfestation() {
		resetInfestation(_config);		
	}
	
	public void resetInfestation(InfestationConfiguration config) {
		
		_config = config;
		
		for (Player p : _players) {
			p.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "Infestation" +  ChatColor.GREEN + "] " + ChatColor.LIGHT_PURPLE + "This zone is resetting");
		}
		resetSpawners();
		if (_players.size() > 0) {
			createRandomSpawners();
		}
	}
	
	private void resetSpawners() {

		for (Spawner s : _spawners) {
			s.removeListener();
		}
		_spawners.clear();
		ScenarioPlugin.logger.fine("clearing the spawners");
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if (_zone.isInside(event.getPlayer().getLocation())) {
			playerEntered(event.getPlayer());
		}
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterBurning(EntityDamageEvent e) {
		if (!_config.BurnDuringDaylight && _zone.isInside(e.getEntity().getLocation()) && e.getCause() == DamageCause.FIRE_TICK) {
			e.setCancelled(true);
			e.getEntity().setFireTicks(0);
		}
	}
}
