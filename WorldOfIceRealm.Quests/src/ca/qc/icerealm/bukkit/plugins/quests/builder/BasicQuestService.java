package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.quests.Fees;
import ca.qc.icerealm.bukkit.plugins.quests.ItemReward;
import ca.qc.icerealm.bukkit.plugins.quests.KillObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.Reward;
import ca.qc.icerealm.bukkit.plugins.quests.Quests;

public class BasicQuestService implements QuestService {

	private final int[] Creatures = new int[] { EntityUtilities.Zombie, EntityUtilities.Creeper, EntityUtilities.Spider, EntityUtilities.Skeleton, EntityUtilities.Enderman };
	private final int MaxObjective = 3;
	private final int MaxKillCount = 1;
	private final ca.qc.icerealm.bukkit.plugins.quests.Quests questsPlugin;
	
	public BasicQuestService(Quests questsPlugin) {
		this.questsPlugin = questsPlugin;
	}

	@Override
	public void LoadQuests() {
	}

	@Override
	public Quest getQuest(Player player) {
		Random random = new Random(Calendar.getInstance().getTimeInMillis());

		int objectivesCount = random.nextInt(MaxObjective-1)+1;
		List<KillObjective> objectives = new ArrayList<KillObjective>();
		List<Integer> creaturesToKill = new ArrayList<Integer>();
		String questName = "";
		
		int pos = random.nextInt(Creatures.length-1);
		creaturesToKill.add(Creatures[pos]);
		questName += EntityUtilities.getEntityName(Creatures[pos]);

		for (int i = 1; i < objectivesCount; i++) {
			pos = random.nextInt(Creatures.length-1);
			creaturesToKill.add(Creatures[pos]);
			questName += " and " + EntityUtilities.getEntityName(Creatures[pos]);
		}
		
		Reward reward = new Reward(10, 10000, this.questsPlugin.getEconomyProvider().getProvider());
		reward.getItems().add(new ItemReward(276, 2));
		
		Quest quest = new Quest(player, 
				questName, 
				"Kill " + MaxKillCount + " " + questName, 
				"Thanks!", 
				false, 
				new Fees(0, 0, 0, 0),
				new Fees(0, 50, 0, 0),
				reward);
		
		for (int i = 0; i < objectivesCount; i++) {
			KillObjective objective = new KillObjective(quest, null, MaxKillCount, creaturesToKill.get(i));
			objectives.add(objective);
			this.questsPlugin.getServer().getPluginManager().registerEvents(objective, this.questsPlugin);
		}
		
		quest.getObjectives().addAll(objectives);

		return quest;
		
	}	
}
