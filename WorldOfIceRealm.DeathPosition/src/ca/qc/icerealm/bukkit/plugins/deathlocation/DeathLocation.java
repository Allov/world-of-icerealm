package ca.qc.icerealm.bukkit.plugins.deathlocation;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathLocation extends JavaPlugin
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@Override
	public void onDisable() 
	{
				
	}

	@Override
	public void onEnable() 
	{	
		// Version 1.0.0
		getServer().getPluginManager().registerEvents(new DeathLocationListener(), this);
	}
}