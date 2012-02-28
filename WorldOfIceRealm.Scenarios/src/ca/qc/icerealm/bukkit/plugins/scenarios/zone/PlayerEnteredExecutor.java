package ca.qc.icerealm.bukkit.plugins.scenarios.zone;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class PlayerEnteredExecutor implements Runnable {

	private ZoneObserver _observer;
	private Player _player;
	public PlayerEnteredExecutor(ZoneObserver z, Player p) {
		_observer = z;
		_player = p;
	}
	
	@Override
	public void run() {
		_observer.playerEntered(_player);
	}

}
