package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class PlayerOutTimer implements TimeObserver {

	private long _alarm;
	private Player _player;
	private WorldZone _zone;
	private Scenario _scenario;
	
	public PlayerOutTimer(Player p, WorldZone z, Scenario s) {
		_player = p;
		_zone = z;
		_scenario = s;
	}
	
	public Player getPlayer() {
		return _player;
	}
	
	@Override
	public void timeHasCome(long time) {
		if (!_zone.isInside(_player.getLocation())) {
			_scenario.removePlayerFromScenario(_player);
		}
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;		
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}

}
