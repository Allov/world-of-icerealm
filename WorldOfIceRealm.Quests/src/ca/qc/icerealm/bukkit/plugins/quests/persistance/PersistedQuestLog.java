package ca.qc.icerealm.bukkit.plugins.quests.persistance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistedQuestLog implements Serializable {
	private static final long serialVersionUID = -7829185117563794085L;
	
	private String player;
	private HashMap<String, PersistedQuest> quests;
	private PersistedQuest random;
	
	public String getPlayer() {
		return player;
	}
	
	public void setPlayer(String player) {
		this.player = player;
	}

	public Map<String, PersistedQuest> getQuests() {
		if (quests == null) {
			quests = new HashMap<String, PersistedQuest>();
		}
		
		return quests;
	}

	public PersistedQuest getRandom() {
		return random;
	}

	public void setRandom(PersistedQuest random) {
		this.random = random;
	}
}
