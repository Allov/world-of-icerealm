package ca.qc.icerealm.bukkit.plugins.perks.warrior;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;

public class LifeLeechPerk implements Listener {
	
	private PerkService perkService = PerkService.getInstance();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShoot(EntityDamageByEntityEvent evt) {
		
		if (evt.getDamager() instanceof Player && perkService.playerHasPerk((Player)evt.getDamager(), AdventurerPerks.LifeLeechId)) {
			Player player = (Player)evt.getDamager();
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 0));
		}
		
	}

}
