package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.HashMap;
import java.util.Map;

public class CompassPlayersInfo 
{
   private static final CompassPlayersInfo instance = new CompassPlayersInfo();
   private Map<String, PlayerCompassData> playersCurrentComppassDataList = new HashMap<String, PlayerCompassData>();

   private CompassPlayersInfo() 
   {

   }
   
   public static synchronized CompassPlayersInfo getInstance() 
   {
      return instance;
   }

   public void setPlayerCompassData(String playerName, PlayerCompassData data)
   {
	   playersCurrentComppassDataList.put(playerName, data);
   }
   
   public PlayerCompassData getPlayerCompassData(String playerName)
   {
	   PlayerCompassData data = playersCurrentComppassDataList.get(playerName);
	   
	   if (data == null)
	   {
		   data = new PlayerCompassData();
	   }
	   return data;
   }
}
