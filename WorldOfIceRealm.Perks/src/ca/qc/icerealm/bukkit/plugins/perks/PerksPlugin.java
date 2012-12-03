package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class PerksPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		registerEvents();
		
		getCommand("perk").setExecutor(new PerkCommandExecutor(this));
	}

	private void registerEvents() {
		// Perks
		getServer().getPluginManager().registerEvents(new StoneWorkerPerk(), this);
		getServer().getPluginManager().registerEvents(new MercenaryPerk(), this);
		getServer().getPluginManager().registerEvents(new ExplorerPerk(), this);
		getServer().getPluginManager().registerEvents(new WoodmanPerk(), this);
		getServer().getPluginManager().registerEvents(new VassalPerk(), this);
		getServer().getPluginManager().registerEvents(new GreenThumbPerk(), this);
		getServer().getPluginManager().registerEvents(new MeatShieldPerk(), this);
		getServer().getPluginManager().registerEvents(new LightningReflexesPerk(), this);
		
		// Other
		getServer().getPluginManager().registerEvents(PerkService.getInstance(), this);
		getServer().getPluginManager().registerEvents(new ClearPerk(), this);
		getServer().getPluginManager().registerEvents(new PerkNotifier(), this);
	}
	
}