package ca.qc.icerealm.bukkit.plugins.quests;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MoneyReward implements Reward {

	private final Economy economy;
	private final int amount;

	public MoneyReward(Economy economy, int amount) {
		this.economy = economy;
		this.amount = amount;		
	}
	
	@Override
	public void give(Player player) {
		if (economy != null) {
			economy.depositPlayer(player.getName(), this.getAmount());
		}
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "" + ChatColor.YELLOW + this.getAmount() + ChatColor.DARK_GREEN + " gold" + (this.getAmount() > 1 ? "s" : "");
	}
}
