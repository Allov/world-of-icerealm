package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class BlockRestore implements TimeObserver {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm;

	private World _world;
	private HashMap<Location, BlockContainer> _blocks;
	private Location _location;
	private long _delayIfEntitiesPresent = 10000;
	
	public BlockRestore(World w, HashMap<Location, BlockContainer> b) {
		_world = w;
		_blocks = b;
	}
	 
	@Override
	public void timeHasCome(long time) {
			
		for (Location l : _blocks.keySet()) {
			Block b = _world.getBlockAt(l);
			b.setTypeId(_blocks.get(l).TypeId);
			b.setData(_blocks.get(l).TypeData);
		}
	}

	@Override
	public void setAlaram(long time) {
		_alarm = time;
	}

	@Override
	public long getAlarm() {
		return _alarm;
	}

}
