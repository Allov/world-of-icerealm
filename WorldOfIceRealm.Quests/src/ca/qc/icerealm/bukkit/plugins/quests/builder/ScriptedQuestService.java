package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.quests.CollectObjective;
import ca.qc.icerealm.bukkit.plugins.quests.FindObjective;
import ca.qc.icerealm.bukkit.plugins.quests.ItemReward;
import ca.qc.icerealm.bukkit.plugins.quests.ItemsReward;
import ca.qc.icerealm.bukkit.plugins.quests.KillObjective;
import ca.qc.icerealm.bukkit.plugins.quests.LevelReward;
import ca.qc.icerealm.bukkit.plugins.quests.MoneyReward;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.QuestReward;
import ca.qc.icerealm.bukkit.plugins.quests.Quests;
import ca.qc.icerealm.bukkit.plugins.quests.ZoneObjective;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLog;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ScriptedQuestService {
	private static final long DayInMillis = 1000 * 60 * 60 * 12;

	private final ConfigWrapper config;
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

			if (config.getString(key + ".requires", "").isEmpty()) {
				player.sendMessage("  > " + ChatColor.DARK_GREEN + "["
						+ ChatColor.YELLOW + key + ChatColor.DARK_GREEN + "] "
						+ ChatColor.WHITE + ": " + ChatColor.DARK_GREEN
						+ config.getString(key + ".name", "N/A"));
			}
		}
	}
	
	public Quest assignQuest(Player player, String id) {
		return assignQuest(player, id, true, 0);
	}
	
	public Quest assignQuest(Player player, String id, boolean checkRequirements, long completionTime) {
		Quest quest = null;

		if (!config.exists(id)) {
			player.sendMessage(ChatColor.RED + "No such quest [" + ChatColor.YELLOW + id + ChatColor.RED + "].");
			return null;
		}

		QuestLog questLog = questLogService.getQuestLogForPlayer(player);
		quest = questLog.getQuestByKey(id);

		if (quest == null) {
			quest = createAndAssignQuest(player, id, questLog, checkRequirements, completionTime);
		} else {
			if (completionTime > 0) {
				quest.setCompletionTime(completionTime);
			}
			
			if (quest.isCompleted() && quest.isDaily()) {
				if (!quest.isDailyCooldownOver()) {
					displayDailyResetMessage(player, quest);
				} else {
					Quest rootQuest = getRootQuest(quest, questLog);

					if (rootQuest.isDailyCooldownOver() && rootQuest.isCompleted()) {
						questLog.removeChildDailyQuests(rootQuest);
						rootQuest.reset();
					}
				}
			} else {
				displayCannotAssignQuestMessage(player);
			}
		}
		
		return quest;
	}

	private Quest getRootQuest(Quest quest, QuestLog questLog) {
		String requiredQuestKey = quest.getRequires();

		Quest requiredQuest = questLog.getQuestByKey(requiredQuestKey);

		return requiredQuest == null ? quest : getRootQuest(requiredQuest,
				questLog);
	}

	private void displayCannotAssignQuestMessage(Player player) {
		player.sendMessage(ChatColor.RED
				+ "This quest is completed and cannot be assigned.");
	}

	private Quest createAndAssignQuest(Player player, String id, QuestLog questLog, boolean checkRequirements, long completionTime) {
		String requiredQuests = config.getString(id + ".requires", "");
		Quest quest = null;

		if (!requiredQuests.isEmpty() && checkRequirements) {
			Quest requiredQuest = questLog.getQuestByKey(requiredQuests);
			if (requiredQuest != null && requiredQuest.isCompleted()) {
				quest = createAndAddToQuestLog(player, id, questLog, completionTime);
			} else {
				player.sendMessage(ChatColor.RED
						+ "You do not meet the requirement for that quest. Complete "
						+ ChatColor.YELLOW + requiredQuests + ChatColor.RED
						+ " first.");
			}
		} else {
			quest = createAndAddToQuestLog(player, id, questLog, completionTime);
		}
		
		return quest;
	}

	private Quest createAndAddToQuestLog(Player player, String id, QuestLog questLog, long completionTime) {
		Quest quest;
		quest = createQuest(player, id, completionTime);
		questLog.addQuest(quest);
		
		return quest;
	}

	private void displayDailyResetMessage(Player player, Quest quest) {
		Date availableDate = new Date(quest.getCompletionTime() + quest.getCooldown());
		Date current = new Date(System.currentTimeMillis());

		long diffInSeconds = (availableDate.getTime() - current.getTime()) / 1000;

		long diff[] = new long[] { 0, 0, 0, 0 };
		/* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60
				: diffInSeconds);
		/* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60
				: diffInSeconds;
		/* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24
				: diffInSeconds;
		/* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));

		player.sendMessage(ChatColor.LIGHT_PURPLE
				+ "You will be eligible to start this quest in "
				+ ChatColor.YELLOW + (diff[0] > 0 ? diff[0] + " days " : "")
				+ diff[1] + " hours " + diff[2] + " minutes");
	}

	private Quest createQuest(Player player, String id, long completionTime) {
		Quest quest;

		quest = new Quest(
					player, 
					id, 
					config.getString(id + ".name", ""),
					config.getString(id + ".requires", ""), 
					config.getString(id + ".messageStart", ""), 
					config.getString(id + ".messageEnd", ""), 
					config.getBoolean(id + ".daily", false),
					config.getLong(id + ".cooldown", DayInMillis));
		
		quest.setCompletionTime(completionTime);

		createRewards(quest, id);

		List<MapWrapper> objectives = config.getMapList(id + ".objectives", new ArrayList<MapWrapper>());
		for (MapWrapper map : objectives) {
			Objective objective = ObjectiveFactory.getInstance().createFromMap(this.questsPlugin, player, map, quest);
			quest.getObjectives().add(objective);
		}

		if (!quest.isCompleted()) {
			for (Objective objective : quest.getObjectives()) {
				objective.register(quest);
			}
		}
		
		return quest;
	}

	private void createRewards(Quest quest, String id) {
		// Level Rewards
		int level = config.getInt(id + ".rewards.level", 0);
		if (level > 0) {
			quest.getRewards().add(new LevelReward(level));
		}

		// Money Rewards
		int money = config.getInt(id + ".rewards.money", 0);
		if (money > 0) {
			quest.getRewards().add(
					new MoneyReward(this.questsPlugin.getEconomyProvider()
							.getProvider(), money));
		}

		// Items Rewards
		List<MapWrapper> items = config.getMapList(id + ".rewards.items",
				new ArrayList<MapWrapper>());
		if (items != null && items.size() > 0) {
			ItemsReward itemsReward = new ItemsReward();
			for (MapWrapper map : items) {
				int itemId = map.getInt("id", 0);

				if (itemId != 0) {
					itemsReward.getItems().add(
							new ItemReward(itemId, map.getInt("amount", 1)));
				}
			}

			quest.getRewards().add(itemsReward);
		}

		// Quest Rewards
		String questId = config.getString(id + ".rewards.quests", "");
		if (!questId.equals("")) {
			quest.getRewards().add(new QuestReward(this, questId));
		}
	}

	public void giveAll(Player player) {
		Set<String> set = config.getConfig().getKeys(false);
		
		for (String key : set) {
			assignQuest(player, key);
		}
	}
}

class RewardFactory {
	private static RewardFactory instance;

	public static RewardFactory getInstance() {
		if (instance == null) {
			instance = new RewardFactory();
		}

		return instance;
	}
}

class ObjectiveFactory {
	private static final String ObjectiveTypeCollect = "collect";
	private static final String ObjectiveTypeKill = "kill";
	private static final String ObjectiveTypeZone = "zone";
	private static final String ObjectiveTypeFind = "find";
	private static final String ObjectiveMonsterFury = "monsterfury";
	private static ObjectiveFactory instance;

	public static ObjectiveFactory getInstance() {
		if (instance == null) {
			instance = new ObjectiveFactory();
		}

		return instance;
	}

	public Objective createFromMap(Quests quests, Player player,
			MapWrapper map, Quest quest) {
		Objective objective = null;
		WorldZone zone = getWorldZone(quests, map);

		if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeKill)) {

			objective = createKillObjective(quests, player, map, zone);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeZone)) {

			objective = createZoneObjective(quests, player, map, zone);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeCollect)) {

			objective = createCollectObjective(quests, player, map, zone, quest);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeFind)) {

			objective = createFindObjective(quests, player, map, zone);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveMonsterFury)) {

		}

		return objective;
	}

	private FindObjective createFindObjective(Quests quests, Player player, MapWrapper map, WorldZone zone) {
		FindObjective objective = new FindObjective(player, zone, map.getString("name", ""), map.getInt("amount", 0), map.getInt("what", 0));

		quests.getPluginManager().registerEvents(objective, quests);
		return objective;
	}

	private CollectObjective createCollectObjective(Quests quests, Player player, MapWrapper map, WorldZone zone, Quest quest) {
		CollectObjective objective = new CollectObjective(player, zone,
				map.getString("name", "N/A"), map.getInt("amount", 0),
				map.getBoolean("keep", false), map.getInt("what", 0));

		if (!quest.isCompleted()) {
			quest.addListener(objective);
		}

		return objective;
	}

	private ZoneObjective createZoneObjective(Quests quests, Player player, MapWrapper map, WorldZone zone) {
		ZoneObjective objective = new ZoneObjective(player, zone,
				map.getString("name", ""), quests.getServer());

		ZoneServer.getInstance().addListener((ZoneObjective) objective);
		return objective;
	}

	private KillObjective createKillObjective(Quests quests, Player player, MapWrapper map, WorldZone zone) {
		List<Integer> entityIds = getEntities(map);

		KillObjective objective = new KillObjective(player, map.getString(
				"name", ""), zone, map.getInt("amount", 0), entityIds);

		quests.getServer().getPluginManager().registerEvents(objective, quests);
		return objective;
	}

	private List<Integer> getEntities(MapWrapper map) {
		List<Integer> entityIds = new ArrayList<Integer>();

		String ids = map.getString("what", "");

		if (ids != null && ids.length() > 0) {
			String[] entities = ids.split(",");

			for (int i = 0; i < entities.length; i++) {
				entityIds.add(Integer.parseInt(entities[i]));
			}
		}
		return entityIds;
	}

	private WorldZone getWorldZone(Quests quests, MapWrapper map) {
		WorldZone zone = null;
		String coords = map.getString("zone", "");
		String world = map.getString("world", "world");

		if (coords.split(",").length == 6) {
			zone = new WorldZone(quests.getServer().getWorld(world), coords);
		}
		return zone;
	}
}
