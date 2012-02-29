package ca.qc.icerealm.bukkit.plugins.tools;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class EntityFollow implements TimeObserver, ZoneObserver {

	private long _alarm;
	private Villager _follower;
	private LivingEntity _followee;
	private WorldZone _zone;
	private boolean _isFollowing = false;
	private Location _startingPosition;
	
	public EntityFollow(LivingEntity e) {
		_follower = (Villager)e;
			
		
		_startingPosition = e.getLocation();
		_zone = new WorldZone(e.getLocation(), 4);
		timeHasCome(System.currentTimeMillis());
	}
	
	@Override
	public void timeHasCome(long time) {
		if (_isFollowing) {
			_follower.setTarget(_followee);
		}
		else {
			_follower.teleport(_startingPosition);
		}
		
		TimeServer.getInstance().addListener(this, 100);
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}

	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		_followee = p;
		_isFollowing = true;
	}

	@Override
	public void playerLeft(Player p) {
		
	}

	@Override
	public Server getCurrentServer() {
		return _follower.getServer();
	}

}
