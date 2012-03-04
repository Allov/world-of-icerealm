package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.io.File;
import java.io.InputStream;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;

public class RareDrops extends JavaPlugin
{
	@Override
	public void onDisable() 
	{
				
	}

	@Override
	public void onEnable() 
	{
		
		File file = new File(getDataFolder(), "raredrops.yml"); 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		InputStream defConfigStream = getResource("raredrops.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }

		getServer().getPluginManager().registerEvents(new RareDropsEntityListener(new ConfigWrapper(config)), this);
	}
}
