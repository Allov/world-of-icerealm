package ca.qc.icerealm.bukkit.plugins.questslog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestLogService {
	
	private static QuestLogService instance;
	public static QuestLogService getInstance() {
		if (instance == null) {
			instance = new QuestLogService();
		}
		
		return instance;
	}
	
	private List<QuestLog> questLogs;
	
	public QuestLogService() {
		this.questLogs = new ArrayList<QuestLog>();
	}
	
	public QuestLog getQuestLogForPlayer(Player player) {
		for (QuestLog questLog : this.questLogs) {
			if (questLog.getPlayer() == player) {
				return questLog;
			}
		}
		
		QuestLog questLog = new QuestLog(player);
		this.questLogs.add(questLog);		
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
}
