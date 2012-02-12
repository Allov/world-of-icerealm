package ca.qc.icerealm.bukkit.plugins.raredrops;

import org.bukkit.plugin.java.JavaPlugin;

public class RareDrops extends JavaPlugin
{
	@Override
	public void onDisable() {
				
	}

	@Override
	public void onEnable() {
		
		// Version 0.3.0
		getServer().getPluginManager().registerEvents(new RareDropsEntityListener(), this);
	}
}
