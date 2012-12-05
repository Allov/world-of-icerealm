package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsFactory;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultipliers;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;

import org.bukkit.entity.EntityType;;

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
			
			// Make sure the mob exists
			if (EntityType.valueOf(mob.getString("type", null)) == null)
			{
				logger.warning("Invalid mob type, ignoring odds for mob: " + mob.getString("name", null));
			}
			else
			{			
				raremob.setCreatureType(EntityType.valueOf(mob.getString("type", null)));		
				raremob.setMobName(mob.getString("name", null));
				raremob.setSpawnOdds(mob.getDouble("odds", 0.00) * oddsMultiplier);
				
				Map<?, ?> temp = mob.getMap();
				MapWrapper rawards = new MapWrapper((Map<?, ?>)temp.get("rewards"));
								
				raremob.setExperienceLevels(rawards.getInt("experienceLevels", 0));
				raremob.setMoney(rawards.getInt("money", 0));
				raremob.setHealth(mob.getInt("health", 20));
				raremob.setStrengthMultiplier(mob.getInt("strength multiplier", 1));
				raremob.setSubordinatesHealthMultiplier(mob.getDouble("subordinates health multiplier", 1));
				raremob.setSubordinatesDamageMultiplier(mob.getDouble("subordinates strength multiplier", 1));
				
				// Load subordinates (mobs' friends)
				ArrayList<Subordinate> subList = new ArrayList<Subordinate>();
				
				List<MapWrapper> subordinates = mob.getMapList("subordinates", new ArrayList<MapWrapper>());
				
				for (MapWrapper subordinate : subordinates) 
				{
					Subordinate sub = new Subordinate();
					
					// Make sure the mob exists
					if (EntityType.valueOf(subordinate.getString("type", null)) == null)
					{
						logger.warning("Invalid mob name, ignoring odds for mob: " + subordinate.getString("name", null));
					}
					else
					{
						sub.setCreatureType(EntityType.valueOf(subordinate.getString("type", null)));	
						subList.add(sub);
						raremob.setSubordinates(subList);
					}
				}
				
				// Load raredrops for this raremob
				List<MapWrapper> raredrops = rawards.getMapList("drops", new ArrayList<MapWrapper>());
				
				if (raredrops.size() != 0)
				{	
					RareDropsFactory rardropsFactory = new RareDropsFactory(raredrops);
					
					RareDropsOdds dropsOdd = rardropsFactory.createOdds();
					raremob.setRaredropsOdds(dropsOdd);
				}
				
				data.addRareMob(raremob);
			}
		}
		
		return data;
	}
}
