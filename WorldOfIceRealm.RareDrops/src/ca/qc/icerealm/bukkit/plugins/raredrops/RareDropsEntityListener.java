package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;

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
	        		entity.getKiller().sendMessage( ChatColor.YELLOW + EntityUtilities.getEntityName(entity) + " dropped a " + ChatColor.DARK_PURPLE + MaterialUtil.getMaterialFriendName(items.get(i).getType().name()) + (items.get(i).getEnchantments().size() != 0 ? " (enchanted)":""));
	        	}	    
	        	
	        	event.getDrops().addAll(items);
        	}
        }
	}
}
