package ca.qc.icerealm.bukkit.plugins.raredrops.enchantment;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class LootingEnchantmentHandler 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	private double levelOneMultiplier = 1.25;
	private double levelTwoMultiplier = 1.50;
	private double levelThreeMultiplier = 2.00;
	
	public LootingEnchantmentHandler()
	{

	}

	public double getLevelOneMultiplier() 
	{
		return levelOneMultiplier;
	}

	public void setLevelOneMultiplier(double levelOneMultiplier) 
	{
		this.levelOneMultiplier = levelOneMultiplier;
	}

	public double getLevelTwoMultiplier() {
		return levelTwoMultiplier;
	}

	public void setLevelTwoMultiplier(double levelTwoMultiplier) 
	{
		this.levelTwoMultiplier = levelTwoMultiplier;
	}

	public double getLevelThreeMultiplier() {
		return levelThreeMultiplier;
	}

	public void setLevelThreeMultiplier(double levelThreeMultiplier) 
	{
		this.levelThreeMultiplier = levelThreeMultiplier;
	}
	
	public double getMultipler(Player player)
	{
		double multiplier = 1.00;
		
		// Appliquer le comportement de l'enchantement "looting" et affecter le multiplier en conséquence
		Map<Enchantment, Integer> enchs = player.getItemInHand().getEnchantments();
		
		if (enchs.containsKey(Enchantment.LOOT_BONUS_MOBS))
		{	
			if (enchs.get(Enchantment.LOOT_BONUS_MOBS) == 1)
			{
				logger.info("multiplier = 1.25");
				multiplier = 1.25;
			}
			else if (enchs.get(Enchantment.LOOT_BONUS_MOBS) == 2)
			{
				logger.info("multiplier = 1.50");
				multiplier = 1.50;
			}        			
			else if (enchs.get(Enchantment.LOOT_BONUS_MOBS) == 3)
			{
				logger.info("multiplier = 2.00");
				multiplier = 2.00;
			}
		}      			
		
		return multiplier;
	}
}
