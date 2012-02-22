package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

public class RareMobsRandomizer 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private RareMobsData rareMobsData = null;
			
	public RareMobsRandomizer(RareMobsData data)
	{
		setRareMobsData(data); 
	}
	
	public RareMob randomizeSpawn()
	{
		double random = (Math.random() * 100);
		logger.info(random + "");
		
		logger.info(random <= 75.00 ? "true":"false");
		
		if (random <= 75.00) 
		{
			logger.info(rareMobsData == null ? "true":"false");
			logger.info(rareMobsData.getRareMobs().size() + "");
			RareMob mob = rareMobsData.getRareMobs().get(0);
			return mob;
		}
		
		// No rare mob this time
		return null;
	}

	public RareMobsData getRareMobsData() 
	{
		return rareMobsData;
	}

	public void setRareMobsData(RareMobsData rareMobsData) 
	{
		this.rareMobsData = rareMobsData;
	}
}
