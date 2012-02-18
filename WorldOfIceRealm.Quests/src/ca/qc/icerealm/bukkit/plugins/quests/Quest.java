package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Quest implements ObjectiveListener {
	public final Logger logger = Logger.getLogger(("Minecraft"));

	private String key;
	private String name;
	private String messageStart;
	private String messageEnd;
	private boolean daily;
	private Player player;
	private boolean completed;
	
	private List<Objective> objectives;
	
	private Fees joinFees;
	private Fees dropFees;
	private List<Reward> rewards;

	private long completionTime;

	private String requires;

	public Quest(Player player, String key, String name, String requires, String messageStart, String messageEnd, boolean daily, Fees joinFees, Fees dropFees) {
		this.player = player;
		this.key = key;
		this.name = name;
		this.setRequires(requires);
		this.messageStart = messageStart;
		this.messageEnd = messageEnd;
		this.daily = daily;
		this.joinFees = joinFees;
		this.dropFees = dropFees;
	}
	
	public List<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new ArrayList<Objective>();
		}
		
		return objectives;
	}
	
	public List<Reward> getRewards() {
		if (rewards == null) {
			rewards = new ArrayList<Reward>();
		}
		
		return rewards;
	}

	public void drop() {
		
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void info() {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Quest: " + ChatColor.YELLOW + this.name);
		player.sendMessage(ChatColor.DARK_GREEN + this.messageStart);
		
		for (Objective objective : this.objectives) {
			player.sendMessage("  > " + objective.toString());
		}
		
		if (getRewards().size() > 0) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rewards will be: ");
			displayRewards(player);
		}
	}

	private void displayRewards(Player player) {
		for (Reward reward : getRewards()) {
			player.sendMessage("  > " + reward.toString());
		}
	}

	@Override
	public void objectiveProgressed(Objective objective) {
		player.sendMessage( ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + this.name + ChatColor.LIGHT_PURPLE + "] " + 
							ChatColor.GREEN + " PROGRESS: "  + objective.toString());
	}

	@Override
	public void objectiveFailed(Objective objective) {
		
	}

	@Override
	public void objectiveCompleted(Objective objective) {
		for (Objective obj : objectives) {
			if (!obj.isCompleted()) {
				return;
			}
		}
		
		for (Objective obj : objectives) {
			obj.unregister(this);
			obj.questCompleted();
		}
		
		completed = true;
		completionTime = System.currentTimeMillis();
		
		player.sendMessage(ChatColor.DARK_GREEN + this.messageEnd);
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Quest is " + ChatColor.GREEN + "done" + ChatColor.DARK_GREEN + "!");
		player.sendMessage(ChatColor.LIGHT_PURPLE + "You received : ");
		displayRewards(player);

		// Objectives are done, reward the player;
		giveRewards(player);
	}
	
	private void giveRewards(Player player) {
		for (Reward reward : getRewards()) {
			reward.give(player);
		}
	}

	@Override
	public String toString() {
		return ChatColor.LIGHT_PURPLE + "Quest: "  + ChatColor.YELLOW + this.name;
	}

	public String getName() {
		return name;
	}

	public boolean isDaily() {
		return daily;
	}
	
	public String getKey() {
		return key;
	}

	public void reset() {
		completed = false;
		for (Objective objective : this.objectives) {
			objective.reset();
			objective.register(this);
		}
	}

	public long getCompletionTime() {
		return completionTime;
	}

	public String getRequires() {
		return requires;
	}

	public void setRequires(String requires) {
		this.requires = requires;
	}
}
