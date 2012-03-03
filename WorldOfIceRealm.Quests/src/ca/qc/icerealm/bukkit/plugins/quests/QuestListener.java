package ca.qc.icerealm.bukkit.plugins.quests;

public interface QuestListener {
	void questProgressed(Quest quest, Objective objective);
	void questCompleted(Quest quest);
	void questReseted(Quest quest);
}
