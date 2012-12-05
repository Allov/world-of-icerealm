package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.raredrops.RareDropsChat;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.MultipleRareDropsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.RareDropsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;
//import net.milkbowl.vault.economy.Economy;

public class RareMobsEntityListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	//private final Economy economy;
	
	/*public RareMobsEntityListener(Economy economy)
	{
		this.economy = economy;
	}*/

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{  
        if(event.getEntity() instanceof Monster)
        { 
        	CurrentRareMob raremob = CurrentRareMob.getInstance();
        	Monster entity = (Monster)event.getEntity();
        	
        	// If there's a living rare mob and if he just died
        	if (raremob.getRareMobEntityId() == entity.getEntityId())
        	{      		
	        	RareDropsRandomizer randomizer = new MultipleRareDropsRandomizer(raremob.getRareMob().getRaredropsOdds());
	        	ArrayList<RareDropResult> items =  randomizer.randomize(); 
	        	
	        	// Add drops
	        	for (int i = 0; i < items.size(); i++)
	        	{
	        		RareDropResult raredrop = items.get(i);
	        		event.getDrops().add(raredrop.getItemStack());
	        	}
	        	
	        	// Experience levels will be devided
	        	event.setDroppedExp(0);
	        	
	        	int levels = raremob.getRareMob().getExperienceLevels() / raremob.getFighters().size();
	        	//int money = raremob.getRareMob().getMoney() / raremob.getFighters().size();
	        	
    			for (Player p : raremob.getFighters())
    			{
    				p.setLevel(p.getLevel() + levels);
    				
    			/*	if (economy != null)
    				{
	    				economy.depositPlayer(p.getName(), levels);
	
	    				p.sendMessage(ChatColor.GRAY + "Reward: " + ChatColor.GOLD
	    				+ money + ChatColor.GRAY + " gold and " + ChatColor.GOLD + levels + ChatColor.GRAY + " levels.");
    				}*/
    				//else
    				//{
	    				p.sendMessage(ChatColor.GRAY + "Rewards: " + ChatColor.GOLD + levels + ChatColor.GRAY + " levels.");
    				//}
    			}
	        	
	        	List<Entity> nearbyEntities = entity.getNearbyEntities(100, 100, 100);
	        	
	        	// Notify the players in range for all obtained rare drops
	        	for (Entity nearbyEntity : nearbyEntities)
	        	{
	        		if (nearbyEntity instanceof Player)
	        		{
	        			RareDropsChat.notifyPlayer(items, (Player)nearbyEntity, EntityUtilities.getEntityName(entity)); 
	        		}
	        	}
	
	        	// Notify all the players on server that the rare drop is no more
	        	if (entity.getKiller() == null)	
	        	{
	        		entity.getServer().broadcastMessage(ChatColor.GREEN + raremob.getRareMob().getMobName() + " is no more. ");
	        	}
	        	else
	        	{
	        		entity.getServer().broadcastMessage(ChatColor.GREEN + raremob.getRareMob().getMobName() + " was slain by " + ChatColor.GOLD + entity.getKiller().getName());
	        	}
	        	
	        	// Reset current mob
	        	raremob.clear();
	        	
	        	RareMobSpawner.clearCompass(entity.getWorld());
        	}
        }
	}
}
