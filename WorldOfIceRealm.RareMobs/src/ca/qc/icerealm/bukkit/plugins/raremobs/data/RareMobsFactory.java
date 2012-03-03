package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.CreatureType;

import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.raredrops.RareDropsFactory;
import ca.qc.icerealm.bukkit.plugins.raredrops.RareDropsOdds;

public class RareMobsFactory 
{
	public final Logger logger = Logger.getLogger(("RareMobsFactory"));
	private double oddsMultiplier = 1;
	List<MapWrapper> mobs = null;
		
	public double getOddsMultiplier() 
	{
		return oddsMultiplier;
	}

	public void setOddsMultiplier(int oddsMultiplier) 
	{
		this.oddsMultiplier = oddsMultiplier;
	}
	
	public RareMobsFactory(List<MapWrapper> mobs)
	{
		this.mobs = mobs;
	}
	
	public RareMobsFactory(List<MapWrapper> mobs, double _oddsMultiplier)
	{
		this.mobs = mobs;
		oddsMultiplier = _oddsMultiplier;
	}
	
	public RareMobsData build()
	{
		RareMobsData data = new RareMobsData();	

		for (MapWrapper mob : mobs) 
		{
			RareMob raremob = new RareMob();
			
			logger.info("Type: " +  mob.getString("type", null));
			
			// Make sure the mob exists
			if (CreatureType.valueOf(mob.getString("type", null)) == null)
			{
				logger.warning("Invalid mob type, ignoring odds for mob: " + mob.getString("name", null));
			}
			else
			{			
				raremob.setCreatureType(CreatureType.valueOf(mob.getString("type", null)));		
				raremob.setMobName(mob.getString("name", null));
				raremob.setSpawnOdds(mob.getDouble("odds", 0.00) * oddsMultiplier);
				raremob.setExperience(mob.getInt("experience", 0));
				raremob.setHealth(mob.getInt("health", 1));
				raremob.setStrengthMultiplier(mob.getInt("strength multiplier", 1));
				
				// Load subordinates (mobs' friends)
				ArrayList<Subordinate> subList = new ArrayList<Subordinate>();
				
				List<MapWrapper> subordinates = mob.getMapList("subordinates", new ArrayList<MapWrapper>());
				
				for (MapWrapper subordinate : subordinates) 
				{
					Subordinate sub = new Subordinate();
					
					// Make sure the mob exists
					if (CreatureType.valueOf(subordinate.getString("type", null)) == null)
					{
						logger.warning("Invalid mob name, ignoring odds for mob: " + subordinate.getString("name", null));
					}
					else
					{
						sub.setCreatureType(CreatureType.valueOf(subordinate.getString("type", null)));	
						subList.add(sub);
						raremob.setSubordinates(subList);
					}
				}
				
				// Load raredrops for this raremob
				List<MapWrapper> raredrops = mob.getMapList("drops", new ArrayList<MapWrapper>());
				
				if (raredrops.size() != 0)
				{
					RareDropsFactory rardropsFactory = new RareDropsFactory(raredrops, 50);
					
					RareDropsOdds dropsOdd = rardropsFactory.createOdds();
					raremob.setRaredropsOdds(dropsOdd);
					logger.info("adding raredrops");
				}
				
				data.addRareMob(raremob);
			}
		}
		
		return data;
	}
}
