package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class TreasureHunt implements Event, ZoneObserver, Listener, Runnable {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final long MS_MINECRAFT_HOUR = 50;
	private Location _source;
	private PinPoint _lootLocation; 
	private List<PinPoint> _loots;
	private List<List<PinPoint>> _zones;
	private Server _server;
	private WorldZone _zone;
	private boolean _lootAppeared = false;
	private Block _chestBlock;
	private int _nbPlayers = 0;
	private boolean _clearLoot = false;
	private List<Player> _players;
	private boolean _canGenerate = true;
	private String _name;
	private Loot _loot;
	
	public TreasureHunt() {
		_players = new ArrayList<Player>();
		_name = "treasurehunt";
	}

	@Override
	public void activateEvent() {
		if (_zones != null && _zones.size() == 1 && _source != null && _server != null) {
			List<PinPoint> pts = _zones.get(0);
			World world = _server.getWorld("world");
			Location lower = new Location(world, _source.getX() + pts.get(0).X, _source.getY() + pts.get(0).Y, _source.getZ() + pts.get(0).Z);
			Location higher = new Location(world, _source.getX() + pts.get(1).X, _source.getY() + pts.get(1).Y, _source.getZ() + pts.get(1).Z);

			_zone = new WorldZone(lower, higher);
			ZoneServer.getInstance().addListener(this);
		}
		
	}
	
	@Override
	public void run() {
		removeLoot();
		_canGenerate = true;
		
		if (!_clearLoot) {
			for (Player p : _players) {
				p.sendMessage(ChatColor.YELLOW + "The treasure just " + ChatColor.RED + "vanished away!");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void chestOpen(PlayerInteractEvent event) {
		
		if (event.getClickedBlock() != null) {
			Location openedChest = event.getClickedBlock().getLocation();

			try {
				if (openedChest.getX() == _loot.getLocation().getX() && openedChest.getY() == _loot.getLocation().getY() && openedChest.getZ() == _loot.getLocation().getZ()) {
					
					if (!_clearLoot) {
						for (Player p : _players) {
							p.sendMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.GREEN + " found the treasure!");
						}
					}
					
					_clearLoot = true;
					_canGenerate = false;
				}
			}
			catch (NullPointerException ex) {
				// on etouffe l'expcetion ici
			}
				
		}
		
	}
	
	@Override
	public void setSourceLocation(Location source) {
		_source = source;
	}

	@Override
	public void setPinPoints(List<PinPoint> points) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLootPoints(List<PinPoint> loots) {
		_loots = loots;	
	}

	@Override
	public void setActivateZone(List<List<PinPoint>> zones) {
		_zones = zones;		
	}

	@Override
	public void setWelcomeMessage(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEndMessage(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServer(Server s) {
		_server = s;		
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player arg0) {
		_players.add(arg0);
		World world = _server.getWorld("world");
		if (!_lootAppeared) {
			
			if (world.getTime() > 12000 && world.getTime() < 23999) {
				if (_canGenerate) {
					generateLoot();	
					
					long timeLeft = world.getTime();
					long alarm = (24000 - timeLeft) * MS_MINECRAFT_HOUR;
					Executors.newSingleThreadScheduledExecutor().schedule(this, alarm, TimeUnit.MILLISECONDS);
				}
				else {
					arg0.sendMessage(ChatColor.RED + "The hunt already happenned tonight, come back later!");
				}
				
			}
			else {
				arg0.sendMessage(ChatColor.RED + "The treasure hunt can happen only during the night");
				_canGenerate = true;
			}
		}
		
		if (_lootAppeared) {
			arg0.sendMessage(ChatColor.YELLOW + "Find the" + ChatColor.GOLD + " treasure " + ChatColor.YELLOW + "in the Ruins!");
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		arg0.sendMessage(ChatColor.YELLOW + "You left the Ruins!");
		_players.remove(arg0);
		
		if (_nbPlayers == 0 && _clearLoot) {
			removeLoot();
		}
	}

	@Override
	public void setWorldZone(WorldZone arg0) {
		_zone = arg0;
	}
	
	@Override
	public void releaseEvent() {
		removeLoot();
		ZoneServer.getInstance().removeListener(this);
	}
	
	@Override
	public String getName() {
		return _name;
	}
	
	private void generateLoot() {
		
		if (_loots.size() > 0) {
			Collections.shuffle(_loots);
			_lootLocation = _loots.get(0);
			
			Location l = new Location(_source.getWorld(), _source.getX() + _lootLocation.X, _source.getY() + _lootLocation.Y, _source.getZ() + _lootLocation.Z);
			_loot = LootGenerator.generateTreasureLoot(ScenarioService.getInstance().calculateHealthModifierWithFrontier(l, _source.getWorld().getSpawnLocation()));
			_loot.generateLoot(l);
			_logger.info("loot at: " + l.toString());
			/*
			Block b = _source.getWorld().getBlockAt(l);
			b.setType(Material.CHEST);
			
			if (b.getType() == Material.CHEST) {
				_chestBlock = b;
				Chest chest = (Chest)b.getState();
				Inventory inv = chest.getInventory();
				int nb = RandomUtils.nextInt(10);				
				inv.addItem(new ItemStack(Material.DIAMOND, nb > 0 ? nb : 1));
				_logger.info("loot apparead");
			}
			*/
			_lootAppeared = true;
		}
		
	}
	
	private void removeLoot() {
		
		if (_loot != null) {
			_loot.removeLoot();
			/*
			Location l = new Location(_source.getWorld(), _source.getX() + _lootLocation.X, _source.getY() + _lootLocation.Y, _source.getZ() + _lootLocation.Z);
			Block b = _source.getWorld().getBlockAt(l);
			
			if (b.getType() == Material.CHEST) {
				Chest chest = (Chest)b.getState();
				Inventory inv = chest.getInventory();
				inv.clear();
			}
			
			b.setType(Material.AIR);
			*/
			_lootAppeared = false;
			_clearLoot = false;
			_logger.info("loot removed!");
		}
	}

	@Override
	public void setConfiguration(String config) {
		// TODO Auto-generated method stub
		
	}

	



}
