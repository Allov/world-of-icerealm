package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.qc.icerealm.bukkit.plugins.data.DataPersistenceService;
import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;
import ca.qc.icerealm.bukkit.plugins.quests.builder.ScriptedQuestService;
import ca.qc.icerealm.bukkit.plugins.quests.persistance.PersistedObjective;
import ca.qc.icerealm.bukkit.plugins.quests.persistance.PersistedQuest;
import ca.qc.icerealm.bukkit.plugins.quests.persistance.PersistedQuestLog;
import ca.qc.icerealm.bukkit.plugins.quests.persistance.QuestLogPersister;
import ca.qc.icerealm.bukkit.plugins.quests.persistance.QuestLogRepository;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLog;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class QuestsEventListener implements Listener {
	
	private final ScriptedQuestService scriptedQuestService;

	public QuestsEventListener(ScriptedQuestService scriptedQuestService) {
		this.scriptedQuestService = scriptedQuestService;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		player.sendMessage(	ChatColor.DARK_GREEN + "Type " + ChatColor.YELLOW + " /q " + ChatColor.DARK_GREEN + "to get a " + ChatColor.GREEN + "random quest " + 
							ChatColor.DARK_GREEN + "or " + ChatColor.YELLOW + "/q help " + ChatColor.DARK_GREEN + "for help.");

		removeAllListeners(player);
		registerQuests(player);
	}

	private void removeAllListeners(Player player) {
		QuestLog questLog = QuestLogService.getInstance().getQuestLogForPlayer(player);
		
		if (questLog != null) {
			questLog.unregisterQuests();
		}
		
		questLog.removeListener(QuestLogPersister.getInstance());
		QuestLogService.getInstance().removeQuestLog(player);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		removeAllListeners(player);
	}

	private void registerQuests(Player player) {
		QuestLogRepository repository = new QuestLogRepository(new DataSerializationService());
		PersistedQuestLog pql = repository.get(player.getName());
		
		if (pql != null) {
			for (PersistedQuest pq : pql.getQuests().values()) {
				Quest quest = scriptedQuestService.assignQuest(player, pq.getName(), false, pq.getCompletionTime());
				
				for (Objective objective : quest.getObjectives()) {
					for (PersistedObjective po : pq.getObjectives()) {
						if (objective.getName().equalsIgnoreCase(po.getName())) {
							objective.setCompleted(po.isCompleted());
							
							if (objective instanceof CountObjective) {
								((CountObjective)objective).setCurrent(po.getCurrentAmount());
							}
						}
					}
				}
			}
		}
	}
}
