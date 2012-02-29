package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import org.bukkit.Location;
import org.bukkit.World;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class Explosion implements TimeObserver {

	private long _alarm;
	private WorldZone _zone;
	private World _world;
	private int _limit;
	
	public Explosion(WorldZone z, World w) {
		_limit = 0;
		_zone = z;
		_world = w;
	}
	
	@Override
	public void timeHasCome(long time) {
		_limit++;
		_world.createExplosion(_zone.getRandomLocation(_world), 5.0f);
		if (_limit < 5) {
			TimeServer.getInstance().addListener(this, 250);
		}
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;		
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}

}
