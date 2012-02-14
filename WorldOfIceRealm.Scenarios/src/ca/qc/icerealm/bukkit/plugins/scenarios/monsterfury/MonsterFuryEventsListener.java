package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface MonsterFuryEventsListener {
	void waveStarting(int monster);
	void waveIsDone(int done);
	void scenarioStarting(int waves, List<Player> list);
	void scenarioEnding(int wavedone);
	void scenarioAborting(int wavedone);
	void coolDownChanged(boolean value);
	void playerDied(Player p,  List<Player> players);
	void monsterDied(Entity e, int left);
}
