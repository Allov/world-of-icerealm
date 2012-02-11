package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.LootingEnchantmentHandler;

public class RareDropsEntityListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{     
        if(event.getEntity() instanceof Monster)
        { 
        	Monster entity = (Monster)event.getEntity();
        	
        	if (entity.getKiller() instanceof Player)
        	{  
        		double multiplier = 1.00;
        		
        		LootingEnchantmentHandler lootingEnchantmentHandler = new LootingEnchantmentHandler();
        		multiplier = lootingEnchantmentHandler.getMultipler(entity.getKiller());
        		
        		//multiplier = multiplier * 50;
        		
	        	RareDropsFactory factory = new RareDropsFactory(multiplier);
	        	RareDropsOdds odds = factory.createEntityOdds(entity);
	        	
	        	RareDropsRandomizer randomizer = new RareDropsRandomizer(odds);
	        	
	        	ArrayList<ItemStack> items =  randomizer.randomize();
	        	
	        	/*ItemStack t = new ItemStack(Material.DIAMOND_SWORD, 1);
	        	t.addEnchantment(Enchantment.FIRE_ASPECT, 1);
	        	
	        	items.add(t);
	        	
	        	t = new ItemStack(Material.DIAMOND_SWORD, 1);
	        	t.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 2);
	        	
	        	items.add(t);
				*/
	        	
	        	// Notifier le player pour tous les items obtenus (rare drops seulement)
	        	for (int i = 0; i < items.size(); i++)
	        	{
	        		entity.getKiller().sendMessage( ChatColor.YELLOW + EntityUtilities.getEntityName(entity) + " dropped a " + ChatColor.DARK_PURPLE + MaterialUtil.getMaterialFriendName(items.get(i).getType().name()) + (items.get(i).getEnchantments().size() != 0 ? " (enchanted)":""));
	        	}	    
	        	
	        	event.getDrops().addAll(items);
        	}
        }
	}
}
