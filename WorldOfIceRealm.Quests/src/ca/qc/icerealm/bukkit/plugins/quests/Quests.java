package ca.qc.icerealm.bukkit.plugins.quests;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.quests.builder.RandomQuestService;
import ca.qc.icerealm.bukkit.plugins.quests.builder.ScriptedQuestService;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class Quests extends JavaPlugin {

	private PluginManager pluginManager;
	private RegisteredServiceProvider<Economy> economyProvider;
	private RandomQuestService randomQuestService;
	private ScriptedQuestService scriptedQuestService;
	
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		initializeQuestServices();
		pluginManager = getServer().getPluginManager();
		
		if(pluginManager.isPluginEnabled("Vault")) {
			economyProvider = getServer()
					.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
		}
		
		getServer().getPluginManager().registerEvents(new QuestsEventListener(scriptedQuestService), this);
		
		getCommand("q").setExecutor(new QuestCommandExecutor(this));
	}

	private void initializeQuestServices() {
		randomQuestService = new RandomQuestService(this, QuestLogService.getInstance());
		
		File file = new File(getDataFolder(), "quests.yml"); 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		InputStream defConfigStream = getResource("quests.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    
		scriptedQuestService = new ScriptedQuestService(this, QuestLogService.getInstance(), new ConfigWrapper(config));
	}

	public RegisteredServiceProvider<Economy> getEconomyProvider() {
		return economyProvider;
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public RandomQuestService getRandomQuestService() {
		return randomQuestService;
	}

	public ScriptedQuestService getScriptedQuestService() {
		return scriptedQuestService;
	}
}