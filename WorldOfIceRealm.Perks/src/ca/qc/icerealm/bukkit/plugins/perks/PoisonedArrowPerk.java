package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonedArrowPerk implements Listener {

	private PerkService perkService = PerkService.getInstance();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShoot(EntityDamageByEntityEvent evt) {

		if (evt.getDamager() instanceof CraftArrow) {
			CraftArrow arrow = (CraftArrow)evt.getDamager();
			
			if (arrow.getShooter() instanceof Player && perkService.playerHasPerk((Player)arrow.getShooter(), AdventurerPerks.PoisonedArrowId)) {
				if (evt.getEntity() instanceof LivingEntity) {
					LivingEntity le = (LivingEntity)evt.getEntity();					
					le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 1));
				}
			}
		}
		
	}
	
}
