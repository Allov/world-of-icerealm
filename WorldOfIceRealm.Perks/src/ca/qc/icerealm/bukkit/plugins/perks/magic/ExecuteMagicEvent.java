package ca.qc.icerealm.bukkit.plugins.perks.magic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public class ExecuteMagicEvent implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(org.bukkit.event.player.PlayerInteractEvent event)
	{  
		// If left-clicked, execute magic
	    if(event.getItem() != null && event.getAction() != null && ((event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))) && event.getItem().getType() != null && event.getItem().getType().equals(Material.COMPASS)))
	    { 
	
	    }
	    // If Right-clicked, execute
	    else if(event.getItem() != null && event.getAction() != null && ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) && event.getItem().getType() != null && event.getItem().getType().equals(Material.COMPASS)))
	    {
	
	    }
	}
}