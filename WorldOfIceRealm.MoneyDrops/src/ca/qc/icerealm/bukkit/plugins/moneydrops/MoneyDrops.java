package ca.qc.icerealm.bukkit.plugins.moneydrops;

import java.util.Calendar;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MoneyDrops extends JavaPlugin {
	
	private final int MaxMoney = 20;

	public PluginManager pluginManager;
	private RegisteredServiceProvider<Economy> economyProvider;
	
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {

		pluginManager = getServer().getPluginManager();
		
		if(pluginManager.isPluginEnabled("Vault")) {
			economyProvider = getServer()
					.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
		}

		getServer().getPluginManager().registerEvents(new MoneyDropsEventListener(this), this);

	}

	public void giveMoneyReward(Player player) {
		if (this.economyProvider != null) {
			Economy economy = economyProvider.getProvider();
	
			if (economy.bankBalance(player.getName()) != null) 
	        {	
	        	economy.depositPlayer(player.getName(), MaxMoney);
	        }
		}
	}

}
