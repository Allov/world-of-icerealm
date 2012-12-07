package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Arena extends BarbarianRaid {
	
	private Logger _logger = Logger.getLogger("Minecraft");
	protected int _experienceReward = 10;
	
	protected void generateLoot() {
		
		int xp = 0;
		if (_players.size() != 0) {
			xp = _experienceReward / _players.size();
		}
		
		for (Player p : _players) {
			p.setLevel(p.getLevel() + xp);
			p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + xp + " level " + ChatColor.GREEN + "of experience");
		}
	}
	
	protected void processEndEvent() {
		super.processEndEvent();
		Executors.newSingleThreadScheduledExecutor().schedule(new ArenaActivator(this), INTERVAL_BETWEEN_ATTACK, TimeUnit.SECONDS);
	}
		
	protected void applyConfiguration() {
		super.applyConfiguration();
		
		String config = getConfiguration();
		if (config != null) {
			String[] data = config.split(",");
			if (data.length > 7) { // on veut pogner le param addiotionnel
				_experienceReward = Integer.parseInt(data[7]);
			}
		}
	}
	
	protected void welcomeMessage(Player arg0) {
		if (!_activated) {
			arg0.sendMessage(ChatColor.YELLOW + "This arena is empty, " + ChatColor.GOLD + "come back later.");
		}
		else {
			arg0.sendMessage(ChatColor.YELLOW + "This place looks like a" + ChatColor.GOLD + " gladiator arena...");	
		}
	}
	
	public String getName() {
		return "arena";
	}
	
}


class ArenaActivator implements Runnable {

	private Logger _logger = Logger.getLogger("Minecraft");
	private Arena _raid;
	
	public ArenaActivator(Arena r) {
		_raid = r;
	}
	
	@Override
	public void run() {
		_raid._activated = true;
		_raid._waveDone = 0;
		_raid._players.clear();
	}
	
}