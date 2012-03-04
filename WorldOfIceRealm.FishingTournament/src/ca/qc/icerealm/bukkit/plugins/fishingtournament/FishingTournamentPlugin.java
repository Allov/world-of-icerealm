package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class FishingTournamentPlugin extends JavaPlugin {

	private FishingTournament fishingTournament;
	private ScheduledFuture<?> fishingTournamentThread;

	@Override
	public void onDisable() {
		if (fishingTournamentThread != null) {
			fishingTournamentThread.cancel(false);
		}
	}

	@Override
	public void onEnable() {
		FileConfiguration config = getTournamentConfiguration();
		
		fishingTournament = new FishingTournament(this, FishingTournamentConfig.fromConfigurationFile(config));
		fishingTournamentThread = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(fishingTournament, 10, 10, TimeUnit.SECONDS);
	}

	private FileConfiguration getTournamentConfiguration() {
		File file = new File(getDataFolder(), "config.yml"); 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		InputStream defConfigStream = getResource("config.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    
	    return config;
	}
}
