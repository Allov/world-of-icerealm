package ca.qc.icerealm.bukkit.plugins.trashvendor;

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
	private WorldZone _zone;
	private long _alarm;
	private RegisteredServiceProvider<Economy> economyProvider;
	
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
		
		//_location = new Location(getServer().getWorld("world"), -163.0, 71.0, 145.0); // server test punisher
		_location = new Location(getServer().getWorld("world"), 691.0,69.0,277.0); // shop village
		_zone = new WorldZone(_location, 2);
		_villager = (Villager)getServer().getWorld("world").spawnCreature(_location, EntityUtilities.getCreatureType("Villager"));
		timeHasCome(System.currentTimeMillis());
		getServer().getPluginManager().registerEvents(this, this);
		ZoneServer.getInstance().addListener(this);
		
		
	}
	
	public void giveMoneyReward(Player player, double money) {
		if (this.economyProvider != null) {
			Economy economy = economyProvider.getProvider();
	
			if (economy.bankBalance(player.getName()) != null) 
	        {	
	        	economy.depositPlayer(player.getName(), money);
	        	player.sendMessage(ChatColor.GREEN + "[Trash Vendor] - " + ChatColor.YELLOW + "You received " + ChatColor.GOLD + money + " gold");
	        }
		}
	}
	
	@Override
	public void timeHasCome(long time) {
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
			
			if (event.getItemDrop().getItemStack().getType() == Material.ROTTEN_FLESH) {
				giveMoneyReward(event.getPlayer(), event.getItemDrop().getItemStack().getAmount());
				event.getItemDrop().remove();
			}
			else if (event.getItemDrop().getItemStack().getType() == Material.SPIDER_EYE) {
				giveMoneyReward(event.getPlayer(), event.getItemDrop().getItemStack().getAmount());
				event.getItemDrop().remove();
			}
			else if (event.getItemDrop().getItemStack().getType() == Material.STRING) {
				giveMoneyReward(event.getPlayer(), event.getItemDrop().getItemStack().getAmount());
				event.getItemDrop().remove();
			}
			else if (event.getItemDrop().getItemStack().getType() == Material.BONE) {
				giveMoneyReward(event.getPlayer(), event.getItemDrop().getItemStack().getAmount());
				event.getItemDrop().remove();
			}
			else if (event.getItemDrop().getItemStack().getType() == Material.SULPHUR) {
				giveMoneyReward(event.getPlayer(), event.getItemDrop().getItemStack().getAmount());
				event.getItemDrop().remove();
			}
			else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GREEN + "[Trash Vendor] - " + ChatColor.RED + "Not interested in these items!");
			}
			
			
			
		}
		
		
		
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
		p.sendMessage(ChatColor.GREEN + "[Trash Vendor] - " + ChatColor.YELLOW + "Hi there you blocky head! Sell me those items");
		p.sendMessage(ChatColor.YELLOW + "by dropping them on me!");
		p.sendMessage(ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + "Rotten Flesh, Spider Eye, String, Bones, Sulphur: " + ChatColor.YELLOW + " (1g each)");

		
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
