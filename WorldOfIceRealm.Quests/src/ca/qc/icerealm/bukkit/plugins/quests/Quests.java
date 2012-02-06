package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.quests.builder.BasicQuestService;
import ca.qc.icerealm.bukkit.plugins.quests.builder.QuestDefinition;
import ca.qc.icerealm.bukkit.plugins.quests.builder.QuestService;

public class Quests extends JavaPlugin {

	public final Logger logger = Logger.getLogger(("Minecraft"));

	private PluginManager pluginManager;
	private List<QuestDefinition> quests;
	private Map<Player, Quest> onGoingQuests;
	private RegisteredServiceProvider<Economy> economyProvider;
	private QuestService questService; 
	
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		questService = new BasicQuestService(this);
		pluginManager = getServer().getPluginManager();
		
		if(pluginManager.isPluginEnabled("Vault")) {
			economyProvider = getServer()
					.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
		}
		
		getCommand("q").setExecutor(new QuestCommandExecutor(this));
	}

	public RegisteredServiceProvider<Economy> getEconomyProvider() {
		return economyProvider;
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public QuestService getQuestService() {
		return questService;
	}
}