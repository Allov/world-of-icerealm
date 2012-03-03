package ca.qc.icerealm.bukkit.plugins.quests.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;
import ca.qc.icerealm.bukkit.plugins.quests.CountObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.QuestListener;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLog;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class QuestLogPersister implements Runnable {
	private final QuestLogRepository questLogRepository;
	private final Server server;

	public QuestLogPersister(QuestLogRepository questLogRepository, Server server) {
		this.questLogRepository = questLogRepository;
		this.server = server;		
	}
	
	private PersistedQuest mapPersistedQuest(Quest quest) {
		PersistedQuest pq = new PersistedQuest();
		pq.setName(quest.getKey());
		pq.setPlayer(quest.getPlayer().getName());
		pq.setCompletionTime(quest.getCompletionTime());
		pq.setObjectives(mapObjectives(quest.getObjectives()));
		
		return pq;
	}

	private List<PersistedObjective> mapObjectives(List<Objective> objectives) {
		
		List<PersistedObjective> pobjs = new ArrayList<PersistedObjective>();
		
		for(Objective objective : objectives) {
			PersistedObjective po = mapPersistedObjective(objective);
			pobjs.add(po);
		}
		
		return pobjs;		
	}

	private PersistedObjective mapPersistedObjective(Objective objective) {
		PersistedObjective po = new PersistedObjective();
		
		if (objective instanceof CountObjective) {
			po.setCurrentAmount(((CountObjective)objective).getCurrent());
		}
		
		po.setName(objective.getName());
		po.setPlayer(objective.getPlayer().getName());
		po.setType(objective.getType());
		po.setCompleted(objective.isCompleted());
		
		return po;
	}
	
	private PersistedQuestLog mapPersistedQuestLog(QuestLog questLog) {
		PersistedQuestLog pql = new PersistedQuestLog();
		
		for (Quest quest : questLog.getDailyQuests()) {
			pql.setPlayer(questLog.getPlayerName());
			pql.getQuests().put(quest.getKey(), mapPersistedQuest(quest));
		}
		
		return pql;
	}


	private static final Object saveLock = new Object();

	public void saveQuestLog(QuestLog questLog) {
		if (questLog == null) return;
		
		PersistedQuestLog pql = mapPersistedQuestLog(questLog);
		
		synchronized(saveLock) {
			questLogRepository.save(pql, questLog.getPlayerName());
		}
	}

	@Override
	public void run() {
		for (Player player : server.getOnlinePlayers()) {
			QuestLog questLog = QuestLogService.getInstance().getQuestLogForPlayer(player.getName());
			saveQuestLog(questLog);
		}
	}
}
