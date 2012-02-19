package ca.qc.icerealm.bukkit.plugins.deathlocation;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.CustomCompassManager;

public class DeathLocationListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{     
        if(event.getEntity() instanceof Player)
        { 
        	Player player = (Player)event.getEntity();
        	CustomCompassManager manager = new CustomCompassManager("DeathLocation", player);
        	manager.setCustomLocation(player.getLocation(), "Your compass is now pointing at your last death location");
        }
	}
}