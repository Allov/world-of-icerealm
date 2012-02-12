package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;

public class Reward {
	private int level;
	private int money;
	private List<ItemReward> items;
	private final Economy economy;
	
	public Reward(int level, int money) {
		this.level = level;
		this.money = money;
		this.economy = null;
	}

	public Reward(int level, int money, Economy economy) {
		this.level = level;
		this.money = money;
		this.economy = economy;		
	}
	
	public List<ItemReward> getItems() {
		if (items == null) {
			items = new ArrayList<ItemReward>();
		}
		
		return items;
	}
	
	public void giveTo(Player player) {
		if (player != null) {
			player.setLevel(player.getLevel() + this.level);
			
			if (this.economy != null) {
				this.economy.depositPlayer(player.getName(), this.money);
			}
			
			for (ItemReward item : getItems()) {
				PlayerInventory inventory = player.getInventory();
				
				Material material = Material.getMaterial(item.getId());
				if (material != null) {
					ItemStack stack = new ItemStack(material, item.getAmount());
					
					HashMap<Integer, ItemStack> leftOvers = inventory.addItem(stack);
					for (ItemStack itemStack : leftOvers.values()) {
						player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
					}
					
				}				
			}
		}
	}
	
	@Override
	public String toString() {
		String message = "";
		boolean addAnd = true;
		
		if (this.level > 0) {
			message = message + ChatColor.YELLOW + this.level + ChatColor.DARK_GREEN + " level";
		}
		
		if (this.money > 0) {
			if (addAnd)
				message = message + " and ";
			
			message = message + ChatColor.YELLOW + this.money + ChatColor.DARK_GREEN + " gold";
		}
		
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
