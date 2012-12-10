package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.common.NamedItemStack;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsFactory;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultipliers;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.LootingEnchantmentHandler;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.MultipleRareDropsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.RareDropsRandomizer;

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
        	
        	if (entity.getKiller() != null)
        	{  
        		Double generalMultiplier = 1.00;
        		
        		// Apply base multiplier based on looting enchantment
        		LootingEnchantmentHandler lootingEnchantmentHandler = new LootingEnchantmentHandler();
        		generalMultiplier = lootingEnchantmentHandler.getMultipler(entity.getKiller());
        		//generalMultiplier = generalMultiplier * 20.00;
        		
        		// Apply custom multiplier set on one specific entity by another plugin
        		RareDropsMultipliers customMultiplier = RareDropsMultiplierData.getInstance().getEntityMutipliers().get(entity.getEntityId());
        		
        		//logger.info("before");
        		if (customMultiplier != null)
        		{
        			customMultiplier.setLowValueMultiplier(generalMultiplier * customMultiplier.getLowValueMultiplier());
        			customMultiplier.setMediumValueMultiplier(generalMultiplier * customMultiplier.getMediumValueMultiplier());
        			customMultiplier.setHighValueMultiplier(generalMultiplier * customMultiplier.getHighValueMultiplier());
        			
        			//logger.info(customMultiplier.getLowValueMultiplier() + "," + customMultiplier.getMediumValueMultiplier() + ","+ customMultiplier.getHighValueMultiplier());
        			// Remove it from the list
        			RareDropsMultiplierData.getInstance().removeEntityRareDropsMultiplier(entity.getEntityId());
        		}
        		else
        		{
        			customMultiplier = new RareDropsMultipliers(generalMultiplier);
        		}
        		
        		// if multiplier equals 0, the drops are handled elsewhere (by another plugin)
        		if (customMultiplier.getLowValueMultiplier() > 0 || customMultiplier.getMediumValueMultiplier() > 0 || customMultiplier.getHighValueMultiplier() > 0)
        		{
	        		String entityName = entity.getType().name();
	        		List<MapWrapper> materials = configWrapper.getMapList("mobs." + entityName + ".materials", new ArrayList<MapWrapper>());
	        		RareDropsFactory factory = new RareDropsFactory(materials, customMultiplier);
		        	RareDropsOdds odds = factory.createOdds();
		        	
		        	RareDropsRandomizer randomizer = new MultipleRareDropsRandomizer(odds);
		        	
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
		        	
		        	// Add drops
		        	for (int i = 0; i < items.size(); i++)
		        	{
		        		RareDropResult raredrop = items.get(i);
		        		
		        		// Also change the name of the item if needed
		        		if (raredrop.getCustomName() != null && !raredrop.getCustomName().equals(""))
		        		{
		        			ItemStack bukkitItem = NamedItemStack.toCraftBukkit(raredrop.getItemStack());
		        		    NamedItemStack namedItemStack = new NamedItemStack(bukkitItem);
		        		    namedItemStack.setName(raredrop.getCustomName());
		        		    event.getDrops().add(bukkitItem);
		        		}
		        		else
		        		{
		        			event.getDrops().add(raredrop.getItemStack());
		        		}
		        	}
		        	
		        	// Notify the player for all obtained rare drops
		        	RareDropsChat.notifyPlayer(items, entity.getKiller(), EntityUtilities.getEntityName(entity));        	
        		}
        	}
        }
	}
}
