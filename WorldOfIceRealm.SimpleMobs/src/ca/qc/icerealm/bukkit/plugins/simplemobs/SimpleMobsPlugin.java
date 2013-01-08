package ca.qc.icerealm.bukkit.plugins.simplemobs;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.simplemobs.SimpleMobsCommandExecutor;

public class SimpleMobsPlugin  extends JavaPlugin
{
	@Override
	public void onDisable() 
	{
	}

	@Override
	public void onEnable() 
	{
		getCommand("mobs").setExecutor(new SimpleMobsCommandExecutor());
	}
}
