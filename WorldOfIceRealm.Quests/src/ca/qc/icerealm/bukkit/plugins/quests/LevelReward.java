package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LevelReward implements Reward {

	private int amount;

	public LevelReward(int amount) {
		this.setAmount(amount);		
	}
	
	@Override
	public void give(Player player) {
		player.setLevel(player.getLevel() + getAmount());
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "" + ChatColor.YELLOW + this.getAmount() + ChatColor.DARK_GREEN + " level";
	}

}
