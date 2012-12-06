package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class BlockRestore implements Runnable {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private World _world;
	private HashMap<Location, BlockContainer> _blocks;
	
	public BlockRestore(World w, HashMap<Location, BlockContainer> b) {
		_world = w;
		_blocks = b;
	}
	 
	@Override
	public void run() {
			
		for (Location l : _blocks.keySet()) {
			Block b = _world.getBlockAt(l);
			b.setTypeId(_blocks.get(l).TypeId);
			b.setData(_blocks.get(l).TypeData);
		}
	}
	
	public static BlockRestore getBlockRestoreFromWorldZone(WorldZone zone) {
		HashMap<Location, BlockContainer> restore = new HashMap<Location, BlockContainer>();
		Location topLeft = zone.getTopLeft();
		Location bottomRight = zone.getRightBottom();
		World world = zone.getWorld();
		
		for (int i = (int)topLeft.getX(); i <= bottomRight.getX(); i++) {
		
			for (int j = (int)topLeft.getZ(); j <= bottomRight.getZ(); j++) {
				
				for (int k = (int)bottomRight.getY(); k <= topLeft.getY(); k++) {
					
					Location loc = new Location(world, i, k, j);
					Block b = world.getBlockAt(loc);
					BlockContainer container = new BlockContainer();
					container.TypeId = b.getTypeId();
					container.TypeData = b.getData();
					restore.put(loc, container);
				}
			}
		}
		
		return new BlockRestore(world, restore);
	}

}