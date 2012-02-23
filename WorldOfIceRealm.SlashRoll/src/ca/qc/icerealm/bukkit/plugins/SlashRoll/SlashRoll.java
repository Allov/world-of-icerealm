package ca.qc.icerealm.bukkit.plugins.SlashRoll;

import org.bukkit.plugin.java.JavaPlugin;

public class SlashRoll extends JavaPlugin {

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		getCommand("roll").setExecutor(new SlashRollCommandExecutor());
	}
}