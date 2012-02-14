package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFuryEventsListener;


public class ScenarioPlugin extends JavaPlugin {
	
	MonsterFury _monster;
	
	@Override
	public void onDisable() {
		_monster.removeAllListener();
	}

	@Override
	public void onEnable() {
		_monster = new MonsterFury(this, new MyOwnEventListenenr());
		
		
	}
}

class MyOwnEventListenenr implements MonsterFuryEventsListener {

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
	public void scenarioAborting(int wavedone) {
		sendMessageToPlayers("You did " + wavedone + "/" + _nbWaves);
		sendMessageToPlayers(_nbMonsterLeft + " monsters left");
		sendMessageToPlayers("Scenario has been aborted");
	}

	@Override
	public void coolDownChanged(boolean value) {
		// rien a faire! 
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
	
	private void sendMessageToPlayers(String info) {
		for (Player p : _player) {
			p.sendMessage(info);
		}
	}
	
}
