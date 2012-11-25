package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CustomMonsterListener implements Listener {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private HashMap<Integer, CustomMonster> _customMonsters;
	
	public CustomMonsterListener() {
		_customMonsters = new HashMap<Integer, CustomMonster>();
	}
	
	public void addMonster(Integer entityId, Integer health) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = true;
		c.Invincible = false;
		_customMonsters.put(entityId, c);
	}
	
	public void addMonster(Integer entityId, Integer health, boolean burn) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = burn;
		c.Invincible = false;
		_customMonsters.put(entityId, c);
	}
	
	public void addMonster(Integer entityId, Integer health, boolean burn, boolean inv) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = burn;
		c.Invincible = inv;
		_customMonsters.put(entityId, c);
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
				logger.info("cancel fire damage");
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
	}
}

class CustomMonster {
	public int EntityId;
	public int Health;
	public boolean Burn;
	public boolean Invincible;
	
}
