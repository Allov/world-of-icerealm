package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MeatShieldPerk implements Listener {

	private final PerkService perkService = PerkService.getInstance();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerHit(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Player) {
			Player player = (Player)evt.getEntity();
			
			if (perkService.playerHasPerk(player, AdventurerPerks.MeatShieldId)) {
				evt.setDamage(evt.getDamage() - 1);
			}
		}
	}
	
}
