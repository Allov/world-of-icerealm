package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class ChestSpawner implements TimeObserver {

	private long _alarm;
	private Location _location;
	
	public ChestSpawner(Location l) {
		_location = l;
	}
	
	@Override
	public void timeHasCome(long time) {
		
		/*
		
		b.setType(Material.CHEST);
		
		Chest c = (Chest)b;
		
		*/
		Block b = _location.getWorld().getBlockAt(_location);
		
		b.setType(Material.CHEST);
		CraftChest c = new CraftChest(b);
		ItemStack stack= new ItemStack(Material.DIRT, 64);
		c.getInventory().setItem(0, stack);
		
		
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
