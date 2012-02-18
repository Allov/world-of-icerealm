package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;

public class DefaultEventListener implements ScenarioEventsListener {

	private int _nbMonster;
	private int _nbWaves;
	private List<Player> _player;
	private int _totalMonsterKilled = 0;
		
	@Override
	public void waveStarting(int monster, String[] types) {
		_nbMonster = monster;
		sendMessageToPlayers(ChatColor.GREEN + "Monster Wave is coming:");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(monster) + ChatColor.GREEN + " monsters");
		
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < types.length; i++) {
			buf.append(EntityUtilities.getCreatureType(types[i]).toString());
			if (i < types.length - 1) {
				buf.append(", ");
			}
		}
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + "Contains: " + ChatColor.LIGHT_PURPLE + buf.toString());
	}

	@Override
	public void waveIsDone(int done) {
		sendMessageToPlayers(ChatColor.GREEN + "Scenario progress:");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(done) + ChatColor.GOLD + " waves done");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.GREEN + String.valueOf( (_nbWaves - done)) + ChatColor.DARK_GREEN + " waves left");
	}

	@Override
	public void scenarioEnding(int wavedone) {
		sendMessageToPlayers(ChatColor.GREEN + "Scenario finished:");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(wavedone) + " waves " + ChatColor.LIGHT_PURPLE + " pushed back.");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(_totalMonsterKilled) + " monsters " + ChatColor.LIGHT_PURPLE + "killed");
	}

	@Override
	public void scenarioAborting(int wavedone, Player p) {
		sendMessageToPlayers(ChatColor.GREEN + "Scenario aborted:");
		p.sendMessage(" " + ChatColor.GRAY + "> " + ChatColor.LIGHT_PURPLE + "You are the last player to leave the zone");
	}

	@Override
	public void coolDownChanged(boolean value, long time, Scenario s) {
		
		if (!value) {
			s.getScenarioServer().broadcastMessage(ChatColor.GREEN + "[" + s.getName() + "] " + ChatColor.LIGHT_PURPLE + " Scenario can be activated now");	
		}
		if (value) {
			s.getScenarioServer().broadcastMessage(ChatColor.GREEN + "[" + s.getName() + "] " + ChatColor.LIGHT_PURPLE + " Scenario will be available in " + ChatColor.YELLOW + (time) + " ms");	
		}
		
		
	}

	@Override
	public void playerDied(Player p, List<Player> players) {
		sendMessageToPlayers(ChatColor.GREEN + "Scenario progress:");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.RED + p.getName() + ChatColor.LIGHT_PURPLE + " died");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(players.size()) + ChatColor.LIGHT_PURPLE + " still alive");
	}

	@Override
	public void monsterDied(Entity e, int left) {
		_totalMonsterKilled++;
		if ((left == _nbMonster - 1) || (left % 5) == 0 || left < 3) {
			sendMessageToPlayers(ChatColor.GREEN + "Scenario progress: " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf((_nbMonster - left)) + "/" + _nbMonster + ChatColor.LIGHT_PURPLE + " monsters killed");
		}
		
		
	}

	@Override
	public void scenarioStarting(int waves, List<Player> list) {
		_totalMonsterKilled = 0;
		_nbWaves = waves;
		_player = list;
		sendMessageToPlayers(ChatColor.GREEN + "Scenario is starting:");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + waves + ChatColor.LIGHT_PURPLE + " waves detected");
		sendMessageToPlayers(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(list.size()) + ChatColor.LIGHT_PURPLE + " players fighting");
	}
	
	@Override
	public void playerRewared(Player p, int exp) {
		sendMessageToPlayers(ChatColor.GREEN + "Scenario reward:");
		p.sendMessage(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(exp) + " level of XP!");
	}
	
	private void sendMessageToPlayers(String info) {
		for (Player p : _player) {
			p.sendMessage(info);
		}
	}

	@Override
	public void playerLeavingWithTimeout(Player p, long timeout) {
		p.sendMessage(ChatColor.GREEN + "You are leaving the scenario zone.");
		p.sendMessage(" " + ChatColor.GRAY + "> " + ChatColor.YELLOW + String.valueOf(timeout / 1000) + " seconds " + ChatColor.LIGHT_PURPLE + "until you're kicked out");		
	}

	@Override
	public void playerRemoved(Player p, Scenario s) {
		p.sendMessage(ChatColor.GREEN + "[" + s.getName() + "] " + ChatColor.LIGHT_PURPLE + "You are outside the scenario zone.");
	}

	@Override
	public void playerAdded(Player p, Scenario s) {
		p.sendMessage(ChatColor.GREEN + "[" + s.getName() + "] " + ChatColor.LIGHT_PURPLE + " You are inside a scenario zone.");
		p.sendMessage(ChatColor.GREEN + "[" + s.getName() + "] " + ChatColor.LIGHT_PURPLE + " Cool Down Active: " + ChatColor.YELLOW + String.valueOf(s.isCooldownActive()));
	}
}
