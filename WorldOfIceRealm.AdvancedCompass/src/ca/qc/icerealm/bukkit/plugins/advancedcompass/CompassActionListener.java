package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;

public class CompassActionListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(org.bukkit.event.player.PlayerInteractEvent event)
	{     
		// If left-clicked anywhere with compass
        if(event.getItem() != null && event.getAction() != null && ((event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))) && event.getItem().getType() != null && event.getItem().getType().equals(Material.COMPASS)))
        { 
        	CompassToggler toggler = new CompassToggler(event.getPlayer());
        	toggler.toggle();
        }
        else if(event.getItem() != null && event.getAction() != null && ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) && event.getItem().getType() != null && event.getItem().getType().equals(Material.COMPASS)))
        {
        	if (event.getPlayer().getCompassTarget() != null)
        	{
        		double distance = LocationUtil.getDistanceBetween(event.getPlayer().getLocation(), event.getPlayer().getCompassTarget());
        		event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + ">> Distance: " + ((int)distance) + "m.");
        	}
        }
	}
}
