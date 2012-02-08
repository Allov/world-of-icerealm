package ca.qc.icerealm.bukkit.plugins.questslog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.Quest;

public class QuestLog {
	private Player player;
	private List<Quest> quests;
	private List<Quest> dailyQuests;
	private Quest randomQuest;
	
	public QuestLog(Player player) {
		this.player = player;
		this.quests = new ArrayList<Quest>();
		this.dailyQuests = new ArrayList<Quest>();
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

	public void display(Player player) {
		if (this.randomQuest != null) {
			String prefix = ChatColor.LIGHT_PURPLE + "Random Quest: ";
			displayQuestText(player, prefix, this.randomQuest);
		}
		
		for (Quest quest : this.quests) {
			String prefix = ChatColor.LIGHT_PURPLE + "Quest: ";
			displayQuestText(player, prefix, quest);
		}
	}

	private void displayQuestText(Player player, String prefix, Quest quest) {
		player.sendMessage(prefix + ChatColor.YELLOW + quest.getName() + 
				(this.randomQuest.isDone() ? ChatColor.GREEN + "Completed" : ChatColor.RED + "Not completed"));
	}
}
