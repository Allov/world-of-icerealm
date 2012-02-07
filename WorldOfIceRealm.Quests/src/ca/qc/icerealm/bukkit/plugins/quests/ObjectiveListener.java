package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.entity.Entity;

public interface ObjectiveListener {
	void objectiveProgressed(Objective objective, Entity entity);
	void objectiveFailed(Objective objective);
	void objectiveDone(Objective objective);
}
