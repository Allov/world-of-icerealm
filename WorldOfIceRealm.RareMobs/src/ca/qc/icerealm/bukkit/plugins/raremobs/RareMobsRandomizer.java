package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.Collections;
import java.util.logging.Logger;

import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMob;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMobsData;

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

		// Randomize the list so every rare mob have equal chances to spawn
		Collections.shuffle(rareMobsData.getRareMobs());
		
		// Randomize odds
		for (RareMob mob : rareMobsData.getRareMobs())
		{
			if (random <= mob.getSpawnOdds()) 
			{
				return mob;
			}
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
