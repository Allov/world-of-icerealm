package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonPlayer implements Runnable {
	
	private Player[] _players;
	private int _duration = 50;
	private int _amplifier = 0;
	
	public PoisonPlayer(Player[] players, int duration, int amplifier) {
		_players = players;
		_duration = duration;
		_amplifier = amplifier;
	}

	@Override
	public void run() {
		
		//Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "Gigantic waves of radiation are hitting the ground!!!");
		for (Player p : _players) {
			if (p.getLocation().getY() > p.getLocation().getWorld().getSeaLevel()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, _duration, _amplifier));
			}
			
		}
	}
}
