package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.World;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class Explosion implements TimeObserver {

	private long _alarm;
	private WorldZone _zone;
	private World _world;
	private int _limit;
	private int _nbExplosion;
	
	public Explosion(WorldZone z, World w) {
		_limit = 1;
		_zone = z;
		_world = w;
		_nbExplosion = 0;
	}
	
	public Explosion(WorldZone z, World w, int limit) {
		_limit = limit;
		_zone = z;
		_world = w;
		_nbExplosion = 0;
	}
	
	@Override
	public void timeHasCome(long time) {
		_limit++;
		_world.createExplosion(_zone.getRandomLocation(_world), 5.0f);
		if (_nbExplosion <= _limit) {
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
