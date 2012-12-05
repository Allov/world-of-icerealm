package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.CustomCompassManager;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultipliers;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMob;

public class RareMobSpawner 
{
	private RareMob rareMob = null;
	private Server bukkitServer;
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public RareMobSpawner(Server bukkitServer, RareMob rareMob)
	{
		setRareMob(rareMob); 
		this.bukkitServer = bukkitServer;
	}
	
	public void spawnMobWithSubordinates(Location loc)
	{
		// Choose a player randomly
		List<Player> players = bukkitServer.getWorld("World").getPlayers(); 
		
		// Don's spawn if all players left
		if (players.size() != 0)
		{
			ArrayList<String> spawnedLocations = new ArrayList<String>();
			spawnedLocations.add(loc.toString());
			
			// Cancel if no spawning loc was found
			if (loc != null)
			{
				LivingEntity mob = (LivingEntity)bukkitServer.getWorld("World").spawnEntity(loc, rareMob.getCreatureType());
				
				CurrentRareMob currentRareMob = CurrentRareMob.getInstance();
				currentRareMob.setRareMobEntityId(mob.getEntityId());
				currentRareMob.setCurrentHealth(currentRareMob.getRareMob().getHealth());
						
				// Cancel base raredrops for this type of monster, we handle the drops ourselves
				RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(mob.getEntityId(), new RareDropsMultipliers(0.00, 0.00, 0.00));
				
				//RareDropsMultipliers customMultiplier = RareDropsMultiplierData.getInstance().getEntityMutipliers().get(mob.getEntityId());
    			//logger.info("eee: "+customMultiplier.getLowValueMultiplier() + "," + customMultiplier.getMediumValueMultiplier() + ","+ customMultiplier.getHighValueMultiplier());
    			
				List<Integer> subordinatesIds = new CopyOnWriteArrayList<Integer>();
				Hashtable<Integer, Double> subHealth = new Hashtable<Integer, Double>();				
				
				if (rareMob.getSubordinates().size() != 0)
				{
					double radius = Math.ceil(Math.sqrt(rareMob.getSubordinates().size())) * 2;
					WorldZone subordinatesZone = new WorldZone(loc, radius);
	
					// Spawn his subordinates	
					for (int i = 0; i < rareMob.getSubordinates().size(); i++)
					{
						// Found a location where a mob didn't spawn yet, try 5 times.. if there's still a mob at that position, make it spawn at same location
						Location locSub = subordinatesZone.getRandomHighestLocation(bukkitServer.getWorld("World"));
						int tries = 0;
						
						if (locSub != null)						
						{
							while (spawnedLocations.contains(locSub) && tries < 5)
							{
								locSub = subordinatesZone.getRandomHighestLocation(bukkitServer.getWorld("World"));
								tries++;
							}
						}
						else
						{
							logger.warning("Couldn't find a location for subordinate, radius: " + radius);
							locSub = loc;
						}
		
						LivingEntity subordinate = (LivingEntity) bukkitServer.getWorld("World").spawnEntity(locSub, rareMob.getSubordinates().get(i).getCreatureType());	
						spawnedLocations.add(locSub.toString());
						subordinatesIds.add(subordinate.getEntityId());
						
						subHealth.put(subordinate.getEntityId(), subordinate.getHealth() * rareMob.getSubordinatesHealthMultiplier());
					}
				}
				
				currentRareMob.setCurrentSubordinateHealth(subHealth);
				currentRareMob.setSubordinatesEntityId(subordinatesIds);
			}
		}
	}
	
	
	public static void clearCompass(World world)
	{
    	for (Player player : world.getPlayers())
		{
        	CustomCompassManager manager = new CustomCompassManager("RareMobs", player);
        	manager.removeCustomLocation();
		}
	}

	public RareMob getRareMob() 
	{
		return rareMob;
	}

	public void setRareMob(RareMob rareMob) 
	{
		this.rareMob = rareMob;
	}	
}
