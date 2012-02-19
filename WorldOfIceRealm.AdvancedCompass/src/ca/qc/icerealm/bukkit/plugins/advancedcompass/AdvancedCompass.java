package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.CompassPlayersInfo;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.PlayerCompassData;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.SerializablePlayerCompassData;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.SerializablePlayerCompassData.LocationData;
import ca.qc.icerealm.bukkit.plugins.data.DataPersistenceService;
import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;

public class AdvancedCompass extends JavaPlugin
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	public static final int PLAYER_MODE_INTERVAL = 1000;
	
	@Override
	public void onDisable() {
				
	}

	@Override
	public void onEnable() {
		
		// Version 1.0.0
		getServer().getPluginManager().registerEvents(new CompassActionListener(), this);
		
		getCommand("c").setExecutor(new CompassCommandExecutor());
		
		DataPersistenceService dataService = new DataSerializationService();
		
		if (dataService.exists("advancedcompass", "current_location"))
		{
			try
			{
				SerializablePlayerCompassData data = (SerializablePlayerCompassData)dataService.load("advancedcompass", "current_location");	
				CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
				
				// Loop into players and recover fixed location
				for (int i = 0; i < data.getPlayerLocationDataList().size(); i++)
				{
					LocationData playerData = data.getPlayerLocationDataList().get(i);
					Location loc = new Location(getServer().getWorld("World"), playerData.getLocationX(), playerData.getLocationY(), playerData.getLocationZ());
					PlayerCompassData playerCompassData = new PlayerCompassData();
					playerCompassData.setCurrentFixedModeLocation(loc);
					compassPlayersInfo.setPlayerCompassData(playerData.getPlayerName(), playerCompassData);
				}		
			}
			catch(Exception e)
			{
				logger.warning("Failed to recover data from AdvancedCompass");
			}
		}
	}
}