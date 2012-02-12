package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class BloodMoonStarter implements TimeObserver {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm = 0;
	private BloodMoon _moon;
	
	public BloodMoonStarter(BloodMoon moon) {
		_moon = moon;
	}
	
	@Override
	public void timeHasCome(long time) {
		if (_moon.isActive()) {
			_moon.getServer().broadcastMessage(ChatColor.DARK_GREEN + "The monsters are " + ChatColor.RED + " very close!");
			for (Player p : _moon.getServer().getOnlinePlayers()) {
				_moon.spawnMonsterCloseToPlayer(p.getLocation());
				_moon.spawnMonsterCloseToPlayer(p.getLocation());
			}
		}			
	}

	@Override
	public void setAlaram(long time) {
		// TODO Auto-generated method stub
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}

}
