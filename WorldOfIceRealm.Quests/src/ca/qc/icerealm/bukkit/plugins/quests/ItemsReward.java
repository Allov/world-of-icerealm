package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;

public class ItemsReward implements Reward {

	private List<ItemReward> items;

	public ItemsReward() {
	}
	
	public ItemsReward(List<ItemReward> items) {
		this.items = items;		
	}
	
	@Override
	public void give(Player player) {
		for (ItemReward item : getItems()) {
			Material material = Material.getMaterial(item.getId());
			if (material != null) {
				ItemStack stack = new ItemStack(material, item.getAmount());
				
				PlayerInventory inventory = player.getInventory();
				HashMap<Integer, ItemStack> leftOvers = inventory.addItem(stack);
				for (ItemStack itemStack : leftOvers.values()) {
					player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
				}
			}				
		}		
	}

	public List<ItemReward> getItems() {
		if (items == null) {
			items = new ArrayList<ItemReward>();
		}
		
		return items;
	}
	
	@Override
	public String toString() {
		String message = "";
		boolean addAnd = false;
		
		if (this.getItems().size() > 0) {
			if (addAnd)
				message = message + " and ";
			
			for (ItemReward item : getItems()) {
				Material material = Material.getMaterial(item.getId());
				message = message + ChatColor.GREEN + item.getAmount() + " " + MaterialUtil.getMaterialFriendName(material.name()) + ChatColor.DARK_GREEN + ", ";  
			}
			
			message = message.substring(0, message.length() - 2);
		}
		
		return message;
	}
}
