package ca.qc.icerealm.bukkit.plugins.perks.magic;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicDataService;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.PlayerMagicData;

public class MagicFoodLevelEvent implements Listener
{
	@EventHandler
	public void onEat(FoodLevelChangeEvent event)
	{
		if (event.getEntityType() == EntityType.PLAYER)
		{	
			Player player = (Player)event.getEntity();
			int foodLevelAdded = player.getFoodLevel() - event.getFoodLevel();
			
			// Monitor added food level (food consumed) only
			if (foodLevelAdded < 0)
			{
				MagicDataService magicDataService = MagicDataService.getInstance();
				PlayerMagicData data = magicDataService.getMagicData(player.getName());
				
				if (player.getFoodLevel() < MagicDataService.MAX_FOOD_LEVEL)
				{				
					data.addFoodLevelsToRegen(foodLevelAdded);		
				}
				else // Reached maximum, make sure it doesn't regenerate for any reason
				{
					data.setFoodLevelsToRegen(0);
					data.setLastTimeRegenerated(0);
				}
			}
		}
	}
}