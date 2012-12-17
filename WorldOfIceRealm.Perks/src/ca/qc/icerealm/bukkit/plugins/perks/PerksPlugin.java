package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.perks.archer.ArcherTree;
import ca.qc.icerealm.bukkit.plugins.perks.archer.ExplorerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.FindWeaknessPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.LightningReflexesPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.PoisonedArrowPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.WindRunPerk;
import ca.qc.icerealm.bukkit.plugins.perks.blacksmith.StoneWorkerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.farmer.GreenThumbPerk;
import ca.qc.icerealm.bukkit.plugins.perks.farmer.VassalPerk;
import ca.qc.icerealm.bukkit.plugins.perks.lumberjack.WoodmanPerk;
import ca.qc.icerealm.bukkit.plugins.perks.magic.ExecuteMagicEvent;
import ca.qc.icerealm.bukkit.plugins.perks.magic.fire.FireStoper;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.BerserkerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.LastManStandingPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.LifeLeechPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.MeatShieldPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.MercenaryPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.WarriorTree;

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
		getServer().getPluginManager().registerEvents(new VassalPerk(), this);
		getServer().getPluginManager().registerEvents(new GreenThumbPerk(), this);
		getServer().getPluginManager().registerEvents(new WoodmanPerk(), this);

		// Warrior
		getServer().getPluginManager().registerEvents(new MercenaryPerk(), this);
		getServer().getPluginManager().registerEvents(new MeatShieldPerk(), this);
		getServer().getPluginManager().registerEvents(new BerserkerPerk(), this);
		getServer().getPluginManager().registerEvents(new LifeLeechPerk(), this);
		getServer().getPluginManager().registerEvents(new LastManStandingPerk(), this);

		// Archer
		getServer().getPluginManager().registerEvents(new ExplorerPerk(), this);
		getServer().getPluginManager().registerEvents(new PoisonedArrowPerk(), this);
		getServer().getPluginManager().registerEvents(new FindWeaknessPerk(), this);
		WindRunPerk windRunPerk = new WindRunPerk();
		getServer().getPluginManager().registerEvents(windRunPerk, this);
		getServer().getPluginManager().registerEvents(new LightningReflexesPerk(windRunPerk), this);
		
		PerkService.getInstance().addTree(new ArcherTree());
		PerkService.getInstance().addTree(new WarriorTree());
		
		// Magic 
		getServer().getPluginManager().registerEvents(new ExecuteMagicEvent(), this);
		
		// Other
		getServer().getPluginManager().registerEvents(PerkService.getInstance(), this);
		getServer().getPluginManager().registerEvents(new ClearPerk(), this);
		getServer().getPluginManager().registerEvents(new PerkNotifier(), this);
		getServer().getPluginManager().registerEvents(new FireStoper(), this);
	}
	
}