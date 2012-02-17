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
        	CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
        	
        	PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(event.getPlayer().getName());
        	
        	CompassMode mode = compassData.getCurrentCompassMode();
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
            		nextMode = getNextCompassMode(nextMode);
        		}
        	}
        	
        	if (nextMode == CompassMode.Player)
        	{
        		String playerName = compassData.getCurrentPlayerModePlayerName();
        		if (playerName != null)
        		{
					for (int i = 0; i < event.getPlayer().getServer().getOnlinePlayers().length; i++)
					{
	        			// Validate if player still exists
						if (event.getPlayer().getServer().getOnlinePlayers()[i].getName().equals(playerName))
						{
		        			event.getPlayer().setCompassTarget(event.getPlayer().getServer().getPlayer(playerName).getLocation());
			        		event.getPlayer().sendMessage("Your compass is now pointing at " + playerName);
						}
						else
						{
							nextMode = getNextCompassMode(nextMode);
						}
					}
        		}
        		else
        		{
            		nextMode = getNextCompassMode(nextMode);
        		}
        	} 
        	
        	if (nextMode == CompassMode.Fixed)
        	{
        		if (compassData.getCurrentFixedModeLocation() != null)
        		{
	        		event.getPlayer().setCompassTarget(compassData.getCurrentFixedModeLocation());
	        		event.getPlayer().sendMessage("Your compass is now pointing at your current fixed location");
        		}
        		else
        		{
            		nextMode = getNextCompassMode(nextMode);
        		}
        	} 
        	
        	if (nextMode == CompassMode.SpawnPoint)
        	{
        		event.getPlayer().setCompassTarget(event.getPlayer().getWorld().getSpawnLocation());
        		event.getPlayer().sendMessage("Your compass is now pointing at world spawn point");
        	}
        	
        	compassData.setCurrentCompassMode(nextMode);
        	compassPlayersInfo.setPlayerCompassData(event.getPlayer().getName(), compassData);
        }
	}
	
	public CompassMode getNextCompassMode(CompassMode mode)
	{
		if (mode == CompassMode.SpawnPoint) return CompassMode.Bed;
		if (mode == CompassMode.Bed) return CompassMode.Player;
		if (mode == CompassMode.Player) return CompassMode.Fixed;
		if (mode == CompassMode.Fixed) return CompassMode.SpawnPoint;
		
		return CompassMode.SpawnPoint;
	}
}
