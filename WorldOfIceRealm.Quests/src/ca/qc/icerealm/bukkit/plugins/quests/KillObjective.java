package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class KillObjective extends Objective implements Listener {
	
	public KillObjective(Player player, WorldZone zone, int amount, int entityId) {
		super(player, zone, amount, entityId);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity killedEntity = event.getEntity();
		if (killedEntity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)killedEntity;
			Player killer = livingEntity.getKiller();
			
			if (killer != null && getPlayer() == killer) {
				if (EntityUtilities.getEntityId(livingEntity) == getEntityId()) {
					advance(livingEntity);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return 	ChatColor.DARK_GREEN + "Killed " + 
				ChatColor.GREEN + this.getCurrent() + "/" + this.getAmount();
	}
}
