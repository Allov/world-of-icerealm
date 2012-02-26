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
	
	public boolean isQuestLogCreated(Player player) {
		return questLogs.containsKey(player.getName());
	}
	
	public QuestLog getQuestLogForPlayer(Player player) {
		QuestLog questLog = null;
		
		if (!isQuestLogCreated(player)) {
			questLog = new QuestLog(player);
			this.questLogs.put(player.getName(), questLog);
			
			questLog.addListener(QuestLogPersister.getInstance());
		} else {
			questLog = questLogs.get(player.getName());
		}
		
		return questLog;		
	}

	public void displayLogForPlayer(Player player) {
		QuestLog questLog = getQuestLogForPlayer(player);
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
