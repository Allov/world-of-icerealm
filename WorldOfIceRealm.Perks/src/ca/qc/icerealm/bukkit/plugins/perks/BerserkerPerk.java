package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BerserkerPerk implements Listener {
	
	private PerkService perkService = PerkService.getInstance();
	private HashMap<Player, Double> players = new HashMap<Player, Double>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageByEntityEvent evt) {
		if (evt.getDamager() instanceof Player && perkService.playerHasPerk((Player)evt.getDamager(), AdventurerPerks.BerserkerId)) {
			Player damager = (Player)evt.getDamager(); 
			if (players.containsKey(damager)) {
				if (damager.getLocation().getY() < players.get(damager)) {
					damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 0));
				}
			}
				
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent evt) {
		Player player = evt.getPlayer();
		players.put(player, player.getLocation().getY());
	}
}

