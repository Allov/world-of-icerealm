package ca.qc.icerealm.bukkit.plugins.SlashRoll;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class SlashRoll extends JavaPlugin {
	public final Logger logger = Logger.getLogger(("Minecraft"));

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