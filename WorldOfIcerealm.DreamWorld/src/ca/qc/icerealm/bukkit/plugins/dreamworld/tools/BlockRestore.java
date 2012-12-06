package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

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

}