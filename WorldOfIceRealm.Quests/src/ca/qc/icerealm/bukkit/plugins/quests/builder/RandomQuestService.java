package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.quests.Fees;
import ca.qc.icerealm.bukkit.plugins.quests.ItemReward;
import ca.qc.icerealm.bukkit.plugins.quests.KillObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.Reward;
import ca.qc.icerealm.bukkit.plugins.quests.Quests;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLog;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class RandomQuestService {

	private static final int ItemRewardChances = 10;

	private final int[] Creatures = new int[] { EntityUtilities.Zombie, EntityUtilities.Creeper, EntityUtilities.Spider, EntityUtilities.Skeleton, EntityUtilities.Enderman };
	
	private final int MaxObjective = 3;
	private final int MaxKillCount = 10;
	private final int BaseLevelReward = 1;
	private final int BaseMoneyReward = 100;
	private final ItemReward[] BaseItemRewards = new ItemReward[] { 
			new ItemReward(Material.GOLDEN_APPLE.getId(), 5), 
			new ItemReward(Material.IRON_CHESTPLATE.getId(), 1), 
			new ItemReward(Material.IRON_LEGGINGS.getId(), 1), 
			new ItemReward(Material.IRON_BOOTS.getId(), 1), 
			new ItemReward(Material.IRON_HELMET.getId(), 1), 
			}; 
	
	private final ca.qc.icerealm.bukkit.plugins.quests.Quests questsPlugin;
	private final QuestLogService questLogService;
	private Random random;
	
	public RandomQuestService(Quests questsPlugin, QuestLogService questLogService) {
		this.questsPlugin = questsPlugin;
		this.questLogService = questLogService;
		this.random = new Random(Calendar.getInstance().getTimeInMillis());
	}

	public Quest getQuest(Player player) {
		QuestLog questLog = questLogService.getQuestLogForPlayer(player);
		if (!questLog.isRandomQuestFinished()) {
			return null;
		}
		
		int objectivesCount = random.nextInt(MaxObjective-1)+1;
		List<KillObjective> objectives = new ArrayList<KillObjective>();
		List<Integer> creaturesToKill;

		creaturesToKill = createCreatureToKillList(objectivesCount);
		Reward reward = generateReward(objectivesCount);
		
		String questName = getQuestName(creaturesToKill);
		Quest quest = createQuest(player, reward, questName, objectivesCount);
		createObjectives(player, objectivesCount, objectives, creaturesToKill, quest);
		
		questLog.setRandomQuest(quest);

		return quest;
		
	}

	private Quest createQuest(Player player, Reward reward, String questName, int objectivesCount) {
		Quest quest = new Quest(
				player, 
				"random",
				questName, 
				ChatColor.DARK_GREEN + "Hey you there with the blocky head, kill " + ChatColor.GREEN + MaxKillCount / objectivesCount + " " + ChatColor.YELLOW + questName + ChatColor.DARK_GREEN + " for me and I'll pay you good!", 
				ChatColor.DARK_GREEN + "Thanks! Here's what you were waiting for... make good use of it!", 
				false, 
				new Fees(0, 0, 0, 0),
				new Fees(0, 0, 0, 0),
				reward);
		return quest;
	}

	private void createObjectives(Player player, int objectivesCount, List<KillObjective> objectives, List<Integer> creaturesToKill, Quest quest) {
		for (int i = 0; i < objectivesCount; i++) {
			int amount = MaxKillCount / objectivesCount;
			KillObjective objective = new KillObjective(player, "Kill " +  amount + " " + EntityUtilities.getEntityName(creaturesToKill.get(i)), null, amount, creaturesToKill.get(i));
			objective.register(quest);
			objectives.add(objective);
			this.questsPlugin.getServer().getPluginManager().registerEvents(objective, this.questsPlugin);
		}

		quest.getObjectives().addAll(objectives);
	}

	private Reward generateReward(int objectivesCount) {
		Reward reward = new Reward(
				BaseLevelReward, 
				BaseMoneyReward, 
				this.questsPlugin.getEconomyProvider().getProvider());
		
		if (RandomUtil.getDrawResult(ItemRewardChances)) {
			int rewardId = random.nextInt(BaseItemRewards.length-1);
			reward.getItems().add(BaseItemRewards[rewardId]);
		}
		
		return reward;
	}

	private List<Integer> createCreatureToKillList(int objectivesCount) {
		List<Integer> availableCreatures = new ArrayList<Integer>();
		List<Integer> creaturesToKill = new ArrayList<Integer>();
		
		for(int i = 0; i < Creatures.length; i++) {
			availableCreatures.add(Creatures[i]);
		}

		int creature = getCreatureIdToKill(availableCreatures);
		creaturesToKill.add(creature);

		for (int i = 1; i < objectivesCount; i++) {
			creature = getCreatureIdToKill(availableCreatures);
			creaturesToKill.add(creature);
		}
		
		return creaturesToKill;
	}

	private String getQuestName(List<Integer> creaturesToKill) {
		String questName = "";
		for (int i = 0; i < creaturesToKill.size(); i++) {
			questName += EntityUtilities.getEntityName(creaturesToKill.get(i));
			if (creaturesToKill.size() > 1 && i < creaturesToKill.size()-1) {
				questName += " and ";
			}
		}
		
		return questName;
	}

	private int getCreatureIdToKill(List<Integer> availableCreatures) {
		int pos = random.nextInt(availableCreatures.size()-1);
		int creature = availableCreatures.get(pos);
		availableCreatures.remove(pos);
		return creature;
	}	
}
