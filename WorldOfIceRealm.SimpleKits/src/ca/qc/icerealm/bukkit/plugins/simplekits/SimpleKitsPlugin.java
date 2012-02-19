package ca.qc.icerealm.bukkit.plugins.simplekits;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleKitsPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getCommand("kit").setExecutor(new SimpleKitsCommandExecutor());
	}

}
