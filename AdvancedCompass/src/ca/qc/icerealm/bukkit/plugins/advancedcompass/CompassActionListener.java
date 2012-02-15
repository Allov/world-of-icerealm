package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
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
        	boolean notSet = false;
        	
        	CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
        	
        	CompassMode mode = compassPlayersInfo.getCompassMode(event.getPlayer().getName());
        	CompassMode nextMode = getNextCompassMode(mode);
        	
        	if (nextMode == CompassMode.Bed)
        	{
        		if (event.getPlayer().getBedSpawnLocation() != null)
        		{
	        		event.getPlayer().setCompassTarget(event.getPlayer().getBedSpawnLocation());
	        		event.getPlayer().sendMessage("Your compass is now pointing at your bed");
        		}
        		else
        		{
            		notSet = true;
        		}
        	}
        	else if (nextMode == CompassMode.Player)
        	{
        		String playerName = compassPlayersInfo.getCurrentPlayerModePlayerName();
        		if (compassPlayersInfo.getCurrentPlayerModePlayerName() != null)
        		{
	        		event.getPlayer().setCompassTarget(event.getPlayer().getServer().getPlayer(compassPlayersInfo.getCurrentPlayerModePlayerName()).getLocation());
	        		event.getPlayer().sendMessage("Your compass is now pointing at " + playerName);
        		}
        		else
        		{
            		notSet = true;
        		}
        	}   	
        	
        	if (nextMode == CompassMode.SpawnPoint || notSet)
        	{
        		event.getPlayer().setCompassTarget(event.getPlayer().getWorld().getSpawnLocation());
        		event.getPlayer().sendMessage("Your compass is now pointing at world spawn point");
        	}
        	
        	compassPlayersInfo.setCompassMode(event.getPlayer().getName(), nextMode);
        }
	}
	
	public CompassMode getNextCompassMode(CompassMode mode)
	{
		if (mode == CompassMode.SpawnPoint) return CompassMode.Bed;
		if (mode == CompassMode.Bed) return CompassMode.Player;
		if (mode == CompassMode.Player) return CompassMode.SpawnPoint;
		
		return CompassMode.SpawnPoint;
	}
}
