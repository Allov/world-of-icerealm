package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class CompassPlayersInfo 
{
   private static CompassPlayersInfo instance = null;
   private Map<String, CompassMode> playersCurrentComppassModeList = new HashMap<String, CompassMode>();
   private String currentPlayerModePlayerName = null;
   private Location currentFixedModeLocation = null;

   protected CompassPlayersInfo() 
   {

   }
   
   public static CompassPlayersInfo getInstance() 
   {
      if (instance == null) 
      {
         instance = new CompassPlayersInfo();
      }
      
      return instance;
   }
   
   public void setCompassMode(String playerName, CompassMode mode)
   {
	   playersCurrentComppassModeList.put(playerName, mode);
   }
   
   public CompassMode getCompassMode(String playerName)
   {
	   CompassMode mode = playersCurrentComppassModeList.get(playerName);
	   
	   if (mode != null)
	   {
		   return playersCurrentComppassModeList.get(playerName);
	   }
	   
	   return CompassMode.SpawnPoint;
   }
   
   public void setCurrentPlayerModePlayerName(String playerName)
   {
	   currentPlayerModePlayerName = playerName;
   }
   
   public String getCurrentPlayerModePlayerName()
   {
	   return currentPlayerModePlayerName;
   }

	public Location getCurrentFixedModeLocation() 
	{
		return currentFixedModeLocation;
	}

	public void setCurrentFixedModeLocation(Location currentFixedModeLocation) 
	{
		this.currentFixedModeLocation = currentFixedModeLocation;
	}
}
