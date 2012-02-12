package ca.qc.icerealm.bukkit.plugins.quests;


public interface ObjectiveListener {
	void objectiveProgressed(Objective objective);
	void objectiveFailed(Objective objective);
	void objectiveCompleted(Objective objective);
}
