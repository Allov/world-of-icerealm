package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.plugin.PluginManager;
//import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMobsData;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMobsFactory;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CustomMonsterListener;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class RareMobs extends JavaPlugin
{
	public static final int SPAWN_CHECK_INTERVAL = 50000;
	public static final int SPAWN_AROUND_X = 150;
	public static final int SPAWN_AROUND_Y = 50;
	public static final int SPAWN_AROUND_Z = 150;
	public static final int DEFAULT_ODDS_MULTIPLIER = 1;
	public static Server server;
	
	//private PluginManager pluginManager;
	//private RegisteredServiceProvider<Economy> economyProvider;
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@Override
	public void onDisable() 
	{
				
	}

	@Override
	public void onEnable() 
	{	
		server = this.getServer();
		
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
		RareMobsFactory factory = new RareMobsFactory(mobs, DEFAULT_ODDS_MULTIPLIER);
		
		// Add mobs to singleton
		RareMobsData data = factory.build();
		
		// Set randomizer and start an observer that will spawn rare mobs randomly
		RareMobsRandomizer randomizer = new RareMobsRandomizer(data);
		RareMobsTimeObserver observer = new RareMobsTimeObserver(getServer(), randomizer);
		TimeServer.getInstance().addListener(observer, SPAWN_CHECK_INTERVAL);
		
		//pluginManager = getServer().getPluginManager();
		
	/*	if(pluginManager.isPluginEnabled("Vault")) {
			economyProvider = getServer()
					.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
		}*/
		
		//getServer().getPluginManager().registerEvents(new RareMobsEntityListener(economyProvider.getProvider()), this);
		getServer().getPluginManager().registerEvents(new RareMobsEntityListener(), this);
		getServer().getPluginManager().registerEvents(new RareMobDamageListener(), this);
	}
}
