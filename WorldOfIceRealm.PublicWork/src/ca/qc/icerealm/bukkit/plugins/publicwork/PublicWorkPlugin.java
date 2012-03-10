package ca.qc.icerealm.bukkit.plugins.publicwork;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class PublicWorkPlugin extends JavaPlugin implements CommandExecutor, Listener {

	private Logger _logger = Logger.getLogger("Minecraft");
	private ZoneSubject _zoneServer = ZoneServer.getInstance();
	private WorldZone _zone;
	private RegisteredServiceProvider<Economy> economyProvider;
	private Double _amountPerSnow = 1.0;
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// commande console
		getCommand("pw").setExecutor(this);

		// zone de pelletage
		_zone = new WorldZone(getServer().getWorld("world"), "617,214,744,298,66,128");
		ShovelingZone shov = new ShovelingZone(getServer(), _zone);
		_zoneServer.addListener(shov);
		
		// listener pour savoir ce qui est pellet�
		getServer().getPluginManager().registerEvents(this, this);
		
		if(this.getServer().getPluginManager().isPluginEnabled("Vault")) {
			economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		}
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] params) {

		sender.sendMessage(ChatColor.GREEN + "Shovel snow inside the village: " + ChatColor.GOLD + _amountPerSnow.intValue() + " gold each");
		return true;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerShoveling(BlockBreakEvent event) {
		
		if (_zone.isInside(event.getBlock().getLocation()) && event.getBlock().getType() == Material.SNOW) {
			
			if (economyProvider != null) {
				giveMoneyReward(event.getPlayer(), _amountPerSnow);
			}
			else {
				_logger.info("[PublicWork] no economy provider for Public Works");
			}
			
			
		}
		
	}
	
	public void giveMoneyReward(Player player, double money) {
		if (this.economyProvider != null) {
			
			Economy economy = economyProvider.getProvider();
	
			if (economy.bankBalance(player.getName()) != null) 
	        {	
	        	economy.depositPlayer(player.getName(), money);
	        }
		}
	}
	
	
}
