package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Quest implements ObjectiveListener {
	private String key;
	private String name;
	private String messageStart;
	private String messageEnd;
	private boolean daily;
	private long cooldown;
	private Player player;
	private boolean completed;
	private long completionTime;
	private String requires;
	
	private List<Objective> objectives;
	private List<Reward> rewards;
	private List<QuestListener> listeners = new CopyOnWriteArrayList<QuestListener>();

	public Quest(Player player, String key, String name, String requires, String messageStart, String messageEnd, boolean daily, long cooldown) {
		this.player = player;
		this.key = key;
		this.name = name;
		this.cooldown = cooldown;
		this.requires = requires;
		this.messageStart = messageStart;
		this.messageEnd = messageEnd;
		this.daily = daily;
	}
	
	public void addListener(QuestListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(QuestListener listener) {
		listeners.remove(listener);
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
		getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Quest: " + ChatColor.YELLOW + this.name);
		getPlayer().sendMessage(ChatColor.DARK_GREEN + this.messageStart);
		
		for (Objective objective : this.objectives) {
			getPlayer().sendMessage("  > " + objective.toString());
		}
		
		if (getRewards().size() > 0) {
			getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Your rewards will be: ");
			displayRewards(getPlayer());
		}
	}

	private void displayRewards(Player player) {
		for (Reward reward : getRewards()) {
			player.sendMessage("  > " + reward.toString());
		}
	}

	@Override
	public void objectiveProgressed(Objective objective) {
		for(QuestListener listener : listeners) {
			listener.questProgressed(this, objective);
		}
		
		getPlayer().sendMessage( ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + this.name + ChatColor.LIGHT_PURPLE + "] " + 
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
		
		completed = true;
		completionTime = System.currentTimeMillis();

		for (Objective obj : objectives) {
			obj.unregister(this);
		}
		
		for (QuestListener listener : listeners) {
			listener.questCompleted(this);
		}
		
		getPlayer().sendMessage(ChatColor.DARK_GREEN + this.messageEnd);
		getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Quest is " + ChatColor.GREEN + "done" + ChatColor.DARK_GREEN + "!");
		getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "You received : ");
		displayRewards(getPlayer());

		// Objectives are done, reward the player;
		giveRewards(getPlayer());
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

	public Player getPlayer() {
		return player;
	}

	public void setCompletionTime(long completionTime) {
		this.completionTime = completionTime;
		this.completed = completionTime > 0;		
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public boolean isDailyCooldownOver() {
		return System.currentTimeMillis() - this.getCompletionTime() > this.getCooldown();
	}
}
