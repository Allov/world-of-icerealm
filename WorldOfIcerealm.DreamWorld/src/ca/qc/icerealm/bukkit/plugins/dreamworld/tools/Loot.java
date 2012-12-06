package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Loot {

	private Block _chestBlock;
	private Chest _chestItem;
	private List<ItemStack> _content;
	
	public Loot() {
		_content = new ArrayList<ItemStack>();
	}
	
	public void addItemStack(Material m, int qty) {
		_content.add(new ItemStack(m, qty));
	}
	
	public void generateLoot(Location location) {
		Location l = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
		Block b = location.getWorld().getBlockAt(l);
		b.setType(Material.CHEST);
		
		ItemStack[] stacks = _content.toArray(new ItemStack[_content.size()]);
		
		if (b.getType() == Material.CHEST) {
			_chestBlock = b;
			_chestItem = (Chest)b.getState();
			Inventory inv = _chestItem.getInventory();
			inv.setContents(stacks);
		}
	}
	
	public void removeLoot() {
		if (_chestBlock != null) {
			
			if (_chestBlock.getType() == Material.CHEST) {
				Inventory inv = _chestItem.getInventory();
				inv.clear();
				_chestBlock.setType(Material.AIR);
				_chestBlock = null;
				_chestItem = null;
				_content.clear();
			}			
		}
	}
	
}
