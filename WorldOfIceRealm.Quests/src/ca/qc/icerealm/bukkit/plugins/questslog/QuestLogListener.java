package ca.qc.icerealm.bukkit.plugins.questslog;

import ca.qc.icerealm.bukkit.plugins.quests.Quest;

public interface QuestLogListener {
	void randomQuestSet(Quest quest);
	void questAdded(Quest quest);
}
