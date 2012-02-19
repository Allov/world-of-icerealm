package ca.qc.icerealm.bukkit.plugins.simplekits;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SimpleKitsProvider {
	private static SimpleKitsProvider instance;
	
	public static SimpleKitsProvider getInstance() {
		if (instance == null) {
			instance = new SimpleKitsProvider();
		}
		
		return instance;
	}
	
	public void kit(String name, Player player) {
		Inventory inventory = player.getInventory();
		inventory.clear();
		
		List<ItemStack> items;
		
		if (name.equalsIgnoreCase("starter")) {
			items = SimpleKitsFactory.getInstance().createStarterKit();
		} else if (name.equalsIgnoreCase("average")) {
			items = SimpleKitsFactory.getInstance().createAverageKit();
		} else {
			items = SimpleKitsFactory.getInstance().createAdvancedKit();
		}
		
		for (ItemStack itemStack : items) {
			inventory.addItem(itemStack);
		}
	}
}
