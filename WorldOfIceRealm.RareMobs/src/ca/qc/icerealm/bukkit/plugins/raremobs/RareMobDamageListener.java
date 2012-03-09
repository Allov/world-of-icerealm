package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;

public class RareMobDamageListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public RareMobDamageListener()
	{

	}

	/*
	private HashMap<Integer, CustomMonster> _customMonsters;
	
	public CustomMonsterListener() {
		_customMonsters = new HashMap<Integer, CustomMonster>();
	}
	
	public void addMonster(Integer entityId, Integer health) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = true;
		_customMonsters.put(entityId, c);
	}
	
	public void addMonster(Integer entityId, Integer health, boolean burn) {
		CustomMonster c = new CustomMonster();
		c.Health = health;
		c.Burn = burn;
		_customMonsters.put(entityId, c);
	}
	
	public void removeMonster(Integer entityId) {
		_customMonsters.remove(entityId);
	}*/
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamage(EntityDamageEvent event)
	{
		CurrentRareMob raremob = CurrentRareMob.getInstance();
		
		// Cancel fire from sun
		if(raremob.getRareMobLocation() != null && event.getEntity() instanceof Monster 
				&& (event.getEntity().getEntityId() == raremob.getRareMobEntityId()) 
				&& event.getCause() == DamageCause.FIRE_TICK)
		{
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
			return;
		}
		
		// Cancel fire from sun for subordinates
		if((raremob.getSubordinatesEntityId() != null
				&& raremob.getSubordinatesEntityId().contains(event.getEntity().getEntityId()))
				 && event.getCause() == DamageCause.FIRE_TICK)
		{
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
			return; 
		}	
		
	    if (event instanceof EntityDamageByEntityEvent)
	    {
	    	EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
	    	
			// If this is our rare mob, override damage
	        if(event.getEntity() instanceof Player && raremob.getRareMobLocation() != null && entityDamageByEntityEvent.getDamager().getEntityId() == raremob.getRareMobEntityId())
	        { 
	        	event.setDamage((int)Math.rint(event.getDamage() * raremob.getRareMob().getStrengthMultiplier()));
	        }
	        
	        // If this is a subordinate, override damage
	        if(event.getEntity() instanceof Player && raremob.getSubordinatesEntityId() != null && raremob.getSubordinatesEntityId().contains(entityDamageByEntityEvent.getDamager().getEntityId()))
	        { 
	        	event.setDamage((int)Math.rint(event.getDamage() * 2));
	        } 
	        
	        // Override damage for arrows as well (skeletons)
	        if(event.getEntity() instanceof Player && entityDamageByEntityEvent.getDamager() instanceof Arrow) 
	        {
	        	Arrow a = (Arrow) entityDamageByEntityEvent.getDamager();
	        	
	        	if (a.getShooter().getEntityId() == raremob.getRareMobEntityId())
	        	{
	        		event.setDamage((int)Math.rint(event.getDamage() * raremob.getRareMob().getStrengthMultiplier()));
	        	}
	        	
	        	// Subordinates
	        	if (raremob.getSubordinatesEntityId().contains(a.getShooter().getEntityId()))
	        	{
	        		event.setDamage((int)Math.rint(event.getDamage() * 2));
	        	} 
	        }
	        
	        // Add fighters to the list for rewards
	        if(entityDamageByEntityEvent.getDamager() instanceof Player && raremob.getRareMobLocation() != null && entityDamageByEntityEvent.getEntity().getEntityId() == raremob.getRareMobEntityId())
	        {
	        	Player player = (Player)entityDamageByEntityEvent.getDamager();
	        	
	        	if (!raremob.getFighters().contains(player))
	        	{
	        		raremob.getFighters().add(player);
	        	}
	        	
	        	// Also reset spawn timer
	        	raremob.setTimeSpawned(System.currentTimeMillis());
	        }
	    }
	    	
	    // Handle health for rare mob
		if(raremob.getRareMobLocation() != null && event.getEntity() instanceof Monster && event.getEntity().getEntityId() == raremob.getRareMobEntityId())
		{
			raremob.setCurrentHealth(raremob.getCurrentHealth() - event.getDamage());
			Monster m = (Monster)event.getEntity();
			
			if (raremob.getCurrentHealth() < 0) 
			{
				m.damage(m.getMaxHealth());
			}
			else {
				m.setHealth(m.getMaxHealth());
			}
		}
		
		// Handle health for his subordinates
		if(raremob.getSubordinatesEntityId() != null && event.getEntity() instanceof Monster && raremob.getSubordinatesEntityId().contains(event.getEntity().getEntityId()))
		{
			raremob.getCurrentSubordinateHealth().put(event.getEntity().getEntityId(), raremob.getCurrentSubordinateHealth().get(event.getEntity().getEntityId()) - event.getDamage());
			Monster m = (Monster)event.getEntity();
			logger.info(event.getDamage() + "");
			if (raremob.getCurrentSubordinateHealth().get(event.getEntity().getEntityId()) < 0) 
			{
				m.damage(m.getMaxHealth());
			}
			else {
				m.setHealth(m.getMaxHealth());
			}
		}
	}
}
