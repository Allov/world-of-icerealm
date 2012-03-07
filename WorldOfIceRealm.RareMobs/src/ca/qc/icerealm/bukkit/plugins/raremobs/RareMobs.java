package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMobsData;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMobsFactory;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class RareMobs extends JavaPlugin
{
	public static final int SPAWN_CHECK_INTERVAL = 10000;
	
	@Override
	public void onDisable() 
	{
				
	}

	@Override
	public void onEnable() 
	{	
		File file = new File(getDataFolder(), "raremobs.yml"); 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		InputStream defConfigStream = getResource("raremobs.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		
	    // Load all the data (rare mobs, subordinates and rare drops)
		ConfigWrapper configWrapper = new ConfigWrapper(config);
		List<MapWrapper> mobs = configWrapper.getMapList("raremobs", new ArrayList<MapWrapper>());
		RareMobsFactory factory = new RareMobsFactory(mobs, 1);
		
		// Add mobs to singleton
		RareMobsData data = factory.build();
		
		// Set randomizer and start an observer that will spawn rare mobs randomly
		RareMobsRandomizer randomizer = new RareMobsRandomizer(data);
		RareMobsTimeObserver observer = new RareMobsTimeObserver(getServer(), randomizer);
		TimeServer.getInstance().addListener(observer, SPAWN_CHECK_INTERVAL);
		
		getServer().getPluginManager().registerEvents(new RareMobsEntityListener(), this);
		getServer().getPluginManager().registerEvents(new RareMobDamageListener(), this);
	}
}
