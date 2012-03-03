package ca.qc.icerealm.bukkit.plugins.questslog;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.persistance.QuestLogPersister;

public class QuestLogService {
	
	private static QuestLogService instance;
	public static QuestLogService getInstance() {
		if (instance == null) {
			instance = new QuestLogService();
		}
		
		return instance;
	}
	
	private Map<String, QuestLog> questLogs;
	
	public QuestLogService() {
		this.questLogs = new HashMap<String, QuestLog>();
	}
	
	public boolean isQuestLogCreated(String key) {
		return questLogs.containsKey(key);
	}
	
	public QuestLog getQuestLogForPlayer(String key) {
		QuestLog questLog = null;
		
		if (!isQuestLogCreated(key)) {
			questLog = new QuestLog(key);
			this.questLogs.put(key, questLog);
		} else {
			questLog = questLogs.get(key);
		}
		
		return questLog;		
	}

	public void displayLogForPlayer(Player player) {
		QuestLog questLog = getQuestLogForPlayer(player.getName());
		if (questLog != null) {
			questLog.display(player);
		} else {
			player.sendMessage(ChatColor.RED + "You are not on any quest at the moment.");
		}
	}

	public void removeQuestLog(Player player) {
		questLogs.remove(player.getName());
	}
}
