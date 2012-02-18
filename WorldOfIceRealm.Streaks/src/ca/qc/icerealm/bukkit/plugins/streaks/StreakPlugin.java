package ca.qc.icerealm.bukkit.plugins.streaks;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class StreakPlugin extends JavaPlugin {

	private Economy economy;

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new StreakListener(getEconomy(), this), this);
	}
	
	public Economy getEconomy() {
		if (this.economy == null) {
			PluginManager pluginManager = getServer().getPluginManager();
			
			if(pluginManager.isPluginEnabled("Vault")) {
				RegisteredServiceProvider<Economy> economyProvider = getServer()
																	.getServicesManager()
																	.getRegistration(net.milkbowl.vault.economy.Economy.class);
				
				if (economyProvider != null) {
					this.economy = economyProvider.getProvider();
				}
			}
		}
		
		return this.economy;
	}

}
