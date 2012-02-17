package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class CompassPlayersInfo 
{
   private static CompassPlayersInfo instance = null;
   private Map<String, PlayerCompassData> playersCurrentComppassDataList = new HashMap<String, PlayerCompassData>();

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

   public void setPlayerCompassData(String playerName, PlayerCompassData data)
   {
	   playersCurrentComppassDataList.put(playerName, data);
   }
   
   public PlayerCompassData getPlayerCompassData(String playerName)
   {
	   PlayerCompassData data = playersCurrentComppassDataList.get(playerName);
	   if (data != null)
	   {
		   return playersCurrentComppassDataList.get(playerName);
	   }
	   
	   data = new PlayerCompassData();
	   return data;
   }
}
