package ca.qc.icerealm.bukkit.plugins.scenarios.zone;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class PlayerLeftExecutor implements Runnable {

	private ZoneObserver _observer;
	private Player _player;
	
	public PlayerLeftExecutor(ZoneObserver z, Player p) {
		_observer = z;
		_player = p;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		_observer.playerLeft(_player);
	}

}
