package ca.qc.icerealm.bukkit.plugins.streaks;


import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class StreakPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new StreakListener(this), this);
	}

}
