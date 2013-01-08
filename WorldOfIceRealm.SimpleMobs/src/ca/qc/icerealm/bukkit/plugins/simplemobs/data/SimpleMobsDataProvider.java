package ca.qc.icerealm.bukkit.plugins.simplemobs.data;

import ca.qc.icerealm.bukkit.plugins.data.DataPersistenceService;
import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;
import ca.qc.icerealm.bukkit.plugins.simplemobs.data.SimpleMobsDataProvider;

public class SimpleMobsDataProvider 
{
	private static SimpleMobsDataProvider instance;
	
	public static SimpleMobsDataProvider getInstance() 
	{
		if (instance == null) 
		{
			instance = new SimpleMobsDataProvider();
		}
		
		return instance;
	}
	
	public SimpleMob[] LoadLastSimpleMobsData()
	{
		DataPersistenceService dataService = new DataSerializationService();
		
		if (dataService.exists("simplemobs", "data"))
		{
			return (SimpleMob[])dataService.load("simplemobs", "data");	
		}
		
		return null;
	}
	
	public void SaveLastSimpleMobsData(SimpleMob[] data)
	{
		DataPersistenceService dataService = new DataSerializationService();
		dataService.save("simplemobs", "data", data);
	}
}
