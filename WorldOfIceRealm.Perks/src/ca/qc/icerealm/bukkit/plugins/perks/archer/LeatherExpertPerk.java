package ca.qc.icerealm.bukkit.plugins.perks.archer;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;

public class LeatherExpertPerk implements Listener {

	private PerkService perkService = PerkService.getInstance();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShoot(EntityDamageByEntityEvent evt) {
		Player damager = null;
		
		if (evt.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow)evt.getDamager();
			if (arrow.getShooter() instanceof Player) {
				damager = (Player)arrow.getShooter();
			}
		}
		
		if (damager != null) {// && perkService.playerHasPerk((Player)damager, ArcherTree.LeatherExpertId)) {
			PlayerInventory inventory = damager.getInventory();
			ItemStack[] armorContents = inventory.getArmorContents();
			
			double damageBoost = 1;
			
			for(ItemStack armor : armorContents) {
				if (armor.getType().equals(Material.LEATHER_BOOTS)) {
					damageBoost += 0.10;
				} else if (armor.getType().equals(Material.LEATHER_CHESTPLATE)) {
					damageBoost += 0.10;
				} else if (armor.getType().equals(Material.LEATHER_HELMET)) {
					damageBoost += 0.10;
				} else if (armor.getType().equals(Material.LEATHER_LEGGINGS)) {
					damageBoost += 0.10;
				}
			}
			
			if (damageBoost == 0.4) {
				damageBoost = 0.5;
			}
			
			int baseDamage = evt.getDamage();
			int finalDamage = (int) (baseDamage * damageBoost); 
			evt.setDamage(finalDamage);
		}
	}
	
}
