package ca.qc.icerealm.bukkit.plugins.questslog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.CollectObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.QuestListener;
import ca.qc.icerealm.bukkit.plugins.quests.persistance.QuestLogPersister;

public class QuestLog {
	private Player player;
	private List<Quest> quests;
	private List<Quest> dailyQuests;
	private Quest randomQuest;
	
	private List<QuestLogListener> listeners = new CopyOnWriteArrayList<QuestLogListener>();
	
	public QuestLog(Player player) {
		this.player = player;
		this.quests = new ArrayList<Quest>();
		this.dailyQuests = new ArrayList<Quest>();
	}
	
	public void addListener(QuestLogListener questLogListener) {
		listeners.add(questLogListener);
	}
	
	public void removeListener(QuestLogListener questLogListener) {
		listeners.remove(questLogListener);
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
		if (key.equals("random")) {
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
		
		for(QuestLogListener questLogListener : listeners) {
			questLogListener.randomQuestSet(quest);
		}
	}
	
	public void addQuest(Quest quest) {
		if (quest.isDaily()) {
			this.dailyQuests.add(quest);
		} else {
			this.quests.add(quest);
		}

		for(QuestLogListener questLogListener : listeners) {
			questLogListener.questAdded(quest);
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

		player.sendMessage(ChatColor.LIGHT_PURPLE + "Cooldown Quest: ");
		for (Quest quest : this.dailyQuests) {
			displayQuestText(player, quest);
		}

		if (this.dailyQuests.size() == 0) {
			player.sendMessage("  > " + ChatColor.GRAY + "No cooldown quest in progress or completed.");
		}
	}

	private void displayQuestText(Player player, Quest quest) {
		player.sendMessage(	"  > " + ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + (quest.getRequires().length() > 0 ? quest.getRequires() + ChatColor.GRAY + "->" + ChatColor.YELLOW : "") + 
						    quest.getKey() + ChatColor.LIGHT_PURPLE +"] " + 
							ChatColor.DARK_GREEN + quest.getName() + " " + 
							(quest.isCompleted() ? ChatColor.GREEN + "Completed" : ChatColor.RED + "Not completed"));
	}
	
	public void unregisterQuests() {
		for (Quest quest : dailyQuests) {
			unregisterObjectives(quest);
			quest.removeListener(QuestLogPersister.getInstance());
		}

		for (Quest quest : quests) {
			unregisterObjectives(quest);
			quest.removeListener(QuestLogPersister.getInstance());
		}
		
		if (randomQuest != null) {
			unregisterObjectives(randomQuest);
			randomQuest.removeListener(QuestLogPersister.getInstance());
		}
	}

	private void unregisterObjectives(Quest quest) {
		for (Objective objective : quest.getObjectives()) {
			objective.unregister(quest);
			
			if (objective instanceof QuestListener) {
				quest.removeListener((QuestListener) objective);
			}
		}
	}

	public void removeChildDailyQuests(Quest rootQuest) {
		
		Quest[] quests = this.dailyQuests.toArray(new Quest[0]);
		for (Quest quest : quests) {
			if (quest.getRequires().equalsIgnoreCase(rootQuest.getKey())) {
				removeChildDailyQuests(quest);
				this.dailyQuests.remove(quest);
			}
		}
	}
}
