package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.LivingEntity;
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
	private HashMap<Integer, CustomMonster> _noBurnMonsters;
	private HashMap<Integer, CustomMonster> _damageMonsters;
	
	public CustomMonsterListener() {
		_noBurnMonsters = new HashMap<Integer, CustomMonster>();
		_damageMonsters = new HashMap<Integer, CustomMonster>();
	}

	public void updateFireproofMonster(LivingEntity e, boolean burn) {
		CustomMonster c = new CustomMonster();
		c.Burn = burn;
		_noBurnMonsters.put(e.getEntityId(), c);
	}
	
	public void updateDamageModifierMonster(LivingEntity e, double modifier) {
		CustomMonster c = new CustomMonster();
		c.DamageModifier = modifier;
		_damageMonsters.put(e.getEntityId(), c);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamage(EntityDamageEvent event) {		

		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)event;
			CustomMonster damager = _damageMonsters.get(damageEvent.getDamager().getEntityId());
			
			if (damager != null && damageEvent.getDamager() instanceof Monster && event.getEntity() instanceof Player) {
				int damageDone = (int)(event.getDamage() + (event.getDamage() * damager.DamageModifier));
				logger.info("damage done is: " + damageDone);
				event.setDamage(damageDone);
			}
		}
		
		CustomMonster noBurn = _noBurnMonsters.get(event.getEntity().getEntityId());
		if (noBurn != null && event.getEntity() instanceof Monster && event.getCause() == DamageCause.FIRE_TICK) {
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
		}
	}
}