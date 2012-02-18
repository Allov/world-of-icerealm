package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface ScenarioEventsListener {
	void waveStarting(int monster, String[] type);
	void waveIsDone(int done);
	void scenarioStarting(int waves, List<Player> list);
	void scenarioEnding(int wavedone);
	void scenarioAborting(int wavedone, Player p);
	void coolDownChanged(boolean value, long time, Scenario s);
	void playerDied(Player p,  List<Player> players);
	void monsterDied(Entity e, int left);
	void playerRewared(Player p, int exp);
	void playerLeavingWithTimeout(Player p, long timeout);
	void playerRemoved(Player p, Scenario s);
	void playerAdded(Player p, Scenario s);
}
