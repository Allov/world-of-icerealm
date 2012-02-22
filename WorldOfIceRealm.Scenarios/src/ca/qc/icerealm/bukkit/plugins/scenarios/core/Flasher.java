package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class Flasher implements TimeObserver {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm;
	
	private boolean _showIt = false;
	private Location _location;
	private World _world;
	
	public Flasher(Location l) {
		_location = l;
		_world  = l.getWorld();
	}
	
	
	@Override
	public void timeHasCome(long time) {
		
		Block b = _world.getBlockAt(_location);
		if (_showIt) {
			_showIt = false;
			b.setType(Material.GLOWSTONE);
		}
		else {
			_showIt = true;
			b.setType(Material.AIR);
		}
		
		TimeServer.getInstance().addListener(this, 1000);
	}

	@Override
	public void setAlaram(long time) {
		_alarm  = time;
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}

	
}
