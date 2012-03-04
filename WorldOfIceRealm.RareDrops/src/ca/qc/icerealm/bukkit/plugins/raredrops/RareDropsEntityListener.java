package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsFactory;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.LootingEnchantmentHandler;

public class RareDropsEntityListener implements Listener
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public RareDropsEntityListener(ConfigWrapper config)
	{
		setConfigWrapper(config);
	}
	
	private ConfigWrapper configWrapper = null;
	
	public ConfigWrapper getConfigWrapper() 
	{
		return configWrapper;
	}

	public void setConfigWrapper(ConfigWrapper configWrapper) 
	{
		this.configWrapper = configWrapper;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{     
        if(event.getEntity() instanceof Monster)
        { 
        	Monster entity = (Monster)event.getEntity();
        	
        	if (entity.getKiller() instanceof Player)
        	{  
        		double multiplier = 1.00;
        		
        		// Apply base multiplier based on looting enchantment
        		LootingEnchantmentHandler lootingEnchantmentHandler = new LootingEnchantmentHandler();
        		multiplier = lootingEnchantmentHandler.getMultipler(entity.getKiller());
        		
        		// Apply custom multiplier set on one specific entity by another plugin
        		Double customMultiplier = RareDropsMultiplierData.getInstance().getEntityMutipliers().get(entity.getEntityId());
        		
        		if (customMultiplier != null)
        		{
        			multiplier = multiplier * customMultiplier;
        		}
        		
        		//multiplier = multiplier * 50;
        		
        		// if multiplier equals 0, the drops are handled elsewhere (by another plugin)
        		if (multiplier > 0)
        		{
	        		String entityName = EntityUtilities.getEntityCreatureType(entity).name();
	        		List<MapWrapper> materials = configWrapper.getMapList("mobs." + entityName + ".materials", new ArrayList<MapWrapper>());
		        	RareDropsFactory factory = new RareDropsFactory(materials, multiplier);
		        	RareDropsOdds odds = factory.createOdds();
		        	
		        	RareDropsRandomizer randomizer = new RareDropsRandomizer(odds);
		        	
		        	ArrayList<RareDropResult> items =  randomizer.randomize();
		        	
		        	/*ItemStack t = new ItemStack(Material.SHEARS, 1);
		        	t.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		        	t.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		        	t.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
		        	
		        	items.add(t);*/
		        	/*
		        	t = new ItemStack(Material.DIAMOND_SWORD, 1);
		        	t.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 2);
		        	
		        	items.add(t);
					*/
		        	
		        	// Notify the player for all obtained rare drops
		        	for (int i = 0; i < items.size(); i++)
		        	{
		        		RareDropResult raredrop = items.get(i);
		        		
		        		String itemName = raredrop.getItemStack().getType().name();
		        		
		        		// If no enchantment, this isn't a custom item.
		        		if (raredrop.getCustomName() != null && raredrop.getItemStack().getEnchantments() != null && !raredrop.getItemStack().getEnchantments().isEmpty())
		        		{
		        			itemName = raredrop.getCustomName();
		        		}
		        		
		        		entity.getKiller().sendMessage( ChatColor.YELLOW + EntityUtilities.getEntityName(entity) + " dropped a " + ChatColor.DARK_PURPLE + MaterialUtil.getMaterialFriendName(itemName) + (raredrop.getItemStack().getEnchantments().size() != 0 ? " (enchanted)":""));
		        		event.getDrops().add(raredrop.getItemStack());
		        	}	
        		}
        	}
        }
	}
}
