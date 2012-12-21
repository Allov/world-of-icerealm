package ca.qc.icerealm.bukkit.plugins.perks.magic.fire;

import java.util.logging.Logger;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireDamageModifier implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamage(EntityDamageEvent event)
	{		
		if (event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
		
			//logger.info(entityDamageByEntityEvent.getDamager());
			if(entityDamageByEntityEvent.getDamager() instanceof Fireball) 
			{
				Fireball fb = (Fireball) entityDamageByEntityEvent.getDamager();
				
				if (fb.getShooter() instanceof Player)
				{
					if (fb instanceof SmallFireball)
					{
						entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * 3);
					}
					else // Big fireball
					{
						entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * 5);
					}
				}
			}
		}
	}
}
