package ca.qc.icerealm.bukkit.plugins.quests.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;
import ca.qc.icerealm.bukkit.plugins.quests.CountObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.QuestListener;
import ca.qc.icerealm.bukkit.plugins.quests.builder.ScriptedQuestService;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogListener;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class QuestLogPersister implements QuestListener, QuestLogListener, Listener {
	private static QuestLogPersister instance;
	public static QuestLogPersister getInstance() {
		if (instance == null) {
			instance = new QuestLogPersister(new QuestLogRepository(new DataSerializationService()));
		}
		
		return instance;
	}	
	
	private final QuestLogRepository questLogRepository;

	public QuestLogPersister(QuestLogRepository questLogRepository) {
		this.questLogRepository = questLogRepository;		
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
	
	private PersistedQuestLog getOrCreatePersistedQuestLog(String key) {
		PersistedQuestLog pql = questLogRepository.get(key);
		
		if (pql == null) {
			pql = new PersistedQuestLog();
		}
		
		return pql;		
	}

	@Override
	public void questAdded(Quest quest) {
		Player player = quest.getPlayer();
		PersistedQuestLog pql = getOrCreatePersistedQuestLog(player.getName());
		
		if (!pql.getQuests().containsKey(quest.getKey())) {
			pql.getQuests().put(quest.getKey(), mapPersistedQuest(quest));
			questLogRepository.save(pql, player.getName());
		}
		
		quest.addListener(this);
	}

	@Override
	public void randomQuestSet(Quest quest) {
		Player player = quest.getPlayer();
		PersistedQuestLog pql = getOrCreatePersistedQuestLog(player.getName());
		pql.setRandom(mapPersistedQuest(quest));
		questLogRepository.save(pql, player.getName());
	}

	@Override
	public void questCompleted(Quest quest) {
		Player player = quest.getPlayer();
		PersistedQuestLog pql = getOrCreatePersistedQuestLog(player.getName());		

		PersistedQuest pq = pql.getQuests().get(quest.getKey());
		pq.setCompletionTime(quest.getCompletionTime());
		pq.setObjectives(mapObjectives(quest.getObjectives()));
		
		questLogRepository.save(pql, player.getName());
	}

	@Override
	public void questProgressed(Quest quest, Objective objective) {
		Player player = quest.getPlayer();
		PersistedQuestLog pql = getOrCreatePersistedQuestLog(player.getName());
		
		PersistedQuest pq = pql.getQuests().get(quest.getKey());
		pq.setObjectives(mapObjectives(quest.getObjectives()));

		questLogRepository.save(pql, player.getName());
	}
}
