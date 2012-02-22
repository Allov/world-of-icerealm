package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class StrongerMonster implements Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private int _max;
	private int _entityId;
	private int _damageSoFar;
	private boolean _burn;
	
	public StrongerMonster(LivingEntity m, int max, boolean burn) {
		_max = max;
		_damageSoFar = 0;
		_entityId = m.getEntityId();
		_burn = burn;
	}
	
	
	public void onMonsterDamage(EntityDamageEvent e) {
		if (_entityId == e.getEntity().getEntityId()) {

			_damageSoFar += e.getDamage();
			
			if (_damageSoFar > _max) {
				((Monster)e.getEntity()).damage( ((Monster)e.getEntity()).getMaxHealth() );
				
			}
			else {
				((Monster)e.getEntity()).setHealth( ((Monster)e.getEntity()).getMaxHealth() );
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterFire(EntityDamageEvent e) {
		if (_entityId == e.getEntity().getEntityId() && _burn) {
			onMonsterDamage(e);
		}
		else if (_entityId == e.getEntity().getEntityId() && !_burn && e.getCause() == DamageCause.FIRE_TICK) {
			e.setCancelled(true);
			e.getEntity().setFireTicks(0);
		}
		else if (_entityId == e.getEntity().getEntityId() && !_burn && e.getCause() != DamageCause.FIRE_TICK) {
			onMonsterDamage(e);
		}
	}
	
	
	
	
	
}
