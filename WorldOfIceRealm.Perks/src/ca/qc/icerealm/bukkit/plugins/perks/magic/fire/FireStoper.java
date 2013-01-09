package ca.qc.icerealm.bukkit.plugins.perks.magic.fire;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import ca.qc.icerealm.bukkit.plugins.scenarios.tools.BlockContainer;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.BlockRestore;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
 
public class FireStoper implements Listener 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event)
    {   
    	//logger.info("Cause: " + event.getCause().name());
    	
    	// Because we don't have any info about which player sent the fireball, disable block burning in World only (won't affect nether)
        if(event.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL
        		&& event.getBlock().getWorld().getName().equalsIgnoreCase("World"))
        {
        	event.setCancelled(true);
        }
    }
    
 /*   @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event)
    {   
    	logger.info("Entity: " + event.getEntity().getType().name());
        if(event.getEntity().getType().equals(EntityType.FIREBALL))
        {
        	event.setCancelled(true);
        }
    }*/
    
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDestroy(EntityExplodeEvent event) 
	{
		try 
		{
			//logger.info("Block destroy, entity: " + event.getEntityType().name());
			if (event.getEntityType().getTypeId() == EntityType.FIREBALL.getTypeId()
					&& event.getEntity().getWorld().getName().equalsIgnoreCase("World"))
			{
			//if (_config.RegenerateExplodedBlocks && _zone.isInside(event.getEntity().getLocation())) {
				HashMap<Location, BlockContainer> _blocks = new HashMap<Location, BlockContainer>();
				for (Block b : event.blockList()) {
					BlockContainer bc = new BlockContainer();
					bc.TypeId = b.getTypeId();
					//logger.info("Adding: " + bc.TypeId);
					bc.TypeData = b.getData();
					_blocks.put(b.getLocation(), bc);
				}
				
				BlockRestore restore = new BlockRestore(event.getEntity().getWorld(), _blocks);
				//logger.info("Restoring... ");
				Executors.newSingleThreadScheduledExecutor().schedule(restore, 1, TimeUnit.MILLISECONDS);
			}
		}
		catch (Exception ex) 
		{
			
		}	
	}
}