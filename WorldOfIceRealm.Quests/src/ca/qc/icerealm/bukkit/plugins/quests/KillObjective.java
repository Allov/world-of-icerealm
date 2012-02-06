package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.Zone;

public class KillObjective extends Objective implements Listener {
	
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public KillObjective(Quest quest, Zone zone, int amount, int entityId) {
		super(quest, zone, amount, entityId);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity killedEntity = event.getEntity();
		logger.info("Killed something");
		if (killedEntity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)killedEntity;
			Player killer = livingEntity.getKiller();
			
			logger.info("It's a LivingEntity");
			if (killer != null) {
				logger.info("It's got a killer and has to be " + EntityUtilities.getEntityId(livingEntity) + " == " + getEntityId());				
				if (EntityUtilities.getEntityId(livingEntity) == getEntityId()) {
					logger.info("It's what we're looking for!");
					advance(livingEntity);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return 	ChatColor.DARK_PURPLE + "Killed " + 
				ChatColor.GREEN + this.getCurrent() + "/" + this.getAmount();
	}
}
