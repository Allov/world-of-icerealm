package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.quests.CollectObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Fees;
import ca.qc.icerealm.bukkit.plugins.quests.ItemReward;
import ca.qc.icerealm.bukkit.plugins.quests.KillObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.Quests;
import ca.qc.icerealm.bukkit.plugins.quests.Reward;
import ca.qc.icerealm.bukkit.plugins.quests.ZoneObjective;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLog;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ScriptedQuestService {
	private final ConfigWrapper config;
	private final Logger logger = Logger.getLogger("ScriptedQuests");
	private final Quests questsPlugin;
	private final QuestLogService questLogService;
	
	public ScriptedQuestService(Quests questsPlugin, QuestLogService questLogService, ConfigWrapper config) {
		this.questsPlugin = questsPlugin;
		this.questLogService = questLogService;
		this.config = config;
	}
	
	public void listQuest(Player player) {
		Set<String> set = config.getConfig().getKeys(false);
		
		player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Available quests");
		for (String key : set) {
			player.sendMessage("  > " + ChatColor.DARK_GREEN + "[" + ChatColor.YELLOW + key + ChatColor.DARK_GREEN + "] " + ChatColor.WHITE + ": " + ChatColor.DARK_GREEN + config.getString(key + ".name", "N/A"));
		}
	}
	
	public Quest getQuest(Player player, String id) {
		Quest quest = null;
		
		if (config.exists(id)) {
			QuestLog questLog = this.questLogService.getQuestLogForPlayer(player);
			
			if (questLog.getQuestByKey(id) == null) {
				quest = createQuest(player, id, questLog);
			} else {
				player.sendMessage(ChatColor.RED + "You're already on that quest.");
			}			
		}
		
		return quest;
	}

	private Quest createQuest(Player player, String id, QuestLog questLog) {
		Quest quest;
		Fees joinFees = new Fees(config.getInt(id + ".joinFees.level", 0), config.getInt(id + ".joinFees.money", 0), 0, 0);
		Fees dropFees = new Fees(config.getInt(id + ".dropFees.level", 0), config.getInt(id + ".dropFees.money", 0), 0, 0);
		Reward reward = createReward(id);
		
		quest = new Quest(
				player,
				id,
				config.getString(id + ".name", ""),
				config.getString(id + ".messageStart", ""),
				config.getString(id + ".messageEnd", ""),
				config.getBoolean(id + ".daily", false),
				joinFees,
				dropFees,
				reward);
		
		List<MapWrapper> objectives = config.getMapList(id + ".objectives", new ArrayList<MapWrapper>());
		for (MapWrapper map : objectives) {
			Objective objective = ObjectiveFactory.getInstance()
					.createFromMap(this.questsPlugin, player, map);
			quest.getObjectives().add(objective);
			objective.register(quest);
		}
		questLog.addQuest(quest);
		return quest;
	}

	private Reward createReward(String id) {
		Reward reward = new Reward(
				config.getInt(id + ".rewards.level", 0), 
				config.getInt(id + ".rewards.money", 0), 
				this.questsPlugin.getEconomyProvider().getProvider());
		
		List<MapWrapper> items = config.getMapList(id + ".rewards.items", new ArrayList<MapWrapper>());
		
		if (items != null) {
			for (MapWrapper map : items) {
				int itemId = map.getInt("id", 0);
				
				if (itemId != 0) {
					reward.getItems().add(new ItemReward(itemId, map.getInt("amount", 1)));
				}
			}
		}
		
		return reward;
	}
}

class ObjectiveFactory {
	private static final String ObjectiveTypeCollect = "collect";
	private static final String ObjectiveTypeKill = "kill";
	private static final String ObjectiveTypeZone = "zone";
	private static ObjectiveFactory instance;
	
	public static ObjectiveFactory getInstance() {
		if (instance == null) {
			instance = new ObjectiveFactory();
		}
		
		return instance;
	}
	
	public Objective createFromMap(Quests quests, Player player, MapWrapper map) {
		Objective objective = null;
		if (map.getString("type", "").toString().equalsIgnoreCase(ObjectiveTypeKill)) {
			
			WorldZone zone = getWorldZone(quests, map);
			List<Integer> entityIds = getEntities(map);			
			
			objective = new KillObjective(
					player,
					map.getString("name", ""),
					zone, 
					map.getInt("amount", 0),
					entityIds);
			
			quests.getServer().getPluginManager().registerEvents((KillObjective)objective, quests);
			
		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeZone)) {
			WorldZone zone = getWorldZone(quests, map);

			objective = new ZoneObjective(player, zone, map.getString("name", ""), quests.getServer());
			
			ZoneServer.getInstance().addListener((ZoneObjective)objective);
		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeCollect)) {
			
			WorldZone zone = getWorldZone(quests, map);
			
			objective = new CollectObjective(
					player, 
					zone, 
					map.getString("name", "N/A"), 
					map.getInt("amount", 0), 
					map.getInt("what", 0));
			
			quests.getServer().getPluginManager().registerEvents((CollectObjective)objective, quests);			
		}
		
		return objective;
	}

	private List<Integer> getEntities(MapWrapper map) {
		List<Integer> entityIds = new ArrayList<Integer>();
		
		String ids = map.getString("what", "");
		
		if (ids != null && ids.length() > 0) {
			String[] entities = ids.split(",");
			
			for(int i = 0; i < entities.length; i++) {
				entityIds.add(Integer.parseInt(entities[i]));
			}
		}
		return entityIds;
	}

	private WorldZone getWorldZone(Quests quests, MapWrapper map) {
		WorldZone zone = null;
		String coords = map.getString("zone", "");
		
		if (coords.split(",").length == 6) {
			zone = new WorldZone(quests.getServer().getWorld("world"), coords);
		}
		return zone;
	}
}
