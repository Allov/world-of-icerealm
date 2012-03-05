package ca.qc.icerealm.bukkit.plugins.advancedcompass.data;

import java.util.Hashtable;
import java.util.logging.Logger;

import org.bukkit.Location;

public class CompassPlayersInfo 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
   private static final CompassPlayersInfo instance = new CompassPlayersInfo();
   private Hashtable<String, PlayerCompassData> playersCurrentComppassDataList = new Hashtable<String, PlayerCompassData>();

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
		   playersCurrentComppassDataList.put(playerName, data);
	   }
	   return data;
   }
   
	public SerializablePlayerCompassData toSerializablePlayerCompassData()
	{
		SerializablePlayerCompassData data = new SerializablePlayerCompassData();
		
		Object[] playersName = playersCurrentComppassDataList.keySet().toArray();
		
		for (int i = 0; i < playersName.length; i++)
		{
			Location loc = playersCurrentComppassDataList.get(playersName[i]).getCurrentFixedModeLocation();
			
			if (loc != null)
			{
				logger.info("playerName: " + playersName[i].toString());
				data.addPlayerLocationData(playersName[i].toString(), loc.getX(), loc.getY(), loc.getZ());
			}
		}
		
		return data;
	}
}
