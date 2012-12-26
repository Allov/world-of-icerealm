package ca.qc.icerealm.bukkit.plugins.perks.magic.data;

import java.util.Hashtable;
import java.util.logging.Logger;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;

public class PlayerMagicData 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Hashtable<Integer, MagicPerk> magicData = new Hashtable<Integer, MagicPerk>();
	private long lastTimeCast;
	private int foodLevelsToRegen = 0;
	private long lastTimeRegenerated = 0;

	public Hashtable<Integer, MagicPerk> getCurrentMagic() 
	{
		return magicData;
	}
	
	public MagicPerk getCurrentMagic(int school) 
	{
		MagicPerk magicPerk = magicData.get(school);
		
		if (magicPerk == null)
		{
			magicData.put(school, MagicUtils.getFirstMagicInstance(school));
		}
		
		return magicData.get(school);
	}

	public void setCurrentMagic(Hashtable<Integer, MagicPerk> magicData) 
	{
		this.magicData = magicData;
	}
	
	public void setCurrentMagic(int school, MagicPerk magicPerk) 
	{
		this.magicData.put(school, magicPerk);
	}

	public long getLastTimeCast() 
	{
		return lastTimeCast;
	}

	public void setLastTimeCast(long lastTimeCast) 
	{
		this.lastTimeCast = lastTimeCast;
	}

	public int getFoodLevelsToRegen() 
	{
		return foodLevelsToRegen;
	}

	public void setFoodLevelsToRegen(int foodLevelsToRegen) 
	{
		this.foodLevelsToRegen = foodLevelsToRegen;
	}
	
	public synchronized void addFoodLevelsToRegen(int foodLevels)
	{
		if (this.foodLevelsToRegen + foodLevels > MagicDataService.MAX_FOOD_LEVEL - MagicDataService.MINIMUM_FOOD_LEVEL)
		{
			this.foodLevelsToRegen = MagicDataService.MAX_FOOD_LEVEL - MagicDataService.MINIMUM_FOOD_LEVEL;
		}
		else
		{
			this.foodLevelsToRegen += foodLevels;
			
			if (foodLevelsToRegen <= 0)
			{
				foodLevelsToRegen = 0;
				lastTimeRegenerated = 0;
			}
		}
		
		//logger.info("foodLevelsToRegen: " + foodLevelsToRegen);
	}

	public long getLastTimeRegenerated() 
	{
		return lastTimeRegenerated;
	}

	public void setLastTimeRegenerated(long lastTimeRegenerated) 
	{
		this.lastTimeRegenerated = lastTimeRegenerated;
	}
}
