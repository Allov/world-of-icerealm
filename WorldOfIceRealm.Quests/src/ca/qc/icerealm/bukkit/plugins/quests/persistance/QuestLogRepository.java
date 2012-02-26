package ca.qc.icerealm.bukkit.plugins.quests.persistance;

import ca.qc.icerealm.bukkit.plugins.data.DataPersistenceService;

public class QuestLogRepository {
	
	private DataPersistenceService dataPersistenceService;
	
	public QuestLogRepository(DataPersistenceService dataPersistenceService) {
		this.dataPersistenceService = dataPersistenceService;		
	}
	
	public PersistedQuestLog get(String key) {
		return (PersistedQuestLog)dataPersistenceService.load("quests", key);
	}
	
	public void save(PersistedQuestLog questLog, String key) {
		dataPersistenceService.save("quests", key, questLog);
	}
}
