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
		return randomQuest == null || randomQuest.isCompleted();
	}

	public Player getPlayer() {
		return player;
	}

	public Quest getRandomQuest() {
		return this.randomQuest; 
	}
	
	public Quest getQuestByKey(String key) {
		if (key == "random") {
			return randomQuest;
		}		
		
		for (Quest quest : this.quests) {
			if (quest.getKey().equals(key)) {
				return quest;
			}
		}

		for (Quest quest : this.dailyQuests) {
			if (quest.getKey().equals(key)) {
				return quest;
			}
		}
		
		return null;
	}
	
	public void setRandomQuest(Quest quest) {
		this.randomQuest = quest;
	}
	
	public void addQuest(Quest quest) {
		if (quest.isDaily()) {
			this.dailyQuests.add(quest);
		} else {
			this.quests.add(quest);
		}
	}

	public void display(Player player) {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Random Quest: ");
		if (this.randomQuest != null) {
			displayQuestText(player, this.randomQuest);
		} else {
			player.sendMessage("  > " + ChatColor.GRAY + "No random quest in progress. Type /q to get one.");
		}
		
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Quest: ");
		for (Quest quest : this.quests) {
			displayQuestText(player, quest);
		}
		
		if (this.quests.size() == 0) {
			player.sendMessage("  > " + ChatColor.GRAY + "No quest in progress or completed.");
		}

		player.sendMessage(ChatColor.LIGHT_PURPLE + "Daily Quest: ");
		for (Quest quest : this.dailyQuests) {
			displayQuestText(player, quest);
		}

		if (this.dailyQuests.size() == 0) {
			player.sendMessage("  > " + ChatColor.GRAY + "No daily quest in progress or completed.");
		}
	}

	private void displayQuestText(Player player, Quest quest) {
		player.sendMessage(	"  > " + ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + quest.getKey() + ChatColor.LIGHT_PURPLE +"] " + 
							ChatColor.DARK_GREEN + quest.getName() + " " + 
							(quest.isCompleted() ? ChatColor.GREEN + "Completed" : ChatColor.RED + "Not completed"));
	}
}
