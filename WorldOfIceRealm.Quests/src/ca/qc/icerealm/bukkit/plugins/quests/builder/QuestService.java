package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.List;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.Quest;

public interface QuestService {
	void LoadQuests();
	Quest getQuest(Player player);
}
