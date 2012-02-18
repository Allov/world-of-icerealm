package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DefaultEventListener implements MonsterFuryEventsListener {

	private int _nbMonster;
	private int _nbMonsterLeft;
	private int _nbWaves;
	private List<Player> _player;
		
	@Override
	public void waveStarting(int monster) {
		_nbMonster = monster;
		sendMessageToPlayers("This wave has " + monster + " monsters!");
	}

	@Override
	public void waveIsDone(int done) {
		sendMessageToPlayers("This wave has been pushed back! " + (_nbWaves - done) + " waves left.");
	}

	@Override
	public void scenarioEnding(int wavedone) {
		sendMessageToPlayers("The enemy has been defeated! " + wavedone + " waves have been pushed back!");	
	}

	@Override
	public void scenarioAborting(int wavedone, Player p) {
		p.sendMessage("You did " + wavedone + "/" + _nbWaves);
		p.sendMessage(_nbMonsterLeft + " monsters left");
		p.sendMessage("Scenario has been aborted");
	}

	@Override
	public void coolDownChanged(boolean value, long time) {
		sendMessageToPlayers("Count down: " + value + ". time: " + time);
	}

	@Override
	public void playerDied(Player p, List<Player> players) {
		sendMessageToPlayers(p.getName() + " died! " + _player.size() + " still alive!");
	}

	@Override
	public void monsterDied(Entity e, int left) {
		_nbMonsterLeft = left;
		sendMessageToPlayers(_nbMonster - left + "/" + _nbMonster + " killed");
	}

	@Override
	public void scenarioStarting(int waves, List<Player> list) {
		_nbWaves = waves;
		_player = list;
		sendMessageToPlayers(waves + " waves are coming. We are " + _player.size() + " fighters");
	}
	
	@Override
	public void playerRewared(Player p, int exp) {
		p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.GOLD + String.valueOf(exp) + " level of XP!");
	}
	
	private void sendMessageToPlayers(String info) {
		for (Player p : _player) {
			p.sendMessage(info);
		}
	}

	@Override
	public void playerLeavingWithTimeout(Player p, long timeout) {
		p.sendMessage("You are leaving the zone. You have " + timeout + "ms to get back!");		
	}	
}
