package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.builder.ScriptedQuestService;

public class QuestReward implements Reward {

	private final ScriptedQuestService scriptedQuestService;
	private final String questName;

	public QuestReward(ScriptedQuestService scriptedQuestService, String questName) {
		this.scriptedQuestService = scriptedQuestService;
		this.questName = questName;
	}
	
	@Override
	public void give(Player player) {
		Quest quest = scriptedQuestService.assignQuest(player, getQuestName());
		if (quest != null) {
			quest.info();
		}
	}

	public String getQuestName() {
		return questName;
	}

	@Override
	public String toString() {
		return "" + ChatColor.YELLOW + this.getQuestName() + ChatColor.DARK_GREEN + " quest";
	}
}
