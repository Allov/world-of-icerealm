package ca.qc.icerealm.bukkit.plugins.perks.warrior;

import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;

public class LastManStandingPerk implements Listener {
	private PerkService perkService = PerkService.getInstance();
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShoot(EntityDamageEvent evt) {
		
		boolean canLastMan = false;
		
		if (evt.getEntity() instanceof Player && perkService.playerHasPerk((Player)evt.getEntity(), AdventurerPerks.LastManStandingId)) {
			Player player = (Player)evt.getEntity();
			
			if (player.getHealth() > 10) {
				return;
			}
			long current = Calendar.getInstance().getTimeInMillis();
			
			if (cooldowns.containsKey(player.getName())) {
				if (current - cooldowns.get(player.getName()) > 60000) {
					cooldowns.put(player.getName(), current);
					canLastMan = true;
				}
			} else {
				cooldowns.put(player.getName(), current);
				canLastMan = true;
			}
			
			if (canLastMan) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*10, 2));
			}
		}
		
	}

}
