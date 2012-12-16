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

public class FindWeaknessPerk implements Listener {
	
	private PerkService perkService = PerkService.getInstance();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShoot(EntityDamageByEntityEvent evt) {

		Player damager = null;
		
		if (evt.getDamager() instanceof CraftArrow) {
			CraftArrow arrow = (CraftArrow)evt.getDamager();
						
			if (arrow.getShooter() instanceof Player) {
				damager = (Player)arrow.getShooter();
			}
		} else if (evt.getDamager() instanceof Player) {
			damager = (Player)evt.getDamager();
		}
		
		if (damager != null && perkService.playerHasPerk(damager, AdventurerPerks.FindWeaknessId)) {
			if (evt.getEntity() instanceof LivingEntity) {
				LivingEntity le = (LivingEntity)evt.getEntity();					
				le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5*20, 1));
			}
		}
	}
}
