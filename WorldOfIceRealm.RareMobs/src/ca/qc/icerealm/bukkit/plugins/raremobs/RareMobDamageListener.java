package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;

public class RareMobDamageListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public RareMobDamageListener()
	{

	}

/*	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{     
		CurrentRareMob raremob = CurrentRareMob.getInstance();
		logger.info("INNNNNNNN");
		// If this is our rare mob, override damage
        if(event.getEntity() instanceof Monster && raremob.getRareMobLocation() != null && event.getEntity().getEntityId() == raremob.getRareMobEntityId())
        { 
        	logger.info("new damage");
        	event.setDamage((int)Math.rint(event.getDamage() * raremob.getRareMob().getStrengthMultiplier()));
        }
        
        // Add fighters
        if(event.getEntity() instanceof Player && raremob.getRareMobLocation() != null && event.getDamager().getEntityId() == raremob.getRareMobEntityId())
        {
        	Player player = (Player)event.getEntity();
        	
        	if (!raremob.getFighters().contains(player))
        	{
        		logger.info("add player");
        		raremob.getFighters().add(player);
        	}
        }
	}*/
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
		
	    if (event instanceof EntityDamageByEntityEvent)
	    {
	    	EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) event;
	    	
			// If this is our rare mob, override damage
	        if(event.getEntity() instanceof Player && raremob.getRareMobLocation() != null && entityDamageByEntityEvent.getDamager().getEntityId() == raremob.getRareMobEntityId())
	        { 
	        	event.setDamage((int)Math.rint(event.getDamage() * raremob.getRareMob().getStrengthMultiplier()));
	        }
	        
	        // Add fighters
	        if(entityDamageByEntityEvent.getDamager() instanceof Player && raremob.getRareMobLocation() != null && entityDamageByEntityEvent.getEntity().getEntityId() == raremob.getRareMobEntityId())
	        {
	        	Player player = (Player)entityDamageByEntityEvent.getDamager();
	        	
	        	if (!raremob.getFighters().contains(player))
	        	{
	        		raremob.getFighters().add(player);
	        	}
	        }
	    }
		
		//CustomMonster custom = _customMonsters.get(event.getEntity().getEntityId());		
		
		if(raremob.getRareMobLocation() != null && event.getEntity() instanceof Monster && event.getEntity().getEntityId() == raremob.getRareMobEntityId())
		{
		//if (custom != null && event.getEntity() instanceof Monster) {	
			raremob.setCurrentHealth(raremob.getCurrentHealth() - event.getDamage());
			Monster m = (Monster)event.getEntity();
			
			if (raremob.getCurrentHealth() < 0) 
			{
				m.damage(m.getMaxHealth());
				//_customMonsters.remove(event.getEntity().getEntityId());
			}
			else {
				m.setHealth(m.getMaxHealth());
			}
		}
		
		// Cancel fire from sun
		if(raremob.getRareMobLocation() != null && event.getEntity() instanceof Monster 
				&& (event.getEntity().getEntityId() == raremob.getRareMobEntityId()) 
				&& event.getCause() == DamageCause.FIRE_TICK)
		{
		//if (!_config.BurnDuringDaylight && _zone.isInside(e.getEntity().getLocation()) && e.getCause() == DamageCause.FIRE_TICK) {
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
		}
		
		if((raremob.getSubordinatesEntityId() != null
				&& raremob.getSubordinatesEntityId().contains(event.getEntity().getEntityId()))
				 && event.getCause() == DamageCause.FIRE_TICK)
		{
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
		}	
	}
	
	/*@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterBurning(EntityDamageEvent event) 
	{
		CurrentRareMob raremob = CurrentRareMob.getInstance();
		
		if(raremob.getRareMobLocation() != null && event.getEntity() instanceof Monster 
				&& (event.getEntity().getEntityId() == raremob.getRareMobEntityId()) 
				&& event.getCause() == DamageCause.FIRE_TICK)
		{
		//if (!_config.BurnDuringDaylight && _zone.isInside(e.getEntity().getLocation()) && e.getCause() == DamageCause.FIRE_TICK) {
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
		}
		
		if((raremob.getSubordinatesEntityId() != null
				&& raremob.getSubordinatesEntityId().contains(event.getEntity().getEntityId()))
				 && event.getCause() == DamageCause.FIRE_TICK)
		{
			event.setCancelled(true);
			event.getEntity().setFireTicks(0);
		}		
	}*/
}
