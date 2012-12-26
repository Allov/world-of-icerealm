package ca.qc.icerealm.bukkit.plugins.perks.magic;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.PlayerMagicData;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicUtils;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicDataService;

public class ExecuteMagicEvent implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(org.bukkit.event.player.PlayerInteractEvent event)
	{  
		Player p = event.getPlayer();
		MagicDataService magicDataService = MagicDataService.getInstance();
		
	    if(event.getItem() != null && event.getAction() != null && event.getItem().getType() != null && event.getItem().getType().equals(Material.BLAZE_ROD))
	    { 
	    	if (PerkService.getInstance().playerHasPerk(p, MagicUtils.getFirstMagicInstance(MagicUtils.SCHOOL_OF_FIRE).getPerkId()))
	    	{
				// If left-clicked, toggle magic
			    if(event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))
			    { 
			    	magicDataService.toggle(p, MagicUtils.SCHOOL_OF_FIRE);
					p.sendMessage(ChatColor.GREEN + ">> Current magic perk: " + ChatColor.RED + magicDataService.getCurrentMagicPerk(p.getName(), 1).getDisplayName());
			    }
			    // If Right-clicked, execute magic
			    else if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			    { 
			    	PlayerMagicData data = magicDataService.getMagicData(p.getName());
			    	MagicPerk magic = data.getCurrentMagic(MagicUtils.SCHOOL_OF_FIRE);	
			    	
			    	// Before casting a spell, make sure the player have the rights to cast it (in case he died and lost all his perks, or perks were reset for any reason)
			    	if (PerkService.getInstance().playerHasPerk(p, magic.getPerkId()))
			    	{
				    	// Check if cool down if over for this spell
				    	if (data.getLastTimeCast() + magic.getCoolDown() <= System.currentTimeMillis())
				    	{
					    	if (p.getFoodLevel() > magic.getFoodCost() + MagicDataService.MINIMUM_FOOD_LEVEL)
					    	{
								magic.executeMagic(p);
								
								// Apply food cost
								p.setFoodLevel(p.getFoodLevel() - magic.getFoodCost());
								
								// If regen time was stopped, start it when casting spell
								if (data.getLastTimeRegenerated() == 0)
								{
									data.setLastTimeRegenerated(System.currentTimeMillis());
								}
								
								// Add to food level to regen counter							
								data.addFoodLevelsToRegen(magic.getFoodCost());
								
								// Set current time cast
								magicDataService.getMagicData(p.getName()).setLastTimeCast(System.currentTimeMillis());
					    	}
					    	else
					    	{
					    		p.sendMessage(ChatColor.RED + ">> You are too tired to cast a spell.");
					    	}
				    	}
				    	else // No ready to cast yet, output remaining seconds before the player can cast again
				    	{
				    		long timeBeforeCasting = magic.getCoolDown() - (System.currentTimeMillis() - data.getLastTimeCast());
				    		DecimalFormat formater = new DecimalFormat("#.000");
				    		String seconds = formater.format(new Double(timeBeforeCasting) / 1000.00);
				    		p.sendMessage(ChatColor.RED + ">> You must wait " + ChatColor.YELLOW + seconds  + ChatColor.RED + " seconds before casting again.");
				    	}
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