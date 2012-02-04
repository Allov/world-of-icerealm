package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class RareDropsEntityListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{     
        if(event.getEntity() instanceof Monster)
        { 
        	Monster entity = (Monster)event.getEntity();
        	
        	if (entity.getKiller() instanceof Player)
        	{   	
	        	RareDropsFactory factory = new RareDropsFactory(1);
	        	RareDropsOdds odds = factory.createEntityOdds(entity);
	        	
	        	RareDropsRandomizer randomizer = new RareDropsRandomizer(odds);
	        	
	        	ArrayList<ItemStack> items =  randomizer.randomize();
	
	        	for (int i = 0; i < items.size(); i++)
	        	{
	        		entity.getKiller().sendMessage("Monster dropped a " + items.get(i).getType().name());
	        	}	    
	        	
	        	event.getDrops().addAll(items);
        	}
        }
	}
}
