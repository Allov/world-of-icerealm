package ca.qc.icerealm.bukkit.plugins.trashvendor;

import java.util.HashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;



public class TrashVendorPlugin extends JavaPlugin implements TimeObserver, Listener, ZoneObserver {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Location _location;
	private Villager _villager;
	private Location _lastPosition;
	private WorldZone _zone;
	private long _alarm;
	private String _vendorName = "SmokeySteve";
	private RegisteredServiceProvider<Economy> economyProvider;
	private HashMap<Material, Double> _tradeTable;
	
	@Override
	public void onDisable() {
		_villager.remove();
		TimeServer.getInstance().removeListener(this);
		ZoneServer.getInstance().removeListener(this);
	}

	@Override
	public void onEnable() {
		
		if(this.getServer().getPluginManager().isPluginEnabled("Vault")) {
			economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		}
		
		fillTradeTable();
		
		//_location = new Location(getServer().getWorld("world"), -163.0, 71.0, 145.0); // server test punisher
		_location = new Location(getServer().getWorld("world"), 691.0,69.0,276.0); // shop village
		_zone = new WorldZone(_location, 5);
		//_zone = new WorldZone(getServer().getWorld("world"), "-167,140,-159,150,69,75");
		_villager = (Villager)getServer().getWorld("world").spawnCreature(_location, EntityUtilities.getCreatureType("Villager"));
		_lastPosition = _villager.getLocation(); 
		timeHasCome(System.currentTimeMillis());
		getServer().getPluginManager().registerEvents(this, this);
		ZoneServer.getInstance().addListener(this);
		
		
	}
	
	private void fillTradeTable() {
		_tradeTable = new HashMap<Material, Double>();
		_tradeTable.put(Material.ROTTEN_FLESH, 1.0);
		_tradeTable.put(Material.STRING, 1.0);
		_tradeTable.put(Material.BONE, 1.0);
		_tradeTable.put(Material.SPIDER_EYE, 1.0);
		_tradeTable.put(Material.SULPHUR, 1.0);
	}
	
	public void giveMoneyReward(Player player, double money) {
		Double doubleMoney = money;
		if (this.economyProvider != null) {
			
			Economy economy = economyProvider.getProvider();
	
			if (economy.bankBalance(player.getName()) != null) 
	        {	
	        	economy.depositPlayer(player.getName(), money);
	        	player.sendMessage(ChatColor.GREEN + "[" + ChatColor.GRAY + _vendorName + ChatColor.GREEN + "] - " + ChatColor.GREEN + "You received " + ChatColor.GOLD + doubleMoney.intValue() + " gold");
	        }
		}
	}
	
	@Override
	public void timeHasCome(long time) {
		/*
		if (!_zone.isInside(_villager.getLocation())) {
			_villager.teleport(_location);	
		}
		*/
		/*
		else {
			_lastPosition = _villager.getLocation();
		}
		*/
		_villager.teleport(_location);	
		TimeServer.getInstance().addListener(this, 100);
		
	}

	@Override
	public void setAlaram(long time) {
		// TODO Auto-generated method stub
		_alarm = time;
		
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void villagerDamage(EntityDamageEvent event) {
		if (event.getEntity().getEntityId() == _villager.getEntityId()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void itemDropToVendor(PlayerDropItemEvent event) {
		if (_zone.isInside(event.getItemDrop().getLocation())) {
			
			if (_tradeTable.containsKey(event.getItemDrop().getItemStack().getType())) {
				processTrade(event);
			}
			else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GREEN + "[" + ChatColor.GRAY + _vendorName + ChatColor.GREEN + "] - " + ChatColor.RED + "Not interested in these items!");
				event.getPlayer().updateInventory();
			}
		}		
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerRightClickf(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getEntityId() == _villager.getEntityId()) {
			event.getPlayer().sendMessage(ChatColor.GREEN + "[" + ChatColor.GRAY + _vendorName + ChatColor.GREEN + "] - " + ChatColor.DARK_GREEN + "Did you ever go to Newtown? I heard it has been doomed after a terrible fight with the ENDERDRAGON.");
		}
	}
	
	private void processTrade(PlayerDropItemEvent event) {
		giveMoneyReward(event.getPlayer(), event.getItemDrop().getItemStack().getAmount() * _tradeTable.get(event.getItemDrop().getItemStack().getType()));
		event.getItemDrop().remove();
	}

	@Override
	public void setWorldZone(WorldZone z) {
		// TODO Auto-generated method stub
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		// TODO Auto-generated method stub
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		p.sendMessage(ChatColor.GREEN + "[" + ChatColor.GRAY + _vendorName + ChatColor.GREEN + "] - " +  ChatColor.DARK_GREEN + "Drop items on me for " + ChatColor.GOLD + "gold:");
		p.sendMessage(ChatColor.GRAY + " > " + ChatColor.GREEN + "Rotten Flesh, Spider Eye, String, Bones, Sulphur: " + ChatColor.GOLD + "1 gold");

		
	}

	@Override
	public void playerLeft(Player p) {
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return this.getServer();
	}


}
