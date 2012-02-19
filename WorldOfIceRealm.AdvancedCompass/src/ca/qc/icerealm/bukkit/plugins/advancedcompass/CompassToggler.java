package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.CompassMode;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.CompassPlayersInfo;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.PlayerCompassData;
import ca.qc.icerealm.bukkit.plugins.data.DataPersistenceService;
import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class CompassToggler 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));

	private Player player = null;
	private CompassPlayersInfo compassPlayersInfo;
	private PlayerCompassData compassData;
	
	public CompassToggler(Player p)
	{
		setPlayer(p);
	}
	
	public void setPlayer(Player p)
	{
		player = p;
		compassPlayersInfo = CompassPlayersInfo.getInstance();
		compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
	}
	
	public void toggle()
	{
    	CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
    	
    	PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
    	
    	CompassMode mode = compassData.getCurrentCompassMode();
    	CompassMode nextMode = getNextCompassMode(mode);
    	
    	if (nextMode == CompassMode.Bed)
    	{
    		if (!setBedMode())
    		{
    			nextMode = getNextCompassMode(nextMode);
    		}
    	}
    	
    	if (nextMode == CompassMode.Player)
    	{		
    		if (!setPlayerMode())
    		{
    			nextMode = getNextCompassMode(nextMode);
    		}
    	} 
    	
    	if (nextMode == CompassMode.Fixed)
    	{
    		if (!setFixedMode())
    		{
    			nextMode = getNextCompassMode(nextMode);
    		}
    	} 
    	
    	if (nextMode == CompassMode.Custom)
    	{
    		if (!setCustomLocationMode())
    		{
    			nextMode = getNextCompassMode(nextMode);
    		}
    	} 
    	
    	if (nextMode == CompassMode.SpawnPoint)
    	{
    		if (!setSpawnPointMode())
    		{
    			nextMode = getNextCompassMode(nextMode);
    		}
    	}
    	
    	compassData.setCurrentCompassMode(nextMode);
    	//compassPlayersInfo.setPlayerCompassData(player.getName(), compassData);		
	}
	
	public boolean setBedMode()
	{
		if (player.getBedSpawnLocation() != null)
		{
			player.setCompassTarget(player.getBedSpawnLocation());
			player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Your compass is now pointing at your bed");
			return true;
		}
		
		return false;
	}
	
	public boolean setPlayerMode()
	{
		String playerName = compassData.getCurrentPlayerModePlayerName();
		if (playerName != null)
		{
			for (int i = 0; i < player.getServer().getOnlinePlayers().length; i++)
			{
    			// Validate if player still exists
				if (player.getServer().getOnlinePlayers()[i].getName().equalsIgnoreCase(playerName))
				{
					Player pointingPlayer = player.getServer().getPlayer(playerName);
					player.setCompassTarget(pointingPlayer.getLocation());
					player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Your compass is now pointing at " + playerName);

					// Add a time observer to get constant location change
					TimeServer.getInstance().addListener(new CompassPlayerObserver(player, pointingPlayer), AdvancedCompass.PLAYER_MODE_INTERVAL);
					
	        		return true;
				}
			}	
		}
		
		return false;
	}
	
	public boolean setFixedMode()
	{
		if (compassData.getCurrentFixedModeLocation() != null)
		{
			player.setCompassTarget(compassData.getCurrentFixedModeLocation());
			player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Your compass is now pointing at your current fixed location");
				
			return true;
		}
		
		return false;
	}
	
	public boolean setSpawnPointMode()
	{
		player.setCompassTarget(player.getWorld().getSpawnLocation());
		player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Your compass is now pointing at world spawn point");
		return true;
	}
	
	public boolean setCustomLocationMode()
	{
		if (compassData.getCurrentCustomModeLocation() != null)
		{
			player.setCompassTarget(compassData.getCurrentCustomModeLocation());
			
			if (compassData.getCurrentCustomToggleMessage() == null)
			{
				player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Current custom location set");
			}
			else
			{
				player.sendMessage(ChatColor.LIGHT_PURPLE + ">> " + compassData.getCurrentCustomToggleMessage());
			}
			
			return true;
		}
		
		return false;
	}
	
	public CompassMode getNextCompassMode(CompassMode mode)
	{
		if (mode == CompassMode.SpawnPoint) return CompassMode.Bed;
		if (mode == CompassMode.Bed) return CompassMode.Player;
		if (mode == CompassMode.Player) return CompassMode.Fixed;
		if (mode == CompassMode.Fixed) return CompassMode.Custom;
		if (mode == CompassMode.Custom) return CompassMode.SpawnPoint;
		
		return CompassMode.SpawnPoint;
	}
}
