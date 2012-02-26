package ca.qc.icerealm.bukkit.plugins.quests.persistance;

import java.io.Serializable;

public class PersistedObjective implements Serializable {
	private static final long serialVersionUID = -6530121746079980005L;
	
	private String type;
	private String name;
	private String player;
	private boolean completed;
	private int currentAmount;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}
}
