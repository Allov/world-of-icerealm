package ca.qc.icerealm.bukkit.plugins.perks.magic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicData;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicTogglingService;

public class ExecuteMagicEvent implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(org.bukkit.event.player.PlayerInteractEvent event)
	{  
		Player p = event.getPlayer();
		MagicTogglingService magicTogglingService = MagicTogglingService.getInstance();
		
	    if(event.getItem() != null && event.getAction() != null && event.getItem().getType() != null && event.getItem().getType().equals(Material.STICK))
	    { 
	    	if (PerkService.getInstance().playerHasPerk(p, MagicData.getFirstMagicInstance(MagicData.SCHOOL_OF_FIRE).getPerkId()))
	    	{
				// If left-clicked, toggle magic
			    if(event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))
			    { 
			    	magicTogglingService.toggle(p, MagicData.SCHOOL_OF_FIRE);
					p.sendMessage(ChatColor.GREEN + ">> Current perk: " + ChatColor.RED + magicTogglingService.getMagicTogglingData(p.getName(), 1).getDisplayName());
			    }
			    // If Right-clicked, execute magic
			    else if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			    { 
			    	if (p.getFoodLevel() > 4)
			    	{
						//p.sendMessage(ChatColor.RED + ">> Execute magic");
						MagicPerk magic = magicTogglingService.getMagicTogglingData(p.getName(), MagicData.SCHOOL_OF_FIRE);
						magic.executeMagic(p);
						
						p.setFoodLevel(p.getFoodLevel() - magic.getFoodCost());
			    	}
			    	else
			    	{
			    		p.sendMessage(ChatColor.RED + ">> You are too tired to cast a spell.");
			    	}
			    }
	    	}
	    	else
	    	{
	    		p.sendMessage(ChatColor.RED + ">> You can't use magic yet. Please buy perks from the one of the magic trees. ");
	    	}
	    }
	}
}