package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.List;
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

public class KillObjective extends CountObjective implements Listener {
	
	private List<Integer> entityIds;	

	public KillObjective(Player player, String name, WorldZone zone, int amount, int entityId) {
		super(player, zone, name, amount);
		this.entityIds = new ArrayList<Integer>();
		this.entityIds.add(entityId);
	}

	public KillObjective(Player player, String name, WorldZone zone, int amount, List<Integer> entityId) {
		super(player, zone, name, amount);
		this.entityIds = entityId;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity killedEntity = event.getEntity();
		
		if (killedEntity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)killedEntity;
			Player killer = livingEntity.getKiller();
			
			if (killer != null && getPlayer() == killer) {
				if (entityIds.contains(EntityUtilities.getEntityId(livingEntity)) && 
					(getZone() == null || getZone().isInside(livingEntity.getLocation()))) {
					advance();
				}
			}
		}
	}
}
