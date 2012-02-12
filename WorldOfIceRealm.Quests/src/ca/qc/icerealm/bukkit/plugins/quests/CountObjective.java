package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class CountObjective extends Objective {

	private int amount;
	private int current;

	public CountObjective(Player player, WorldZone zone, String name, int amount) {
		super(player, zone, name);
		// TODO Auto-generated constructor stub
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
	
	public int getCurrent() {
		return current > amount ? amount : current;
	}

	public void advance(int increment) {
		if (!isCompleted()) {
			current = current + increment;
			
			if (current >= amount) {
				setCompleted(true);
				objectiveProgressed();
				objectiveCompleted();
			} else {
				objectiveProgressed();
			}

		}
	}
	
	public void regress() {
		regress(1);
	}
	
	public void regress(int decrement) {
		if (current > 0) {
			setCompleted(false);
			
			current = current - decrement;
			
			if (current <= 0) {
				current = 0;
			}
			
			objectiveProgressed();
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
}
