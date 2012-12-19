package ca.qc.icerealm.bukkit.plugins.perks.magic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicData;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicTogglingService;

public class ExecuteMagicEvent implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(org.bukkit.event.player.PlayerInteractEvent event)
	{  
		Player p = event.getPlayer();
		MagicTogglingService magicTogglingService = MagicTogglingService.getInstance();
				
		// If left-clicked, toggle magic
	    if(event.getItem() != null && event.getAction() != null && ((event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))) && event.getItem().getType() != null && event.getItem().getType().equals(Material.STICK)))
	    { 
	    	magicTogglingService.toggle(p.getName(), MagicData.SCHOOL_OF_FIRE);
			p.sendMessage(ChatColor.GREEN + ">> Current perk: " + ChatColor.RED + magicTogglingService.getMagicTogglingData(p.getName(), 1).getDisplayName());

	    }
	    // If Right-clicked, execute magic
	    else if(event.getItem() != null && event.getAction() != null && ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) && event.getItem().getType() != null && event.getItem().getType().equals(Material.STICK)))
	    {
	    	if (p.getFoodLevel() > 4)
	    	{
				//p.sendMessage(ChatColor.RED + ">> Execute magic");
				MagicPerk magic = magicTogglingService.getMagicTogglingData(p.getName(), MagicData.SCHOOL_OF_FIRE);
				magic.executeMagic(p);
				
				//double random = (Math.random() * 100);
				p.setFoodLevel(p.getFoodLevel() - magic.getFoodCost());
	    	}
	    	else
	    	{
	    		p.sendMessage(ChatColor.RED + ">> You are too tired to cast a spell");
	    	}
	    }
	}
}