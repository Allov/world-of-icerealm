package ca.qc.icerealm.bukkit.plugins.perks.archer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.v1_4_6.PathEntity;

import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftWolf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import ca.qc.icerealm.bukkit.plugins.perks.Cooldown;
import ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol.AgressivityMobControl;

public class MansBestFriendPerk implements Listener, Runnable {
	
	private final Server server;
	private HashMap<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();
	private static final long CooldownTime = 10000;

	public MansBestFriendPerk(Server server) {
		this.server = server;		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onTamedWolfDamage(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Wolf) {
			Wolf wolf = (Wolf)evt.getEntity();
			
			if (wolf.isTamed()) {
				evt.setDamage(evt.getDamage() / 2);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onTamedWolfAttack(EntityDamageByEntityEvent evt) {
		if (evt.getDamager() instanceof Wolf) {
			Wolf wolf = (Wolf)evt.getDamager();
			
			if (wolf.isTamed()) {
				evt.setDamage(evt.getDamage() * 2);
			}
		}
	}

	@Override
	public void run() {
		Collection<Wolf> wolves = this.server.getWorld("world").getEntitiesByClass(Wolf.class);
		
		for(Wolf wolf : wolves) {
			if (wolf.isTamed()) {
				if (wolf.getOwner() instanceof Player) {
					Player player = (Player)wolf.getOwner();

					boolean canTaunt = false;
					
					Logger.getLogger("Minecraft").info("Wat!");

					if (cooldowns.containsKey(player.getName())) {
						Logger.getLogger("Minecraft").info("Wat2!");
						if (!cooldowns.get(player.getName()).isOnCooldown()) {
							Logger.getLogger("Minecraft").info("Wat4!");
							Cooldown cd = new Cooldown(CooldownTime);
							cooldowns.put(player.getName(), cd);
							cd.start();
							
							canTaunt = true;
						}
					} else {
						Logger.getLogger("Minecraft").info("Wat3!");
						Cooldown cd = new Cooldown(CooldownTime);
						cooldowns.put(player.getName(), cd);
						cd.start();
						
						canTaunt = true;
					}
					
					if (canTaunt) {
						player.sendMessage("Wolf is taunting");
						List<Entity> nearbyEntities = wolf.getNearbyEntities(10, 10, 10);
						player.sendMessage("Nearby: " + nearbyEntities.size());
						
						for(Entity entity : nearbyEntities) {
							Logger.getLogger("Minecraft").info("Entity: " + entity.toString());
							if (entity instanceof Monster) {
								
								AgressivityMobControl.defineTarget((LivingEntity)entity, wolf);
								
								player.sendMessage("Monster taunted: " + entity.toString());
								
							}
						}
					}
				}
			}
		}
	}
}
