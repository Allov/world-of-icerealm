package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;

import org.bukkit.entity.Player;

public interface Scenario {
	String getName();
	List<Player> getPlayers();
	void triggerScenario();
	void terminateScenario();
	void abortScenario();
	boolean isActive();
		
}
