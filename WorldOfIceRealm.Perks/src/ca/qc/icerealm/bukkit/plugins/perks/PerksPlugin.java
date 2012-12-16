package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.perks.archer.LightningReflexesPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.PoisonedArrowPerk;
import ca.qc.icerealm.bukkit.plugins.perks.blacksmith.StoneWorkerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.farmer.GreenThumbPerk;
import ca.qc.icerealm.bukkit.plugins.perks.farmer.VassalPerk;
import ca.qc.icerealm.bukkit.plugins.perks.lumberjack.WoodmanPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.BerserkerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.ExplorerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.FindWeaknessPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.LastManStandingPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.LifeLeechPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.MeatShieldPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.MercenaryPerk;

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
		getServer().getPluginManager().registerEvents(new BerserkerPerk(), this);
		getServer().getPluginManager().registerEvents(new PoisonedArrowPerk(), this);
		getServer().getPluginManager().registerEvents(new LifeLeechPerk(), this);
		getServer().getPluginManager().registerEvents(new FindWeaknessPerk(), this);
		getServer().getPluginManager().registerEvents(new LastManStandingPerk(), this);
		
		// Other
		getServer().getPluginManager().registerEvents(PerkService.getInstance(), this);
		getServer().getPluginManager().registerEvents(new ClearPerk(), this);
		getServer().getPluginManager().registerEvents(new PerkNotifier(), this);
	}
	
}