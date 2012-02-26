package ca.qc.icerealm.bukkit.plugins.quests.persistance;

import java.io.Serializable;
import java.util.List;

public class PersistedQuest implements Serializable {
	private static final long serialVersionUID = 4107023624359094824L;
	
	private String name;
	private String player;
	private long completionTime;
	private List<PersistedObjective> objectives;
	
	public long getCompletionTime() {
		return completionTime;
	}
	
	public void setCompletionTime(long completionTime) {
		this.completionTime = completionTime;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public void setPlayer(String player) {
		this.player = player;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<PersistedObjective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<PersistedObjective> objectives) {
		this.objectives = objectives;
	}
}
