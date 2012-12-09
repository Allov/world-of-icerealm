package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.tools.TimeFormatter;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class ZoneTrigger implements ZoneObserver {

	private Logger _logger = Logger.getLogger("Minecraft");
	private List<MonsterSpawner> _runnable;
	private Server _server;
	private WorldZone _zone;
	private boolean _started = false;
	private List<Player> _players;
	private long _coolDown = 0;
	private boolean _lootCreated = false;
	
	public ZoneTrigger(List<MonsterSpawner> run, Server s) {
		_runnable = run;
		_server = s;		
		_players = new ArrayList<Player>();
	}
	
	public void setLootCreated(boolean b) {
		_lootCreated = b;
	}
	
	public void setCoolDown(long cool) {
		_coolDown = cool;
	}
	
	public void setPlayerList(List<Player> players) {
		_players = players;
	}
	
	@Override
	public Server getCurrentServer() {
		return _server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player arg0) {
		//_logger.info("playe entered: " + _started);
		
		if (!_players.contains(arg0)) {
			_players.add(arg0);
			//_logger.info("player added!");
		}
		
		//_logger.info("zonetrigger player size: " + _players.size());
		if (!_started) {
			arg0.sendMessage(ChatColor.GREEN + "You just entered in a" + ChatColor.RED + " dangerous area." + ChatColor.GREEN + " Kill 80% of the monsters!");
			for (Runnable r : _runnable) {
				r.run();
			}
			_started = true;
		}
		
		else if (_lootCreated) {
			long timeLeft = (_coolDown - System.currentTimeMillis());
			if (timeLeft < 0) {
				timeLeft = 0;
			}
			arg0.sendMessage(ChatColor.YELLOW + "This area has been already" + ChatColor.GOLD + " cleared!" + ChatColor.YELLOW + " Come back in " + 
							 ChatColor.GREEN + TimeFormatter.readableTime(timeLeft));
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		//_players.remove(arg0);
	}

	@Override
	public void setWorldZone(WorldZone arg0) {
		_zone = arg0;
	}
	
	public void setActivate(boolean a) {
		_started = a;
		//_logger.info("started in zonetrigger = " + _started);
		for (MonsterSpawner s : _runnable) {
			s.setActivate(a);
		}
	}

}
