package ca.qc.icerealm.bukkit.plugins.trashvendor;

import org.bukkit.plugin.java.JavaPlugin;


public class TrashVendorPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
				
	}

	@Override
	public void onEnable() {
		getCommand("tv").setExecutor(new TrashVendorCommander(this));
	}

}
