package ca.qc.icerealm.bukkit.plugins.questslog;

import java.util.List;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.Quest;

public class QuestLog {
	private Player player;
	private List<Quest> quests;
	private List<Quest> dailyQuests;
	private Quest randomQuest;
	
	public QuestLog(Player player) {
		this.player = player;
	}
	
	public boolean isRandomQuestFinished() {
		return randomQuest == null || randomQuest.isDone();
	}

	public Player getPlayer() {
		return player;
	}

	public Quest getRandomQuest() {
		return this.randomQuest; 
	}
	
	public void setRandomQuest(Quest quest) {
		this.randomQuest = quest;
	}
}
