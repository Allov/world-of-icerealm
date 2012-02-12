package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.Calendar;
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
	private Reward reward;

	private long completionTime;

	public Quest(Player player, String key, String name, String messageStart, String messageEnd, boolean daily, Fees joinFees, Fees dropFees, Reward reward) {
		this.player = player;
		this.key = key;
		this.name = name;
		this.messageStart = messageStart;
		this.messageEnd = messageEnd;
		this.daily = daily;
		this.joinFees = joinFees;
		this.dropFees = dropFees;
		this.reward = reward;		
	}
	
	public List<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new ArrayList<Objective>();
		}
		
		return objectives;
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
		
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Your reward will be: " + this.reward.toString());		
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
		
		// Objectives are done, reward the player;
		reward.giveTo(player);
		player.sendMessage(ChatColor.DARK_GREEN + this.messageEnd);
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Quest is " + ChatColor.GREEN + "done" + ChatColor.DARK_GREEN + "!");
		player.sendMessage(ChatColor.LIGHT_PURPLE + "You received " + this.reward.toString());
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
}
