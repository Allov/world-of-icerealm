package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ca.qc.icerealm.bukkit.plugins.quests.builder.ItemRewardDefinition;

public class Reward {
	private int level;
	private int money;
	private List<ItemRewardDefinition> items;
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
	
	public List<ItemRewardDefinition> getItems() {
		if (items == null) {
			items = new ArrayList<ItemRewardDefinition>();
		}
		
		return items;
	}
	
	public void giveTo(Player player) {
		if (player != null) {
			player.giveExp(this.level);
			
			if (this.economy != null) {
				this.economy.depositPlayer(player.getName(), this.money);
			}
			
			for (ItemRewardDefinition item : getItems()) {
				PlayerInventory inventory = player.getInventory();
				
				Material material = Material.getMaterial(item.getId());
				if (material != null) {
					inventory.addItem(new ItemStack(material, item.getAmount()));
				}				
			}
		}
	}
	
	@Override
	public String toString() {
		String message = "";
		boolean addAnd = true;
		
		if (this.level > 0) {
			message = message + ChatColor.YELLOW + this.level + ChatColor.DARK_PURPLE + " experience";
		}
		
		if (this.money > 0) {
			if (addAnd)
				message = message + " and";
			
			message = message + ChatColor.YELLOW + this.money + ChatColor.DARK_PURPLE + " gold";
		}
		
		if (this.getItems().size() > 0) {
			if (addAnd)
				message = message + " and";
			
			for (ItemRewardDefinition item : getItems()) {
				Material material = Material.getMaterial(item.getId());
				message = message + ChatColor.GREEN + item.getAmount() + material.name() + ChatColor.DARK_PURPLE + ", ";  
			}
			
			message = message.substring(0, message.length() - 2);
		}
		
		return message;
	}
}
