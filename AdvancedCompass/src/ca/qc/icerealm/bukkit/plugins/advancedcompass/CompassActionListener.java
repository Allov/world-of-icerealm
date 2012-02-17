package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public class CompassActionListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(org.bukkit.event.player.PlayerInteractEvent event)
	{     
		// If left-clicked anywhere with compass
        if(((event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))) && event.getItem().getType().equals(Material.COMPASS)))
        { 
        	CompassToggler toggler = new CompassToggler(event.getPlayer());
        	toggler.toggle();
        }
	}
}
