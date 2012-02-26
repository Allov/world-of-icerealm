package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public abstract class CountObjective extends Objective {

	private int amount;
	private int current;

	public CountObjective(Player player, WorldZone zone, String name, int amount) {
		super(player, zone, name);
		// TODO Auto-generated constructor stub
		this.amount = amount;
	}
	
	public void setCurrent(int current) {
		this.current = current;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getCurrent() {
		return current > amount ? amount : current;
	}

	public void advance(int amount) {
		if (!isCompleted() && amount != 0) {
			current = current + amount;
			
			if (current >= this.amount) {
				current = getAmount();
				setCompleted(true);
				objectiveProgressed();
				objectiveCompleted();
			} else {
				objectiveProgressed();
			}

		}
	}
	
	public void advance() {
		advance(1);
	}

	@Override
	public String toString() {
		return 	ChatColor.YELLOW + this.getName() +
				ChatColor.GREEN + " (" + this.getCurrent() + "/" + this.getAmount() + ")" +
				(isCompleted() ? ChatColor.GRAY + " (COMPLETED)" : "");
	}
	
	@Override
	public void reset() {
		super.reset();
		this.current = 0;
	}
}
