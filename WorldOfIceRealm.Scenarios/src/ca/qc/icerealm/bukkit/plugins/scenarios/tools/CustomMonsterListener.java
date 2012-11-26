package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CustomMonsterListener implements Listener {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private HashMap<Integer, CustomMonster> _customMonsters;
	
	public CustomMonsterListener() {
		_customMonsters = new HashMap<Integer, CustomMonster>();
	}
	
	public boolean monsterAlreadyAdded(Integer entityId) {
		return _customMonsters.containsKey(entityId);
	}
	
	public void addMonster(Integer entityId, Integer health) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = true;
		c.Invincible = false;
		c.EntityId = entityId;
		c.DamageModifier = 0.0;
		_customMonsters.put(entityId, c);
		logInfo(c);
	}
	
	public void addMonster(Integer entityId, Integer health, boolean burn) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = burn;
		c.Invincible = false;
		c.EntityId = entityId;
		c.DamageModifier = 0.0;
		_customMonsters.put(entityId, c);
		logInfo(c);
	}
	
	public void addMonster(Integer entityId, Integer health, boolean burn, boolean inv) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = burn;
		c.Invincible = inv;
		c.EntityId = entityId;
		c.DamageModifier = 0.0;
		_customMonsters.put(entityId, c);
		logInfo(c);
	}
	
	public void addMonster(Integer entityId, Integer health, boolean burn, boolean inv, double damage) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = burn;
		c.Invincible = inv;
		c.EntityId = entityId;
		c.DamageModifier = damage;
		_customMonsters.put(entityId, c);
		logInfo(c);
	}
	
	public void removeMonster(Integer entityId) {
		_customMonsters.remove(entityId);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamage(EntityDamageEvent event) {		
		CustomMonster custom = _customMonsters.get(event.getEntity().getEntityId());
		
		if (custom != null && event.getEntity() instanceof Monster) {
			
			if (event.getCause() == DamageCause.FIRE_TICK && !custom.Burn) {
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
			}
			else {
				custom.Health -= event.getDamage();
				Monster m = (Monster)event.getEntity();
				
				if (custom.Health < 0) {
					m.damage(m.getMaxHealth());
					_customMonsters.remove(event.getEntity().getEntityId());
					
				}
				else {
					m.setHealth(m.getMaxHealth());
				}	
			}
		}

		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)event;
			CustomMonster damager = _customMonsters.get(damageEvent.getDamager().getEntityId());
			//logger.info("damager is " + (damager != null) + " and hitting a " + event.getEntityType());
			
			if (damager != null && damageEvent.getDamager() instanceof Monster && event.getEntity() instanceof Player) {
				int damageDone = event.getDamage() + (int)(event.getDamage() * damager.DamageModifier);
				//logger.info("damage done: " + damageDone + " base damage: " + event.getDamage() + " additional damage: " + additionalDamage);
				event.setDamage(damageDone);
			}
		}
	}

	private void logInfo(CustomMonster c) {
		//logger.info("Custom Monster: " + c.Health + " health - " + c.EntityId + " id" + " damage: " + c.DamageModifier);
	}
}

class CustomMonster {
	public int EntityId;
	public int Health;
	public boolean Burn;
	public boolean Invincible;
	public double DamageModifier;
	
}
